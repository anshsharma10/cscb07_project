package com.CSCB07G3.medicalappointmenttracker;

import java.io.Serializable;

public abstract class User implements Serializable {
    public String name;
    public String passWord;

    public User(){

    }

    public User(String name, String passWord){
        this.name = name;
        this.passWord = passWord;
    }

    public void getAppointment(){

    }

    public String getName(){ return name; }
    public String getPassWord(){ return passWord; }
}
