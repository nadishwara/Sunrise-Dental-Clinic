/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package contraollersTest;

import controller.RegisterController;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


/**
 *
 * @author nadis
 */
public class RegisterControllerTest {
    private RegisterController controller = new RegisterController();

    @Test
    public void testEmptyUsername() {
        String result = controller.registerUser("", "user@gmail.com", "Pass123", "Pass123", "PATIENT", null);
        assertEquals("Username is required", result);
    }

    @Test
    public void testInvalidEmailFormat() {
        String result = controller.registerUser("John", "john.email.com", "Pass123", "Pass123", "PATIENT", null);
        assertEquals("Invalid email format", result);
    }

    @Test
    public void testPasswordMismatch() {
        String result = controller.registerUser("John", "john@gmail.com", "Pass123", "Mismatch321", "PATIENT", null);
        assertEquals("Passwords do not match", result);
    }

    @Test
    public void testWeakPassword() {
        String result = controller.registerUser("John", "john@gmail.com", "123", "123", "PATIENT", null);
        assertEquals("Password must contain at least 6 characters", result);
    }

    @Test
    public void testMissingStaffContactNo() {
        String result = controller.registerUser("Dr. Perera", "perera@sunrisedental.com", "Pass123", "Pass123", "DENTIST", "");
        assertEquals("Contact number is required for staff roles", result);
    }

    @Test
    public void testDuplicateEmail() {
        String result = controller.registerUser("Sam Copy", "sepiolTest@email.com", "Pass123", "Pass123", "PATIENT", null);
        assertEquals("Email already registered", result);
    }

    @Test
    public void testValidPatientRegistration() {
        String uniqueEmail = "patient_" + System.currentTimeMillis() + "@gmail.com";
        String result = controller.registerUser("New Patient", uniqueEmail, "Pass123", "Pass123", "PATIENT", null);
        assertEquals("REGISTRATION_SUCCESS", result);
    }

    @Test
    public void testValidDentistRegistration() {
        String uniqueEmail = "dentist_" + System.currentTimeMillis() + "@gmail.com";
        String result = controller.registerUser("Dr. Silva", uniqueEmail, "Pass123", "Pass123", "DENTIST", "0771234567");
        assertEquals("REGISTRATION_SUCCESS", result);
    }
}
