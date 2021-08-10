package com.CSCB07G3.medicalappointmenttracker.Model;

import java.io.Serializable;
import java.util.Date;

public class Patient extends User implements Serializable {
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

    @Override
    public String Type() {
        return "Patients";
    }

    public boolean checkNull(){
        if(this == null){
            return true;
        }else if(birthday!=null&&medinfo!= null && userId!=null&&name!=null&&passWord!=null&&gender!=null){
            return false;
        }
        return true;
    }

    public Patient(String name, String userid, String password, String gender, String medinfo, Date birthday){
        super(name, userid, password, gender);
        this.medinfo = medinfo;
        this.birthday = birthday;
    }


    public void selectAppointment(Appointment appointment){
        appointment.setPatientId(userId);
        //Update database
    }

    public void cancelAppointment(Appointment appointment){
        appointment.setPatientId("");
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