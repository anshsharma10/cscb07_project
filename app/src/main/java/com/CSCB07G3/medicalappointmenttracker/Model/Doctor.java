package com.CSCB07G3.medicalappointmenttracker.Model;

public class Doctor extends User{
    String specialization;

    @Override
    public String toString(){
        return "Doctor{"+ "id='"+userId+"', name='"+name+"', password='"+passWord+"',gender='"+gender+"', specialization='"+specialization+'\''+'}';
    }

    public Doctor(){
        super();
        this.specialization = "";
    }
    public Doctor(String name, String userid, String password){
        super(name, userid, password);
    }

    @Override
    public User signUp() {
        return this;
    }

    public void setSpecialization(String specialization){
        this.specialization = specialization;
    }
    public String getSpecialization(){ return specialization; }
}