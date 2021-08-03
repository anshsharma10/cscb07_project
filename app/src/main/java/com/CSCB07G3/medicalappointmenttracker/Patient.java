package com.CSCB07G3.medicalappointmenttracker;

public class Patient extends User{
    String medinfo;

    @Override
    public String toString(){
        return "Patient{"+ "name='"+name+"', password='"+passWord+"', medinfo='"+medinfo+'\''+'}';
    }

    public Patient(){
        super();
        this.medinfo = null;
    }
    public Patient(String name, String password, String medinfo){
        super(name, password);
        this.medinfo = medinfo;
    }

    public void selectAppointment(Appointment appointment){
        appointment.setUser(this);
        //Update database
    }

    public void cancelAppointment(){
        appointment.setUser(null);
        //Update database
    }

    public void setMedinfo(medInfo){
        this.medinfo = medInfo;
    }


    public String getMedInfo(){ return medinfo; }

}
