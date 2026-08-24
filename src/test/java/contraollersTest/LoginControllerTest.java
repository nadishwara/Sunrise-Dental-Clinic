/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package contraollersTest;

import controller.LoginController;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author nadis
 */
public class LoginControllerTest {
    private LoginController controller = new LoginController();

    @Test
    public void testEmptyUsername() {
        String result = controller.login("", "password123");
        assertEquals("Username is required", result);
    }

    @Test
    public void testEmptyPassword() {
        String result = controller.login("sepiolTest@email.com", "");
        assertEquals("Password is required", result);
    }

    @Test
    public void testShortUsername() {
        String result = controller.login("ab", "password123");
        assertEquals("Username must contain at least 3 characters", result);
    }

    @Test
    public void testShortPassword() {
        String result = controller.login("sepiolTest@email.com", "123");
        assertEquals("Password must contain at least 5 characters", result);
    }

    @Test
    public void testNonExistingUser() {
        String result = controller.login("fakeuser@email.com", "default123");
        assertEquals("INVALID_CREDENTIALS", result);
        
    }

    @Test
    public void testInvalidPassword() {
        String result = controller.login("sepiolTest@email.com", "wrongpassword");
        assertEquals("INVALID_CREDENTIALS", result);
    }

    // Valid Test using DB Row 1 (sepiolTest@email.com / password123)
    @Test
    public void testValidPatientLogin() {
        String result = controller.login("sepiolTest@email.com", "password123");
        assertEquals("LOGIN_SUCCESS", result);
    }

    // Valid Test using DB Row 27 (nimal@patient.com / default123)
    @Test
    public void testAnotherValidPatientLogin() {
        String result = controller.login("nimal@patient.com", "default123");
        assertEquals("LOGIN_SUCCESS", result);
    }
}
