package com.CSCB07G3.medicalappointmenttracker;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.CSCB07G3.medicalappointmenttracker.Model.Patient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DoctorViewPatientInfoActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_doctor_view_patient_info);
        TextView name = findViewById(R.id.patient_info_name);
        TextView user = findViewById(R.id.patient_info_user);
        TextView gender = findViewById(R.id.patient_info_gender);
        TextView medInfo = findViewById(R.id.patient_info_medinfo);
        TextView doctors = findViewById(R.id.patient_info_doctors);
        TextView appointments = findViewById(R.id.patient_info_appointments);

        Patient patient = (Patient) getIntent().getExtras().getSerializable("patient");
        name.setText(patient.getName());
        user.setText(patient.getUserId());
        gender.setText(patient.getGender());
        medInfo.setText(patient.getMedInfo());
        doctors.setText("Requires past doctors implementation in database/patient class");
        appointments.setText("Requires past appointments implementation in database/patient class");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Patients").child(patient.getUserId());
        ValueEventListener patientListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("testtest", "onDataChange: ");
                name.setText(dataSnapshot.child("name").getValue(String.class));
                user.setText(dataSnapshot.child("userId").getValue(String.class));
                gender.setText(dataSnapshot.child("gender").getValue(String.class));
                medInfo.setText(dataSnapshot.child("medInfo").getValue(String.class));
                doctors.setText("Requires past doctors implementation in database/patient class");
                appointments.setText("Requires past appointments implementation in database/patient class");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mDatabase.addValueEventListener(patientListener);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
