/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author nadis
 */
public class MedicalReportModel {
    private int appointmentId;
    private String appointmentDate;
    private String appointmentTime;
    private String dentistName;
    private String treatmentType;
    private String treatmentName;
    private String clinicalNotes;
    private String remarks;
    private String toothNumber;
    private String affectedArea;
    private String xRayType;
    private String serviceCost;
    
//    public int getAppointmentId() { return appointmentId; }
//    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }

    public String getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(String appointmentDate) { this.appointmentDate = appointmentDate; }

    public String getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(String appointmentTime) { this.appointmentTime = appointmentTime; }

    public String getDentistName() { return dentistName; }
    public void setDentistName(String dentistName) { this.dentistName = dentistName; }

    public String getTreatmentType() { return treatmentType; }
    public void setTreatmentType(String treatmentType) { this.treatmentType = treatmentType; }

    public String getTreatmentName() { return treatmentName; }
    public void setTreatmentName(String treatmentName) { this.treatmentName = treatmentName; }

    public String getClinicalNotes() { return clinicalNotes; }
    public void setClinicalNotes(String clinicalNotes) { this.clinicalNotes = clinicalNotes; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public String getToothNumber() { return toothNumber; }
    public void setToothNumber(String toothNumber) { this.toothNumber = toothNumber; }

    public String getAffectedArea() { return affectedArea; }
    public void setAffectedArea(String affectedArea) { this.affectedArea = affectedArea; }

    public String getXRayType() { return xRayType; }
    public void setXRayType(String xRayType) { this.xRayType = xRayType; }

    public String getServiceCost() { return serviceCost; }
    public void setServiceCost(String serviceCost) { this.serviceCost = serviceCost; }

}
