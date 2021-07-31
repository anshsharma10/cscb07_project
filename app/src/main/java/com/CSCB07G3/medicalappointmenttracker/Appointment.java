package com.CSCB07G3.medicalappointmenttracker;

public class Appointment {
	
	private String date;
	private String time;
	private Docter docter;
	private User user;
	
	public Appointment(Docter docter, String date, String time) {
		this.docter = docter;
		this.time = time;
		this.date = date;
	}
	
}
