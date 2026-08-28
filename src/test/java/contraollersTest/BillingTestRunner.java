/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package contraollersTest;

import controller.BillingController;

/**
 *
 * @author nadis
 */
public class BillingTestRunner {
    static int totalTests = 0;
    static int passedTests = 0;

    public static void main(String[] args) {
        BillingController controller = new BillingController();

        System.out.println("======================================");
        System.out.println("       AUTOMATED BILLING TESTING      ");
        System.out.println("======================================");

        runTest("Invalid Appointment ID Check", controller.generateBill(0, 1, 1, 1500.0, 0.0, 0.0), "Invalid appointment ID");
        runTest("Invalid Receptionist ID Check", controller.generateBill(1, 1, 0, 1500.0, 0.0, 0.0), "Invalid receptionist ID");
        runTest("Negative Consultation Fee Check", controller.generateBill(1, 1, 1, -500.0, 0.0, 0.0), "Consultation fee cannot be negative");
        runTest("Negative Other Charges Check", controller.generateBill(1, 1, 1, 1500.0, -100.0, 0.0), "Other charges cannot be negative");
        runTest("Negative Discount Check", controller.generateBill(1, 1, 1, 1500.0, 0.0, -50.0), "Discount cannot be negative");
        
        runTest("Invalid Update Appointment ID Check", controller.updateBill(0, 1, 1500.0, 0.0, 0.0), "Invalid appointment ID");

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
