package com.CSCB07G3.medicalappointmenttracker;

public class Doctor extends User{
    @Override
    public String toString(){
          return "Doctor{"+ "id="+", name='"+name+"', password='"+passWord+'\''+'}';
    }

    public Doctor(){
        super();
    }
    public Doctor(String name, String password){
        super(name, password);
    }

}
