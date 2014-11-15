package ch.epfl.sweng.bohdomp.dialogue.utils;

/**
 * Class defining a system time provider.
 */
public class SystemTimeProvider implements TimeProvider {
    @Override
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
