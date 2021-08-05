package com.CSCB07G3.medicalappointmenttracker.Model;

import java.io.Serializable;

public class Appointment implements Serializable {
	AppTime start_time;
	AppTime end_time;
	String doctorId;
	String patientId = "";
	
	public Appointment(String doctorid, AppTime start_time, AppTime end_time) {
		this.doctorId = doctorid;
		this.start_time = start_time;
		this.end_time = end_time;
	}

	public Appointment(String doctorid, String patientId, AppTime start_time, AppTime end_time) {
		this.doctorId = doctorid;
		this.patientId = patientId;
		this.start_time = start_time;
		this.end_time = end_time;
	}

    public void setUser(Patient patient) {
    }

	public AppTime getStartTime(){ return this.start_time; }
	public String getPatientId(){ return patientId; }
	public String getDoctorId(){ return doctorId; }
	public AppTime getEndTime(){ return this.end_time; }
}

