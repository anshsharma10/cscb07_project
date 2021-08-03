package com.CSCB07G3.medicalappointmenttracker.Model;

public class Doctor extends User {
    String name;
    String id;
    String password;

    @Override
    public String toString(){
          return "Doctor{"+ "id="+", name='"+name+"', password='"+passWord+'\''+'}';
    }

    public Doctor(){
        super();
    }

    public Doctor signUp() {
        return new Doctor();
    }

    public Doctor(String name, String id, String password){
        super(name, id, password);
    }

}
