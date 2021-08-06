package com.CSCB07G3.medicalappointmenttracker;

import static com.CSCB07G3.medicalappointmenttracker.Fragment.Fragment1.DOCTOR_SELECTED;
import static com.CSCB07G3.medicalappointmenttracker.Fragment.Fragment1.USERID;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.CSCB07G3.medicalappointmenttracker.Model.Appointment;
import com.CSCB07G3.medicalappointmenttracker.Model.Doctor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

public class PatientViewDoctorAvailabilityActivity extends AppCompatActivity {
    String doctorId,userId;
    Spinner date_spn;
    ArrayList<String> dateList;
    ArrayList<Appointment> avaibilityList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view_doctor_avaibility);
        doctorId = getIntent().getStringExtra(DOCTOR_SELECTED);
        userId = getIntent().getStringExtra(USERID);
        TextView title = findViewById(R.id.view_availability_title);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Doctors").child(doctorId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    title.setText("Availability of Doctor " + snapshot.getValue(Doctor.class).getName());
                }else{
                    Toast.makeText(PatientViewDoctorAvailabilityActivity.this,"Doctor removed",Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        Query query = mDatabase.child("Appointments").orderByChild("doctorId").equalTo(doctorId);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dateList = new ArrayList<String>();
                dateList.add("- -");
                avaibilityList = new ArrayList<Appointment>();
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    Appointment availability = child.getValue(Appointment.class);
                    if(Objects.equals(availability.getPatientId(), "")){
                        avaibilityList.add(availability);
                        Date d = new GregorianCalendar(availability.getStartTime().getYear(), availability.getStartTime().getMonth(), availability.getStartTime().getDay()).getTime();
                        String date = new SimpleDateFormat("dd/MM/yyyy").format(d);
                        if(! dateList.contains(date)){
                            dateList.add(date);
                        }
                    }
                }
                date_spn = (Spinner) findViewById(R.id.spn_appointment_date);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, dateList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                date_spn.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        query.addValueEventListener(valueEventListener);
    }

}