package com.CSCB07G3.medicalappointmenttracker.Model;

import com.CSCB07G3.medicalappointmenttracker.Model.Doctor;
import com.CSCB07G3.medicalappointmenttracker.Model.User;

public class Appointment {
	
	String date;
	String time;
	Doctor doctor;
	User user;
	
	public Appointment(Doctor doctor, String date, String time) {
		this.doctor = doctor;
		this.time = time;
		this.date = date;
	}

    public void setUser(Patient patient) {
    }
}

