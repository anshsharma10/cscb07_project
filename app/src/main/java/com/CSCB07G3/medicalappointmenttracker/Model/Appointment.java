package com.CSCB07G3.medicalappointmenttracker.Model;

import java.io.Serializable;
import java.util.Date;


public class Appointment implements Serializable,Comparable<Appointment> {
	AppTime startTime;
	AppTime endTime;
	String doctorId;
	String patientId = "";
	String appointmentId;
	public Appointment(){
	}

	public Appointment(String appointmentId, String doctorid, AppTime start_time, AppTime end_time) {
		this.doctorId = doctorid;
		this.startTime = start_time;
		this.endTime = end_time;
		this.appointmentId = appointmentId;
	}

	public Appointment(String appointmentId, String doctorid, String patientId, AppTime start_time, AppTime end_time) {
		this.doctorId = doctorid;
		this.patientId = patientId;
		this.startTime = start_time;
		this.endTime = end_time;
		this.appointmentId = appointmentId;
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
		return endTime.convertToDate().before(new Date(System.currentTimeMillis()));
	}

    public void setPatientId(String patientId){ this.patientId = patientId; }
	public void setDoctorId(String doctorId){ this.doctorId = doctorId; }
    public void setStartTime(AppTime startTime){this.startTime = startTime; }
	public void setEndTime(AppTime endTime){ this.endTime = endTime; }
	public void setAppointmentId(String appointmentId){ this.appointmentId = appointmentId; }
	public AppTime getStartTime(){ return startTime; }
	public String getPatientId(){ return patientId; }
	public String getDoctorId(){ return doctorId; }
	public AppTime getEndTime(){ return this.endTime; }
	public String getAppointmentId(){ return this.appointmentId; }

	@Override
	public int compareTo(Appointment o) {
		if(startTime.compareTo(o.startTime)==0){
			return endTime.compareTo(o.endTime);
		}else{
			return startTime.compareTo(o.startTime);
		}
	}
}

