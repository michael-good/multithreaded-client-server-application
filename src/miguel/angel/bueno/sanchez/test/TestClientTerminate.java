package miguel.angel.bueno.sanchez.test;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class TestClientTerminate extends TestCase {

    public TestClientTerminate(String test) {
        super(test);
    }

    @Test
    public void testTerminateWordShouldBeTerminate() {

        FakeClientTerminate fakeClientTerminate = new FakeClientTerminate();
        final String expectedMessage = "terminate";
        String actualMessage;

        actualMessage = fakeClientTerminate.sendTerminateToServer();

        for (int i = 0; i < expectedMessage.length(); i++) {
            Assert.assertEquals(expectedMessage.charAt(i), actualMessage.charAt(i));
        }
    }

    @Test
    public void testHasNewLineAtEndOfMessage() {

        FakeClientTerminate fakeClientTerminate = new FakeClientTerminate();
        final String expectedMessage = System.lineSeparator();
        String actualMessage;

        actualMessage = fakeClientTerminate.sendTerminateToServer();

        Assert.assertEquals(expectedMessage.charAt(expectedMessage.length() - 1),
                actualMessage.charAt(actualMessage.length() - 1));
    }
}
