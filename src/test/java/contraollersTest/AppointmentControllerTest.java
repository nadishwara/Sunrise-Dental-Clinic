/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package contraollersTest;

import controller.AppointmentController;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author nadis
 */
public class AppointmentControllerTest {
    private AppointmentController controller = new AppointmentController();

    @Test
    public void testEmptyName() {
        String result = controller.bookAppointment(0, "", "test@email.com", "0771234567", "Negombo", 1, "General Care", "2026-08-30", "08:30 AM - 09:30 AM");
        assertEquals("Name is required", result);
    }

    @Test
    public void testEmptyContact() {
        String result = controller.bookAppointment(0, "John Doe", "test@email.com", "", "Negombo", 1, "General Care", "2026-08-30", "08:30 AM - 09:30 AM");
        assertEquals("Contact number is required", result);
    }

    @Test
    public void testInvalidEmailFormat() {
        String result = controller.bookAppointment(0, "John Doe", "john.email.com", "0771234567", "Negombo", 1, "General Care", "2026-08-30", "08:30 AM - 09:30 AM");
        assertEquals("Invalid email format", result);
    }

    @Test
    public void testInvalidDentist() {
        String result = controller.bookAppointment(0, "John Doe", "test@email.com", "0771234567", "Negombo", 0, "General Care", "2026-08-30", "08:30 AM - 09:30 AM");
        assertEquals("Dentist selection is required", result);
    }

    @Test
    public void testEmptyDate() {
        String result = controller.bookAppointment(0, "John Doe", "test@email.com", "0771234567", "Negombo", 1, "General Care", "", "08:30 AM - 09:30 AM");
        assertEquals("Date is required", result);
    }

    @Test
    public void testEmptyTimeSlot() {
        String result = controller.bookAppointment(0, "John Doe", "test@email.com", "0771234567", "Negombo", 1, "General Care", "2026-08-30", "");
        assertEquals("Time slot is required", result);
    }

    @Test
public void testValidBooking() {
    long timestamp = System.currentTimeMillis();
    String uniqueContact = "077" + (timestamp % 10000000);
    String uniqueDate = "2026-09-" + String.format("%02d", (timestamp % 25) + 1);
    String result = controller.bookAppointment(0, "Test Patient", "patient@email.com", uniqueContact, "Negombo", 1, "General Care", uniqueDate, "08:30 AM - 09:30 AM");
    assertEquals("BOOKING_SUCCESS", result);
}
}
