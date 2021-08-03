package com.CSCB07G3.medicalappointmenttracker;

public class Appointment {
	
	private String date;
	private String time;
	private Doctor doctor;
	private User user;
	
	public Appointment(Doctor doctor, String date, String time) {
		this.doctor = doctor;
		this.time = time;
		this.date = date;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}

