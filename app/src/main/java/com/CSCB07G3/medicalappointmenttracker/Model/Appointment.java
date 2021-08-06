package com.CSCB07G3.medicalappointmenttracker.Model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;


public class Appointment implements Serializable {
	AppTime startTime;
	AppTime endTime;
	String doctorId;
	String patientId = "";
	public Appointment(){
	}

	public Appointment(String doctorid, AppTime start_time, AppTime end_time) {
		this.doctorId = doctorid;
		this.startTime = start_time;
		this.endTime = end_time;
	}

	public Appointment(String doctorid, String patientId, AppTime start_time, AppTime end_time) {
		this.doctorId = doctorid;
		this.patientId = patientId;
		this.startTime = start_time;
		this.endTime = end_time;
	}

	@Override
	public String toString() {
		return "Appointment{" +
				"start_time=" + startTime.toString() +
				", end_time=" + endTime.toString() +
				", doctorId='" + doctorId + '\'' +
				", patientId='" + patientId + '\'' +
				'}';
	}

	public boolean isPast(){
		Date curr = new Date(System.currentTimeMillis());
		Calendar c = Calendar.getInstance();
		c.set(endTime.getYear(),endTime.getMonth(),endTime.getDay(),endTime.getHour(),endTime.getMinute());
		return c.getTime().before(curr);
	}

    public void setPatientId(String patientId){ this.patientId = patientId; }
    public void setStartTime(AppTime startTime){this.startTime = startTime; }
	public void setEndTime(AppTime endTime){ this.endTime = endTime; }
	public AppTime getStartTime(){ return startTime; }
	public String getPatientId(){ return patientId; }
	public String getDoctorId(){ return doctorId; }
	public AppTime getEndTime(){ return this.endTime; }
}

