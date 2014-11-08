package ch.epfl.sweng.bohdomp.dialogue.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Contains utility methods for streams
 */
public class StreamUtils {

    /** Default size of buffers used in utility methods that manitpulate streams. */
    public static final int BUFFER_SIZE = 1024;

    /** Pipe the input of a stream to another output */
    public static void pipe(InputStream in, OutputStream out) throws IOException {
        Contract.throwIfArgNull(in, "in");
        Contract.throwIfArgNull(out, "out");

        byte[] buffer = new byte[BUFFER_SIZE];
        int len;
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
    }

}
