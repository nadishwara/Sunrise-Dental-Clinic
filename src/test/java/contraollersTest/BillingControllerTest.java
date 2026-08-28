/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package contraollersTest;

import controller.BillingController;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author nadis
 */
public class BillingControllerTest {
    private BillingController controller = new BillingController();

    @Test
    public void testInvalidAppointmentId() {
        String result = controller.generateBill(0, 1, 1, 1500.0, 0.0, 0.0);
        assertEquals("Invalid appointment ID", result);
    }

    @Test
    public void testInvalidReceptionistId() {
        String result = controller.generateBill(1, 1, 0, 1500.0, 0.0, 0.0);
        assertEquals("Invalid receptionist ID", result);
    }

    @Test
    public void testNegativeConsultationFee() {
        String result = controller.generateBill(1, 1, 1, -100.0, 0.0, 0.0);
        assertEquals("Consultation fee cannot be negative", result);
    }

    @Test
    public void testNegativeOtherCharges() {
        String result = controller.generateBill(1, 1, 1, 1500.0, -50.0, 0.0);
        assertEquals("Other charges cannot be negative", result);
    }

    @Test
    public void testNegativeDiscount() {
        String result = controller.generateBill(1, 1, 1, 1500.0, 0.0, -10.0);
        assertEquals("Discount cannot be negative", result);
    }

    @Test
    public void testInvalidUpdateAppointmentId() {
        String result = controller.updateBill(0, 1, 1500.0, 0.0, 0.0);
        assertEquals("Invalid appointment ID", result);
    }
}
