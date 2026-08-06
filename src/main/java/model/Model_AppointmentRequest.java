/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;

/**
 *
 * @author nadis
 */
public class Model_AppointmentRequest {
    private int requestId;
    private String patientCustomId;
    private String patientName;
    private String patientEmail;
    private Date preferredDate;
    private String preferredTimeSlot;
    private String dentistName;
    private String notes;
    private String status;

    public Model_AppointmentRequest(int requestId, String patientCustomId, String patientName, String patientEmail, Date preferredDate, String preferredTimeSlot, String dentistName, String notes, String status) {
        this.requestId = requestId;
        this.patientCustomId = patientCustomId;
        this.patientName = patientName;
        this.patientEmail = patientEmail;
        this.preferredDate = preferredDate;
        this.preferredTimeSlot = preferredTimeSlot;
        this.dentistName = dentistName;
        this.notes = notes;
        this.status = status;
    }
    public Model_AppointmentRequest() {
    }

    public int getRequestId() { return requestId; }
    public void setRequestId(int requestId) { this.requestId = requestId; }
    
    public String getPatientCustomId() { return patientCustomId; }
    public void setPatientCustomId(String patientCustomId) { this.patientCustomId = patientCustomId; }
    
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    
    public String getPatientEmail() { return patientEmail; }
    public void setPatientEmail(String patientEmail) { this.patientEmail = patientEmail; }
    
    public Date getPreferredDate() { return preferredDate; }
    public void setPreferredDate(Date preferredDate) { this.preferredDate = preferredDate; }
    
    public String getPreferredTimeSlot() { return preferredTimeSlot; }
    public void setPreferredTimeSlot(String preferredTimeSlot) { this.preferredTimeSlot = preferredTimeSlot; }
    
    public String getDentistName() { return dentistName; }
    public void setDentistName(String dentistName) { this.dentistName = dentistName; }
   
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
