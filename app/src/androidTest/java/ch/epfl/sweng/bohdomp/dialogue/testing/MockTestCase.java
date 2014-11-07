package ch.epfl.sweng.bohdomp.dialogue.testing;


import android.test.InstrumentationTestCase;

/** A TestCase with support for Mockito. This class is copied from SwEng midterm exam testing suite. */
public class MockTestCase extends InstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // XXX: Hack required to make Mockito work on Android
        System.setProperty("dexmaker.dexcache",
                getInstrumentation().getTargetContext().getCacheDir().getPath());
    }

}
