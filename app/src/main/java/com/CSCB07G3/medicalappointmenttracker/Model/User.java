package com.CSCB07G3.medicalappointmenttracker.Model;

import java.io.Serializable;

public abstract class User implements Serializable {
    public String name;
    public String userId;
    public String passWord;

    public User(){

    }

    public User(String name, String userId, String passWord){
        this.name = name;
        this.userId = userId;
        this.passWord = passWord;
    }

    public void getAppointment(){

    }

    public abstract User signUp();

    public String getName(){ return name; }
    public String getPassWord(){ return passWord; }
}
