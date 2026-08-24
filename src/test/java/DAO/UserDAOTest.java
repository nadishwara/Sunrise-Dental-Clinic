/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author nadis
 */
public class UserDAOTest {

    private UserDAO userDAO;

    @Before
    public void setUp() {
        userDAO = new UserDAO();
    }

    @Test
    public void testIsUsernameExistsWithNullShouldReturnFalse() {
        boolean result = userDAO.isUsernameExists(null);
        assertFalse("Null username should return false", result);
    }

    @Test
    public void testIsEmailExistsWithNullShouldReturnFalse() {
        boolean result = userDAO.isEmailExists(null);
        assertFalse("Null email should return false", result);
    }

    @Test
    public void testRegisterUserWithNullUserShouldReturnFalse() {
        boolean result = userDAO.registerUser(null);
        assertFalse("Registering null user should return false", result);
    }
}