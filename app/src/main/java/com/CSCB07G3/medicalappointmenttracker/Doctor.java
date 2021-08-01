package com.CSCB07G3.medicalappointmenttracker;

public class Doctor extends User{
    // delte these fields after user class is completed
    String id;
    String name;
    String password;

    @Override
    public String toString(){
          return "Doctor{"+ "id="+id+", name='"+name+"', password='"+password+'\''+'}';
    }

    public Doctor(String name, String id, String password){
        super(name, id, password);
        this.name = name;
        this.id = id;
        this.password = password;
    }

    public Doctor signUp(){
        return new Doctor(this.name, this.id, this.password);
    }
}
