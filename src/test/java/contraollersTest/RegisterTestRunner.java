/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package contraollersTest;

import controller.RegisterController;

/**
 *
 * @author nadis
 */
public class RegisterTestRunner {

    static int totalTests = 0;
    static int passedTests = 0;

    public static void main(String[] args) {

        RegisterController controller = new RegisterController();

        System.out.println("======================================");
        System.out.println("     AUTOMATED REGISTER TESTING       ");
        System.out.println("======================================");

        runTest("Empty Username Check", controller.registerUser("", "user@gmail.com", "Pass123", "Pass123", "PATIENT", null), "Username is required");
        runTest("Invalid Email Format Check", controller.registerUser("John", "john.email.com", "Pass123", "Pass123", "PATIENT", null), "Invalid email format");
        runTest("Password Mismatch Check", controller.registerUser("John", "john@gmail.com", "Pass123", "Mismatch321", "PATIENT", null), "Passwords do not match");
        runTest("Weak Password Check", controller.registerUser("John", "john@gmail.com", "123", "123", "PATIENT", null), "Password must contain at least 6 characters");
        runTest("Missing Staff Contact Check", controller.registerUser("Dr. Perera", "perera@sunrisedental.com", "Pass123", "Pass123", "DENTIST", ""), "Contact number is required for staff roles");
        runTest("Duplicate Email Registration", controller.registerUser("Sam Copy", "sepiolTest@email.com", "Pass123", "Pass123", "PATIENT", null), "Email already registered");

        String uniquePatientEmail = "patient_" + System.currentTimeMillis() + "@gmail.com";
        runTest("Valid Patient Flow", controller.registerUser("New Patient", uniquePatientEmail, "Pass123", "Pass123", "PATIENT", null), "REGISTRATION_SUCCESS");

        String uniqueDentistEmail = "dentist_" + System.currentTimeMillis() + "@gmail.com";
        runTest("Valid Dentist Flow", controller.registerUser("Dr. Silva", uniqueDentistEmail, "Pass123", "Pass123", "DENTIST", "0771234567"), "REGISTRATION_SUCCESS");

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
