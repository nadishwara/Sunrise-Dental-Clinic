/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import DAO.BillingDAO;

/**
 *
 * @author nadis
 */
public class BillingController {
    private BillingDAO billingDAO = new DAO.BillingDAO();

    public String generateBill(int appointmentId, int patientUserId, int receptionistUserId,
                                double consultationFee, double otherCharges, double discount) {
        if (appointmentId <= 0) {
            return "Invalid appointment ID";
        }
        if (receptionistUserId <= 0) {
            return "Invalid receptionist ID";
        }
        if (consultationFee < 0) {
            return "Consultation fee cannot be negative";
        }
        if (otherCharges < 0) {
            return "Other charges cannot be negative";
        }
        if (discount < 0) {
            return "Discount cannot be negative";
        }

        boolean success = billingDAO.processAndGenerateBill(appointmentId, patientUserId, receptionistUserId, consultationFee, otherCharges, discount);
        if (success) {
            return "BILL_GENERATION_SUCCESS";
        } else {
            return "Bill generation failed in database";
        }
    }

    public String updateBill(int appointmentId, int receptionistUserId, double consultationFee, double otherCharges, double discount) {
        if (appointmentId <= 0) {
            return "Invalid appointment ID";
        }
        if (receptionistUserId <= 0) {
            return "Invalid receptionist ID";
        }
        if (consultationFee < 0) {
            return "Consultation fee cannot be negative";
        }
        if (otherCharges < 0) {
            return "Other charges cannot be negative";
        }
        if (discount < 0) {
            return "Discount cannot be negative";
        }

        boolean success = billingDAO.updateBill(appointmentId, receptionistUserId, consultationFee, otherCharges, discount);
        if (success) {
            return "BILL_UPDATE_SUCCESS";
        } else {
            return "Bill update failed in database";
        }
    }
}
