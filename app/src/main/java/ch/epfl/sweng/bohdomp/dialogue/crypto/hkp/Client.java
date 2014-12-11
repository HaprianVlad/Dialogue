package ch.epfl.sweng.bohdomp.dialogue.crypto.hkp;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.bouncycastle.openpgp.PGPException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.FingerprintUtils;
import ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.KeyNotFoundException;
import ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.PublicKeyChain;
import ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.PublicKeyChainBuilder;
import ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.PublicKeyRing;
import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 * Client to exchange keys with keyservers implementing the HKP protocol
 */
public class Client {

    private final String mLookupUrl;
    private final String mSubmitUrl;

    public Client(String keyServer) {
        Contract.throwIfArgNull(keyServer, "keyServer");
        this.mLookupUrl = "http://" + keyServer + "/pks/lookup";
        this.mSubmitUrl = "http://" + keyServer + "/pks/add";
    }

    /**
     * Create a usable url query with form-encoded parameters
     */
    private static String makeQuery(Map<String, String> vars) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        //set preference for machine-readable format
        params.add(new BasicNameValuePair("options", "mr"));
        for (Map.Entry<String, String> var : vars.entrySet()) {
            params.add(new BasicNameValuePair(var.getKey(), var.getValue()));
        }
        return URLEncodedUtils.format(params, "UTF-8");
    }

    /**
     * Performs a HKP lookup operation.
     *
     * @param vars hkp variables, determining the operation
     * @return the server's response as string or null if the server responded with a 4xx code
     */
    private String lookup(Map<String, String> vars) throws IOException, HkpServerException {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(mLookupUrl + "?" + makeQuery(vars));
        HttpResponse response = client.execute(request);

        String responseCode = Integer.toString(response.getStatusLine().getStatusCode());
        if (responseCode.startsWith("2")) {
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);
        } else if (responseCode.startsWith("4")) {
            return null;
        } else if (responseCode.startsWith("5")) {
            throw new HkpServerException("Lookup operation unsupported, response code: " + responseCode);
        } else {
            throw new HkpServerException("Invalid response code: " + responseCode);
        }
    }

    /**
     * Looks up an ascii-armored key.
     */
    private String lookupKeyString(String fingerprint) throws IOException, HkpServerException, KeyNotFoundException {
        HashMap<String, String> vars = new HashMap<String, String>();
        vars.put("op", "get");
        vars.put("search", fingerprint);

        String response = lookup(vars);
        if (response == null) {
            throw new KeyNotFoundException("Key with fingerprint " + fingerprint + " cannot be found on the server");
        } else {
            return response;
        }
    }

    /**
     * Retrieve a keychain from the server who's master key matches the given fingerprint
     *
     * @param fingerprint the fingerprint in ascii string format of the key to retrieve
     */
    public PublicKeyRing lookupKeyRing(String fingerprint) throws IOException, HkpServerException,
            KeyNotFoundException {

        Contract.throwIfArgNull(fingerprint, "fingerprint");

        try {
            String response = lookupKeyString(FingerprintUtils.toQuery(fingerprint));
            return new PublicKeyChainBuilder().fromString(response).getKeyRing(fingerprint);
        } catch (PGPException ex) {
            throw new HkpServerException("Keyring response is in invalid format");
        }
    }

    private static UrlEncodedFormEntity makeKeySubmissionEntity(String keyString) {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("keytext", keyString));
        try {
            return new UrlEncodedFormEntity(pairs, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new AssertionError("Error, cannot encode a fixed string");
        }
    }

    /**
     * Performs a HKP submit operation.
     *
     * @param keyString the key to submit
     */
    private void submit(String keyString) throws IOException, HkpServerException {
        HttpClient client = new DefaultHttpClient();
        HttpPost request = new HttpPost(mSubmitUrl);
        request.setEntity(makeKeySubmissionEntity(keyString));

        HttpResponse response = client.execute(request);

        String responseCode = Integer.toString(response.getStatusLine().getStatusCode());
        if (responseCode.startsWith("2")) {
            return;
        } else if (responseCode.startsWith("4")) {
            throw new HkpServerException("Server refused submission of key: " + responseCode);
        } else if (responseCode.startsWith("5")) {
            throw new HkpServerException("Submission operation unsupported, response code: " + responseCode);
        } else {
            throw new HkpServerException("Invalid response code: " + responseCode);
        }
    }

    /**
     * Submit a public key to the key server
     */
    public void submitKeyRing(PublicKeyRing keyRing) throws IOException, HkpServerException {
        Contract.throwIfArgNull(keyRing, "keyRing");

        PublicKeyChain chain = new PublicKeyChainBuilder().empty().add(keyRing);
        submit(chain.toArmored());
    }

}
