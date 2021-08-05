package com.CSCB07G3.medicalappointmenttracker.Model;

import java.util.Date;

public class Patient extends User {
    String medinfo;
    Date birthday;

    @Override
    public String toString(){
        return "Patient{"+ "id='"+userId+"', name='"+name+"', password='"+passWord+"',gender='"+gender+"', date of birth="+birthday+", medinfo='"+medinfo+'\''+'}';
    }

    public Patient(){
        super();
        this.medinfo = "";
        this.birthday = new Date();
    }
    public Patient(String name, String userid, String password, String gender, String medinfo, Date birthday){
        super(name, userid, password, gender);
        this.medinfo = medinfo;
        this.birthday = birthday;
    }

    public Patient signUp() {
        return new Patient();
    }

    public void selectAppointment(Appointment appointment){
        appointment.setUser(this);
        //Update database
    }

    public void cancelAppointment(Appointment appointment){
        appointment.setUser(null);
        //Update database
    }

    public void setMedInfo(String medInfo){
        this.medinfo = medInfo;
    }
    public String getMedInfo(){ return medinfo; }

    public void setBirthday(Date birthday){
        this.birthday = birthday;
    }
    public Date getBirthday(){ return birthday; }
}