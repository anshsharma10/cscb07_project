package com.CSCB07G3.medicalappointmenttracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.CSCB07G3.medicalappointmenttracker.Model.LoginModel;
import com.CSCB07G3.medicalappointmenttracker.Presenters.LoginPresenter;

public class LoginActivity extends AppCompatActivity {
    public static final String USERID = "userid";
    EditText edt_userid,edt_passwd;
    private LoginPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Login");

        Button logInBtn = findViewById(R.id.LoginButton);
        TextView registerRedirect = findViewById(R.id.registerRedirect);
        edt_userid = findViewById(R.id.UserName);
        edt_passwd = findViewById(R.id.Password);
        presenter = new LoginPresenter(new LoginModel(),this);
        logInBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                presenter.Login();
            }
        });
        registerRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            // open register activity
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    public void showMessage(String msg){
        Toast.makeText(LoginActivity.this,msg,Toast.LENGTH_SHORT).show();
    }

    public void setUsernameError(String msg){
        edt_userid.setError(msg);
    }

    public void setPasswordError(String msg){
        edt_passwd.setError(msg);
    }

    public String getPassWord(){
        return edt_passwd.getText().toString();
    }

    public String getUsername(){
        return edt_userid.getText().toString();
    }

    public void redirectToDoctor(String userId){
        startActivity(new Intent(LoginActivity.this,DoctorTrackAppointmentActivity.class).putExtra(USERID,userId));
        finish();
    }

    public void redirectToPatient(String userId){
        startActivity(new Intent(LoginActivity.this,ChooseAppointmentActivity.class).putExtra(USERID,userId));
        finish();
    }
}