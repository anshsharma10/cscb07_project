package com.CSCB07G3.medicalappointmenttracker;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.CSCB07G3.medicalappointmenttracker.Model.Appointment;
import com.CSCB07G3.medicalappointmenttracker.Model.Patient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DoctorViewPatientInfoActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Patient patient = (Patient) getIntent().getExtras().getSerializable("patient");
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Patients").child(patient.getUserId());

        setContentView(R.layout.activity_doctor_view_patient_info);
        TextView name = findViewById(R.id.patient_info_name);
        TextView gender = findViewById(R.id.patient_info_gender);
        TextView dob = findViewById(R.id.patient_info_date_of_birth);
        TextView medInfo = findViewById(R.id.patient_info_medinfo);


        TextView doctors = findViewById(R.id.patient_info_doctors);
        //TextView appointments = findViewById(R.id.patient_info_appointments);



        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        ValueEventListener patientListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    finish();
                }
                name.setText(dataSnapshot.child("name").getValue(String.class));
                gender.setText(dataSnapshot.child("gender").getValue(String.class));
                medInfo.setText(dataSnapshot.child("medInfo").getValue(String.class));
                dob.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date(dataSnapshot.child("birthday").child("time").getValue(Long.class))));


                ArrayList<String> doctorsList = new ArrayList<String>();
                for (DataSnapshot appointmentSnapshot : dataSnapshot.child("pastApps").getChildren()) {
                Appointment appointment = appointmentSnapshot.getValue(Appointment.class);
                    DatabaseReference doctorRef = FirebaseDatabase.getInstance().getReference("Doctors").child(appointment.getDoctorId());

                    ValueEventListener getDoctor = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot DoctorsSnapshot) {
                            if (!doctorsList.contains(DoctorsSnapshot.child("name").getValue(String.class) + "\n")) {
                                doctorsList.add(DoctorsSnapshot.child("name").getValue(String.class) + "\n");
                                if (doctorsList.size() != 0) {
                                    String doctorsListString = "";
                                    for (String doctorString : doctorsList) {
                                        doctorsListString += doctorString;
                                    }
                                    doctorsListString = doctorsListString.trim();
                                    doctors.setText(doctorsListString);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    };
                    doctorRef.addListenerForSingleValueEvent(getDoctor);
                }
                if (doctorsList.size() == 0) {
                    doctors.setText("No doctor history");
                }

                //appointments.setText("Requires past appointments implementation in database/patient class");
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
