/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import DAO.TreatmentDAO;
import java.util.List;

/**
 *
 * @author nadis
 */
public class TreatmentController {
    private TreatmentDAO treatmentDAO;

    public TreatmentController() {
        this.treatmentDAO = new TreatmentDAO();
    }

    public String saveTreatment(
            int appointmentId,
            int patientUserId,
            int dentistUserId,
            String treatmentName,
            String toothNumberStr,
            String clinicalNotes,
            int toothNoInt,
            String toothStatus,
            String toothNotes,
            List<Object[]> prescriptionsList,
            String xrayType,
            String xrayFilePath
    ) {
        if (appointmentId <= 0) {
            return "Appointment ID is required";
        }
        if (dentistUserId <= 0) {
            return "Dentist ID is required";
        }
        if (treatmentName == null || treatmentName.trim().isEmpty()) {
            return "Treatment name is required";
        }
        if (clinicalNotes == null || clinicalNotes.trim().isEmpty()) {
            return "Clinical notes are required";
        }

        boolean isSaved = treatmentDAO.saveFullTreatmentRecord(
                appointmentId,
                patientUserId,
                dentistUserId,
                treatmentName,
                toothNumberStr,
                clinicalNotes,
                toothNoInt,
                toothStatus,
                toothNotes,
                prescriptionsList,
                xrayType,
                xrayFilePath
        );

        if (isSaved) {
            return "TREATMENT_SUCCESS";
        } else {
            return "Failed to save treatment record";
        }
    }
}
