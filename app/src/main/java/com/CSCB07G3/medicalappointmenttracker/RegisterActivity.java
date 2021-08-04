package com.CSCB07G3.medicalappointmenttracker;

import static android.widget.Toast.makeText;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
    Spinner gender_spinner,spec_spinner;
    ArrayAdapter gender_spinner_adapter,spec_spinner_adapter;
    String gender,spec;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edt_name = findViewById(R.id.editName);
        edt_userid = findViewById(R.id.usrname);
        edt_password = findViewById(R.id.passwd);
        edt_medinfo = findViewById(R.id.medInfo);
        TextView medinfotxt = findViewById(R.id.medinfotxt);
        TextView specinfotxt = findViewById(R.id.specinfotxt);
        spec_spinner = findViewById(R.id.spn_user_spec);
        registerButton = findViewById(R.id.registerbtn);
        logInRedirect = findViewById(R.id.logInRedirect);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        gender_spinner = findViewById(R.id.spn_user_gender);
        gender_spinner_adapter = ArrayAdapter.createFromResource(this, R.array.genders, android.R.layout.simple_spinner_item);
        gender_spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender_spinner.setAdapter(gender_spinner_adapter);
        gender_spinner.setVisibility(View.VISIBLE);

        medinfotxt.setVisibility(View.GONE);
        edt_medinfo.setVisibility(View.GONE);
        specinfotxt.setVisibility(View.GONE);
        spec_spinner.setVisibility(View.GONE);
        spec_spinner_adapter = ArrayAdapter.createFromResource(this, R.array.specializations, android.R.layout.simple_spinner_item);
        spec_spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spec_spinner.setAdapter(spec_spinner_adapter);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.radioButtonDoctor){
                    medinfotxt.setVisibility(View.GONE);
                    edt_medinfo.setVisibility(View.GONE);
                    specinfotxt.setVisibility(View.VISIBLE);
                    spec_spinner.setVisibility(View.VISIBLE);
                }
                else{
                    medinfotxt.setVisibility(View.VISIBLE);
                    edt_medinfo.setVisibility(View.VISIBLE);
                    specinfotxt.setVisibility(View.GONE);
                    spec_spinner.setVisibility(View.GONE);
                }
            }
        });
        gender_spinner.setOnItemSelectedListener(new genderSelectedListener());
        spec_spinner.setOnItemSelectedListener(new specSelectedListener());

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
                    makeText(getBaseContext(), "Role must be selected", Toast.LENGTH_SHORT).show();
                    validregister = false;
                }

                if (validregister){
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // check if userid is not registered before
                            if (snapshot.child("Patients").hasChild(userid) || snapshot.child("Doctors").hasChild(userid)) {
                                makeText(RegisterActivity.this, "User ID already exists", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                if(radioGroup.getCheckedRadioButtonId() == R.id.radioButtonPatient) {
                                    Patient patient = new Patient(name,userid,password,gender,medinfo);
                                    databaseReference.child("Patients").child(userid).setValue(patient);
                                }
                                else if(radioGroup.getCheckedRadioButtonId() == R.id.radioButtonDoctor){
                                    Doctor doctor = new Doctor(name,userid,password,gender,spec);
                                    databaseReference.child("Doctors").child(userid).setValue(doctor);
                                }
                                makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
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

    private class genderSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            gender= gender_spinner_adapter.getItem(arg2).toString();
        }
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    private class specSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            spec= spec_spinner_adapter.getItem(arg2).toString();
        }
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

}