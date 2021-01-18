package txttopdf;

import java.io.IOException;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * Creates a test PDF and runs the Test on it
 * @author Aden Haussmann
 */
public class TestRunner {

    public static void main(String[] args) throws IOException {

        /**
         * Create PDF
         */
        AppTest tester = new AppTest();
        tester.createTestPdf();

        /**
         * Run tests
         */
        Result result = JUnitCore.runClasses(AppTest.class);

        for (Failure failure : result.getFailures()) {
        System.out.println(failure.toString());
        }

        System.out.println(result.wasSuccessful());

    }
    
}
