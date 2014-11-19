package ch.epfl.sweng.bohdomp.dialogue.utils;

import ch.epfl.sweng.bohdomp.dialogue.BuildConfig;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;

/**
 * Contains helpers methods for various checks
 */
public class Contract {
    public static <S> S throwIfNull(S obj, String name) {
        if (obj == null) {
            throw new NullArgumentException(name);
        }

        return obj;
    }


    public static <S> S assertNotNull(S obj, String name) {
        if (BuildConfig.DEBUG && obj == null) {
            throw new AssertionError(name + " can not be null");
        }

        return obj;
    }
}
