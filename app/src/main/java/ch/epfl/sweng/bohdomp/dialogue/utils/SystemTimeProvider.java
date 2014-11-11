package ch.epfl.sweng.bohdomp.dialogue.utils;

/**
 * Created by BohDomp! on 11.11.14.
 */
public class SystemTimeProvider implements TimeProvider {

    @Override
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
