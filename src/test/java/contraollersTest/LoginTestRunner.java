/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package contraollersTest;

import controller.LoginController;

/**
 *
 * @author nadis
 */
public class LoginTestRunner {

    static int totalTests = 0;
    static int passedTests = 0;

    public static void main(String[] args) {

        LoginController controller = new LoginController();

        System.out.println("======================================");
        System.out.println("       AUTOMATED LOGIN TESTING        ");
        System.out.println("======================================");

        runTest("Empty Email Check", controller.login("", "password123"), "Username is required");
        runTest("Empty Password Check", controller.login("sepiolTest@email.com", ""), "Password is required");
        runTest("Short Email Check", controller.login("ab", "password123"), "Username must contain at least 3 characters");
        runTest("Short Password Check", controller.login("sepiolTest@email.com", "123"), "Password must contain at least 5 characters");
        runTest("Non-Existing User Check", controller.login("fakeuser@email.com", "default123"), "INVALID_CREDENTIALS");
        runTest("Invalid Password Check", controller.login("sepiolTest@email.com", "wrongpassword"), "INVALID_CREDENTIALS");
        runTest("Valid Patient Login (Sam)", controller.login("sepiolTest@email.com", "password123"), "LOGIN_SUCCESS");
        runTest("Valid Patient Login (Nimal)", controller.login("nimal@patient.com", "default123"), "LOGIN_SUCCESS");

        System.out.println();
        System.out.println("======================================");
        System.out.println("              SUMMARY                 ");
        System.out.println("======================================");
        System.out.println("Total Tests  : " + totalTests);
        System.out.println("Passed Tests : " + passedTests);
        System.out.println("Failed Tests : " + (totalTests - passedTests));
        System.out.println("======================================");
    }

    public static void runTest(String testName, String actual, String expected) {
        totalTests++;

        System.out.println();
        System.out.println("Test Case: " + testName);
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
