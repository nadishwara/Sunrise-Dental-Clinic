package contraollersTest;

import controller.AppointmentController;

public class AppointmentTestRunner {

    static int totalTests = 0;
    static int passedTests = 0;

    public static void main(String[] args) {

        AppointmentController controller = new AppointmentController();

        System.out.println("======================================");
        System.out.println("     AUTOMATED APPOINTMENT TESTING    ");
        System.out.println("======================================");

        runTest("Empty Name Check", controller.bookAppointment(0, "", "test@email.com", "0771234567", "Negombo", 1, "General Care", "2026-08-30", "08:30 AM - 09:30 AM"), "Name is required");
        runTest("Empty Contact Check", controller.bookAppointment(0, "John Doe", "test@email.com", "", "Negombo", 1, "General Care", "2026-08-30", "08:30 AM - 09:30 AM"), "Contact number is required");
        runTest("Invalid Email Format Check", controller.bookAppointment(0, "John Doe", "john.email.com", "0771234567", "Negombo", 1, "General Care", "2026-08-30", "08:30 AM - 09:30 AM"), "Invalid email format");
        runTest("Invalid Dentist Selection", controller.bookAppointment(0, "John Doe", "test@email.com", "0771234567", "Negombo", 0, "General Care", "2026-08-30", "08:30 AM - 09:30 AM"), "Dentist selection is required");
        runTest("Empty Date Check", controller.bookAppointment(0, "John Doe", "test@email.com", "0771234567", "Negombo", 1, "General Care", "", "08:30 AM - 09:30 AM"), "Date is required");
        runTest("Empty Time Slot Check", controller.bookAppointment(0, "John Doe", "test@email.com", "0771234567", "Negombo", 1, "General Care", "2026-08-30", ""), "Time slot is required");

        // Dynamic date & contact generation to avoid DB unique constraint conflicts
        long timestamp = System.currentTimeMillis();
        String uniqueContact = "077" + (timestamp % 10000000);
        String uniqueDate = "2026-09-" + String.format("%02d", (timestamp % 25) + 1);

        runTest("Valid Appointment Booking Flow", controller.bookAppointment(0, "Test Patient", "patient@email.com", uniqueContact, "Negombo", 1, "General Care", uniqueDate, "08:30 AM - 09:30 AM"), "BOOKING_SUCCESS");

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