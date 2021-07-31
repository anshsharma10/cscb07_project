package com.CSCB07G3.medicalappointmenttracker;

public class Doctor extends User{
    // delte these fields after user class is completed
    int id;
    String name;
    String password;

    public Doctor(){

    }

    @Override
    public String toString(){
          return "Doctor{"+ "id="+id+", name='"+name+"', password='"+password+'\''+'}';
    }

    public Doctor(String name, int id, String password){
        this.name=name;
        this.id=id;
        this.password=password;
    }
}
