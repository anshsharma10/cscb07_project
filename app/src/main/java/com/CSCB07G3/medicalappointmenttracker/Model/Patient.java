package com.CSCB07G3.medicalappointmenttracker.Model;

import com.CSCB07G3.medicalappointmenttracker.Model.Appointment;
import com.CSCB07G3.medicalappointmenttracker.Model.User;

public class Patient extends User {
    String medinfo;

    @Override
    public String toString(){
        return "Patient{"+ "name='"+name+"', password='"+passWord+"', medinfo='"+medinfo+'\''+'}';
    }

    public Patient(){
        super();
        this.medinfo = null;
    }
    public Patient(String name, String password, String userid, String medinfo){
        super(name, password, userid);
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

    public void setMedinfo(String medInfo){
        this.medinfo = medInfo;
    }


    public String getMedInfo(){ return medinfo; }

}
