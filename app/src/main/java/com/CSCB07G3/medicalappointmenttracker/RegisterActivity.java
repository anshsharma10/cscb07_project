package com.CSCB07G3.medicalappointmenttracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.CSCB07G3.medicalappointmenttracker.Model.Doctor;
import com.CSCB07G3.medicalappointmenttracker.Model.Patient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {
    EditText edt_name, edt_userid, edt_password, edt_medinfo;
    Button registerButton;
    TextView logInRedirect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edt_name = findViewById(R.id.editName);
        edt_userid = findViewById(R.id.usrname);
        edt_password = findViewById(R.id.passwd);
        edt_medinfo = findViewById(R.id.medInfo);
        TextView medinfotxt = findViewById(R.id.textView3);
        registerButton = findViewById(R.id.registerbtn);
        logInRedirect = findViewById(R.id.logInRedirect);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        medinfotxt.setVisibility(View.GONE);
        edt_medinfo.setVisibility(View.GONE);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.radioButtonDoctor){
                    medinfotxt.setVisibility(View.GONE);
                    edt_medinfo.setVisibility(View.GONE);
                }
                else{
                    medinfotxt.setVisibility(View.VISIBLE);
                    edt_medinfo.setVisibility(View.VISIBLE);
                }
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://medical-appointment-trac-30878-default-rtdb.firebaseio.com/").getReference();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edt_name.getText().toString();
                String password = edt_password.getText().toString().trim();
                String userid = edt_userid.getText().toString().trim();
                String medinfo = edt_medinfo.getText().toString();
                boolean validregister = true;
                if (TextUtils.isEmpty(name)) {
                    edt_name.setError("Name is required");
                    validregister = false;
                }
                if (TextUtils.isEmpty(userid)) {
                    edt_userid.setError("User ID is required");
                    validregister = false;
                }
                if (TextUtils.isEmpty(password)) {
                    edt_password.setError("Password is required");
                    validregister = false;
                }
                if (password.length() < 6) {
                    edt_password.setError("Password must be at least 6 characters");
                    validregister = false;
                }
                if (radioGroup.getCheckedRadioButtonId() == -1){
                    Toast.makeText(RegisterActivity.this, "Role must be selected", Toast.LENGTH_SHORT).show();
                    validregister = false;
                }

                if (validregister){
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // check if userid is not registered before
                            if (snapshot.child("Patients").hasChild(userid) || snapshot.child("Doctors").hasChild(userid)) {
                                Toast.makeText(RegisterActivity.this, "User ID already exists", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                if(radioGroup.getCheckedRadioButtonId() == R.id.radioButtonPatient) {
                                    Patient patient = new Patient(name,password,userid,medinfo);
                                    databaseReference.child("Patients").child(userid).setValue(patient);
                                }
                                else if(radioGroup.getCheckedRadioButtonId() == R.id.radioButtonDoctor){
                                    Doctor doctor = new Doctor(name, userid, password);
                                    databaseReference.child("Doctors").child(userid).setValue(doctor);
                                }
                                Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                            }
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        logInRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}