/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package contraollersTest;

import controller.TreatmentController;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
/**
 *
 * @author nadis
 */
public class TreatmentControllerTest {
   private TreatmentController controller = new TreatmentController();

    @Test
    public void testInvalidAppointmentId() {
        String result = controller.saveTreatment(0, 1, 1, "Scaling", "12", "Mild plaque", 12, "Normal", "", new ArrayList<>(), "", "");
        assertEquals("Appointment ID is required", result);
    }

    @Test
    public void testInvalidDentistId() {
        String result = controller.saveTreatment(1, 1, 0, "Scaling", "12", "Mild plaque", 12, "Normal", "", new ArrayList<>(), "", "");
        assertEquals("Dentist ID is required", result);
    }

    @Test
    public void testEmptyTreatmentName() {
        String result = controller.saveTreatment(1, 1, 1, "", "12", "Mild plaque", 12, "Normal", "", new ArrayList<>(), "", "");
        assertEquals("Treatment name is required", result);
    }

    @Test
    public void testEmptyClinicalNotes() {
        String result = controller.saveTreatment(1, 1, 1, "Scaling", "12", "", 12, "Normal", "", new ArrayList<>(), "", "");
        assertEquals("Clinical notes are required", result);
    } 
}
