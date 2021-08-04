package com.CSCB07G3.medicalappointmenttracker.Model;

public class Patient extends User {
    String medinfo;

    @Override
    public String toString(){
        return "Patient{"+ "name='"+name+"', password='"+passWord+"', medinfo='"+medinfo+'\''+'}';
    }

    public Patient(){
        super();
        this.medinfo = "";
    }
    public Patient(String name, String userid,String password, String medinfo){
        super(name, userid, password);
        this.medinfo = medinfo;
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
}