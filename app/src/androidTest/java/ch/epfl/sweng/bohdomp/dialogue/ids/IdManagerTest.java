package ch.epfl.sweng.bohdomp.dialogue.ids;

import ch.epfl.sweng.bohdomp.dialogue.testing.MockTestCase;

/**
 * Created by BohDomp! on 13.11.14.
 */
public class IdManagerTest extends MockTestCase {

    private IdManager mIdManager;

    public void setUp() {
    }

    public void testGetInstance() {
        mIdManager = IdManager.getInstance();
        IdManager secondIdManager = IdManager.getInstance();

        assertEquals(mIdManager, secondIdManager);
    }
}
