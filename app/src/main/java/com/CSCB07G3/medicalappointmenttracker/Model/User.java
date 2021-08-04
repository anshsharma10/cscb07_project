package com.CSCB07G3.medicalappointmenttracker.Model;

import java.io.Serializable;

public abstract class User implements Serializable {
    public String name;
    public String userId;
    public String passWord;
    public String gender;

    public User(){

    }

    public User(String name, String userId, String passWord,String gender){
        this.name = name;
        this.userId = userId;
        this.passWord = passWord;
        this.gender = gender;
    }

    public void getAppointment(){

    }

    public abstract User signUp();

    public String getName(){ return name; }
    public String getPassWord(){ return passWord; }
    public String getUserId(){ return userId; }
    public String getGender(){ return gender; }
}