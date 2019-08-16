package miguel.angel.bueno.sanchez.test;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class TestClientNumbers extends TestCase {

    public TestClientNumbers(String test) {
        super(test);
    }

    @Test
    public void testGeneratedRandomNumberMustBeBetweenGivenRange() {

        FakeClientNumbers fakeClientNumbers = new FakeClientNumbers();

        for (int i = 0; i < 100; i++) {
            int actualNumber = fakeClientNumbers.createRandomNumber();

            Assert.assertTrue(0 <= actualNumber &&
                    actualNumber <= FakeClientNumbers.randomIntegerUpperBoundExclusive);
        }
    }

    @Test
    public void testCheckThatTheNumberHasNineDecimalPlaces() {

        FakeClientNumbers fakeClientNumbers = new FakeClientNumbers();

        int notFormattedNumber = fakeClientNumbers.createRandomNumber();
        String formattedNumber = fakeClientNumbers.addLeadingZeroesToRandomNumber(notFormattedNumber);

        Assert.assertTrue(formattedNumber.length() == 9);
        for (int i = 0; i < formattedNumber.length(); i++) {
            Assert.assertTrue(Character.getNumericValue(formattedNumber.charAt(i)) >= 0 &&
                    Character.getNumericValue(formattedNumber.charAt(i)) <= 9);
        }
    }

    @Test
    public void testHasNewLineAtEndOfMessage() {

        FakeClientNumbers fakeClientNumbers = new FakeClientNumbers();
        int notFormattedNumber = fakeClientNumbers.createRandomNumber();
        String formattedNumber = fakeClientNumbers.addLeadingZeroesToRandomNumber(notFormattedNumber);

        String formattedNumberAndNewLine =
                fakeClientNumbers.appendNewLineSequenceToFormattedRandomNumber(formattedNumber);

        System.out.println(formattedNumberAndNewLine.charAt(formattedNumberAndNewLine.length() - 1));
        Assert.assertEquals(formattedNumberAndNewLine.charAt(formattedNumberAndNewLine.length() - 1),
                System.lineSeparator().charAt(System.lineSeparator().length() - 1));
    }

}
