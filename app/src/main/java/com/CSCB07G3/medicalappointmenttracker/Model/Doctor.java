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
    public Doctor(String name, String userid, String password,String gender,String specialization){
        super(name, userid,password,gender);
        this.specialization=specialization;
    }

    public void setSpecialization(String specialization){
        this.specialization = specialization;
    }
    public String getSpecialization(){ return specialization; }
    public String Type(){
        return "Doctors";
    }
    public boolean checkNull(){
        if(this == null){
            return true;
        }else if(specialization!= null && userId!=null&&name!=null&&passWord!=null&&gender!=null){
            return false;
        }
        return true;
    }

}