package com.CSCB07G3.medicalappointmenttracker.Presenters;

import com.CSCB07G3.medicalappointmenttracker.LoginActivity;
import com.CSCB07G3.medicalappointmenttracker.Model.LoginModel;
import com.CSCB07G3.medicalappointmenttracker.Model.User;

public class LoginPresenter {
    private LoginModel model;
    private LoginActivity view;
    public LoginPresenter(LoginModel model,LoginActivity view){
        this.model =model;
        this.view =view;
    }

    public void Login(){
        String username = view.getUsername();
        String password = view.getPassWord();
        if(username.equals("")){
            view.setUsernameError("Username cannot be empty");
        }else if(password.equals("")){
            view.setPasswordError("Password cannot be empty");
        }else {
            User user = model.foundUser(username);
            if(user == null){
                view.showMessage("User not found");
            }else if(!user.getPassWord().equals(password)){
                view.showMessage("Wrong password");
            }else if(user.Type().equals("Doctors")){
                view.showMessage("Login Success!");
                view.redirectToDoctor(username);
            }else {
                view.showMessage("Login Success!");
                view.redirectToPatient(username);
            }
        }
    }
}
