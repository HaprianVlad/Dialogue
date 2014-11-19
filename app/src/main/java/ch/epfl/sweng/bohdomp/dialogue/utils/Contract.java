package ch.epfl.sweng.bohdomp.dialogue.utils;

import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;

/**
 * Contains helpers methods for various checks
 */
public class Contract {
    public static void throwIfArg(Boolean cond, String msg) {
        throwIfArgNull(msg, "msg");

        if (cond) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static <S> S throwIfArgNull(S obj, String name) {
        // Using throwIfArgNull would result in stackoverflow
        if (name == null) {
            throw new NullArgumentException("name");
        }

        if (obj == null) {
            throw new NullArgumentException(name);
        }

        return obj;
    }
}
