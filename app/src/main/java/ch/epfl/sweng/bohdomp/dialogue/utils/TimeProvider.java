package ch.epfl.sweng.bohdomp.dialogue.utils;

/**
 * Interface describing a time provider.
 */
public interface TimeProvider {

    /**
     * @return current time in milli seconds
     */
    long currentTimeMillis();
}
