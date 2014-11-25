package ch.epfl.sweng.bohdomp.dialogue.utils;

import ch.epfl.sweng.bohdomp.dialogue.BuildConfig;
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

    public static void assertTrue(Boolean condition, final String msg) {
        if (BuildConfig.DEBUG) {
            throwIfArgNull(msg, "message");

            if (!condition) {
                throw new AssertionError(msg);
            }
        }
    }

    public static void assertFalse(Boolean condition, final String msg) {
        if (BuildConfig.DEBUG) {
            throwIfArgNull(msg, "message");

            if (condition) {
                throw new AssertionError(msg);
            }
        }
    }

    public static <T> void assertNotNull(T obj, final String name) {
        if (BuildConfig.DEBUG) {
            throwIfArgNull(name, "name");

            if (obj == null) {
                throw new AssertionError("argument is null: " + name);
            }
        }
    }
}
