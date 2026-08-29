package contraollersTest;

import controller.TreatmentController;
import java.util.ArrayList;

public class TreatmentTestRunner {

    static int totalTests = 0;
    static int passedTests = 0;

    public static void main(String[] args) {
        TreatmentController controller = new TreatmentController();

        System.out.println("======================================");
        System.out.println("      AUTOMATED TREATMENT TESTING     ");
        System.out.println("======================================");
        runTest("Invalid Appointment ID Check", 
                controller.saveTreatment(0, 1, 1, "Scaling", "12", "Mild plaque", 12, "Normal", "", new ArrayList<>(), "", ""), 
                "Appointment ID is required");

        runTest("Invalid Dentist ID Check", 
                controller.saveTreatment(1, 1, 0, "Scaling", "12", "Mild plaque", 12, "Normal", "", new ArrayList<>(), "", ""), 
                "Dentist ID is required");

        runTest("Empty Treatment Name Check", 
                controller.saveTreatment(1, 1, 1, "", "12", "Mild plaque", 12, "Normal", "", new ArrayList<>(), "", ""), 
                "Treatment name is required");

        runTest("Empty Clinical Notes Check", 
                controller.saveTreatment(1, 1, 1, "Scaling", "12", "", 12, "Normal", "", new ArrayList<>(), "", ""), 
                "Clinical notes are required");

        runTest("Valid Treatment Save Flow", 
                controller.saveTreatment(1, 1, 1, "Scaling", "12", "Teeth cleaned properly", 12, "Normal", "Healthy", new ArrayList<>(), "", ""), 
                "TREATMENT_SUCCESS");

        System.out.println("\n======================================");
        System.out.println("              SUMMARY                 ");
        System.out.println("======================================");
        System.out.println("Total Tests  : " + totalTests);
        System.out.println("Passed Tests : " + passedTests);
        System.out.println("Failed Tests : " + (totalTests - passedTests));
        System.out.println("======================================");
    }

    public static void runTest(String testName, String actual, String expected) {
        totalTests++;
        System.out.println("\nTest Case: " + testName);
        System.out.println("Expected : " + expected);
        System.out.println("Actual   : " + actual);

        if (expected.equals(actual)) {
            passedTests++;
            System.out.println("Result   : PASS");
        } else {
            System.out.println("Result   : FAIL");
        }
    }
}