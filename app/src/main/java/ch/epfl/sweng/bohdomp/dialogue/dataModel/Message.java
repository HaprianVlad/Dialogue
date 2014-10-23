package ch.epfl.sweng.bohdomp.dialogue.dataModel;

import android.telephony.SmsMessage;

/**
 * Class representing a Dialogue message
 */
public class Message {

    //FIXME: full implementation is needed
    public static Message convertFromSmsMessage(SmsMessage smsMessage) {
        return new Message(smsMessage.getMessageBody(), null, new Contact());
    }

    public static SmsMessage convertToSmsMessage(Message message) {
        return null;
    }
    private final String body;
    private final String destinationAddress;
    private final Contact sender;


    public Message(String bodyParameter, String destinationAddressParameter,
                   Contact senderParameter
                   ) {
        if (constructorArgumentsOk(bodyParameter, destinationAddressParameter, senderParameter)) {
            this.body = bodyParameter;
            this.destinationAddress = destinationAddressParameter;
            this.sender = senderParameter;
        } else {
            throw new IllegalArgumentException("Invalid parameters for message creation");
        }
    }
    //FIXME: add geters
    public String getBody() {
        return body;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    private boolean constructorArgumentsOk(String bodyParameter,
                                           String destinationAddressParameter, Contact senderParameter) {
        return bodyParameter != null && (senderParameter!=null ^ destinationAddressParameter!=null);
    }

}
