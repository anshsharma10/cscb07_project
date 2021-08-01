package com.CSCB07G3.medicalappointmenttracker;

public class Doctor extends User{

    @Override
    public String toString(){
          return "Doctor{"+ "id="+userId+", name='"+name+"', password='"+passWord+'\''+'}';
    }

    public Doctor(){
        super();
    }
    public Doctor(String name, String id, String password){
        super(name, id, password);
    }

    public Doctor signUp(){
        return new Doctor();
    }
}
