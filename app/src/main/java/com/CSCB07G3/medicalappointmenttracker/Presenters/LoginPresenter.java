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
            model.checkLogin(username, password, new LoginModel.OnLoginListener() {
                @Override
                public void loginSuccess(User user) {
                    view.showMessage("Login Success");
                    if(user.Type().equals("Doctors")){
                        view.redirectToDoctor(username);
                    }else{
                        view.redirectToPatient(username);
                    }
                }

                @Override
                public void loginFailed(String s) {
                    view.showMessage(s);
                }
            });
        }
    }
}
