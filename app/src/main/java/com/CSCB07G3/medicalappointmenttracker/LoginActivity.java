package com.CSCB07G3.medicalappointmenttracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.CSCB07G3.medicalappointmenttracker.Model.Doctor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    public static final String USERID = "userid";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText edt_userid = findViewById(R.id.UserName);
        EditText edt_passwd = findViewById(R.id.Password);
        Button logInBtn = findViewById(R.id.LoginButton);
        TextView registerRedirect = findViewById(R.id.RegisterRedirect);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://medical-appointment-trac-30878-default-rtdb.firebaseio.com/").getReference();

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userid = edt_userid.getText().toString();
                String passwd = edt_passwd.getText().toString();

                if (userid.isEmpty() || passwd.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please enter your user ID or password", Toast.LENGTH_SHORT).show();
                }
                else{
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // check if userid is not registered before
                            if (snapshot.child("Patients").hasChild(userid)) {
                                String getPassword = snapshot.child("Patients").child(userid).getValue(Doctor.class).getPassWord();

                                if (getPassword.equals(passwd)){
                                    Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, ChooseAppointmentActivity.class).putExtra(USERID,userid));
                                    finish();
                                }
                                else{
                                    edt_passwd.setError("Wrong password");
                                }
                            }
                            else if (snapshot.child("Doctors").hasChild(userid)) {
                                String getPassword = snapshot.child("Doctors").child(userid).getValue(Doctor.class).getPassWord();

                                if (getPassword.equals(passwd)){
                                    Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, DoctorTrackAppointmentActivity.class).putExtra(USERID,userid));
                                    finish();
                                }
                                else{
                                    edt_passwd.setError("Wrong password");
                                }
                            }
                            else {
                                edt_userid.setError("Such user does not exist");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
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
}