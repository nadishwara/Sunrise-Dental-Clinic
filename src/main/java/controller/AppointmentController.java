/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import DAO.AppointmentDAO;

/**
 *
 * @author nadis
 */
public class AppointmentController {
    
    private AppointmentDAO appointmentDAO;
    
    public AppointmentController() {
        this.appointmentDAO=new AppointmentDAO();
    }
    public String bookAppointment(int appointmentId, String name, String email, String contact,
                                  String address, int dentistId, String treatment,
                                  String formattedDate, String time) {
        if (name == null || name.trim().isEmpty()) {
            return "Name is required";
        }
        if (contact == null || contact.trim().isEmpty()) {
            return "Contact number is required";
        }
        if (email != null && !email.trim().isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return "Invalid email format";
        }
        if (dentistId <= 0) {
            return "Dentist selection is required";
        }
        if (formattedDate == null || formattedDate.trim().isEmpty()) {
            return "Date is required";
        }
        if (time == null || time.trim().isEmpty()) {
            return "Time slot is required";
        }

        boolean result = appointmentDAO.saveOrUpdateBooking(
                appointmentId, name, email, contact, address, dentistId, treatment, formattedDate, time
        );

        return result ? "BOOKING_SUCCESS" : "BOOKING_FAILED";    
    }
}
