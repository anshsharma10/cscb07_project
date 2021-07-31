package com.CSCB07G3.medicalappointmenttracker;

import java.io.Serializable;

public abstract class User implements Serializable {
    public String name;
    public String userId;
    public String passWord;

    public User(String name, String userId, String passWord){
        this.name = name;
        this.userId = userId;
        this.passWord = passWord;
    }

    public Appointment getAppointment(){
        return new Appointment();
    }

    public abstract User signUp();
}
