/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nadis
 */
public class BillingDetails {
    private int appointmentId;
    private String customAppointmentId;
    private int patientUserId;
    private String patientCustomId;
    private String patientName;
    private String patientPhone;
    private String doctorName;
    private String appointmentDate;
    
    private List<String> treatments = new ArrayList<>();
    private List<String> xrays = new ArrayList<>();
    private List<String> prescriptions = new ArrayList<>();
    private String clinicalNotes;

    private double totalTreatmentCost;
    private double totalXrayCost;

    public BillingDetails() {}

    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }

    public String getCustomAppointmentId() { return customAppointmentId; }
    public void setCustomAppointmentId(String customAppointmentId) { this.customAppointmentId = customAppointmentId; }

    public int getPatientUserId() { return patientUserId; }
    public void setPatientUserId(int patientUserId) { this.patientUserId = patientUserId; }

    public String getPatientCustomId() { return patientCustomId; }
    public void setPatientCustomId(String patientCustomId) { this.patientCustomId = patientCustomId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getPatientPhone() { return patientPhone; }
    public void setPatientPhone(String patientPhone) { this.patientPhone = patientPhone; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public String getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(String appointmentDate) { this.appointmentDate = appointmentDate; }

    public List<String> getTreatments() { return treatments; }
    public void setTreatments(List<String> treatments) { this.treatments = treatments; }

    public List<String> getXrays() { return xrays; }
    public void setXrays(List<String> xrays) { this.xrays = xrays; }

    public List<String> getPrescriptions() { return prescriptions; }
    public void setPrescriptions(List<String> prescriptions) { this.prescriptions = prescriptions; }

    public String getClinicalNotes() { return clinicalNotes; }
    public void setClinicalNotes(String clinicalNotes) { this.clinicalNotes = clinicalNotes; }

    public double getTotalTreatmentCost() { return totalTreatmentCost; }
    public void setTotalTreatmentCost(double totalTreatmentCost) { this.totalTreatmentCost = totalTreatmentCost; }

    public double getTotalXrayCost() { return totalXrayCost; }
    public void setTotalXrayCost(double totalXrayCost) { this.totalXrayCost = totalXrayCost; }
}
