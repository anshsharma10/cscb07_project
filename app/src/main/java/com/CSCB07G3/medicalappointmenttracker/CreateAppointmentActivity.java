package com.CSCB07G3.medicalappointmenttracker;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class CreateAppointmentActivity extends AppCompatActivity {
    EditText edt_date,edt_start_time, edt_end_time;
    Button createapppointmentbtn;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://medical-appointment-trac-30878-default-rtdb.firebaseio.com/").getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_appointment);

        edt_date = findViewById(R.id.editDate);
        edt_start_time = findViewById(R.id.editTimeslot1);
        edt_end_time = findViewById(R.id.editTimeslot2);
        createapppointmentbtn = findViewById(R.id.createappointmentbtn);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);


        edt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        CreateAppointmentActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date;
                        if (month < 10 && day < 10){
                            date = "0" + day + "/" + "0" + month + "/" + year;
                        }
                        else if(month < 10 && day >= 10){
                            date = day + "/" + "0" + month + "/" + year;
                        }
                        else if(month >= 10 && day < 10){
                            date = "0" + day + "/" + month + "/" + year;
                        }
                        else{
                            date = day + "/" + month + "/" + year;
                        }
                        edt_date.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        edt_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        CreateAppointmentActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        String time;
                        if (minute < 10 && hour < 10){
                            time = "0" + hour + ":" + "0" + minute;
                        }
                        else if(minute >= 10 && hour < 10){
                            time = "0" + hour + ":" + minute;
                        }
                        else if(minute < 10 && hour >= 10){
                            time = hour + ":" + "0" + minute;
                        }
                        else{
                            time = hour + ":" + minute;
                        }
                        edt_start_time.setText(time);
                    }
                }, hour, minute, true);
                timePickerDialog.show();
            }
        });

        edt_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        CreateAppointmentActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        String time;
                        if (minute < 10 && hour < 10){
                            time = "0" + hour + ":" + "0" + minute;
                        }
                        else if(minute >= 10 && hour < 10){
                            time = "0" + hour + ":" + minute;
                        }
                        else if(minute < 10 && hour >= 10){
                            time = hour + ":" + "0" + minute;
                        }
                        else{
                            time = hour + ":" + minute;
                        }
                        edt_end_time.setText(time);
                    }
                }, hour, minute, true);
                timePickerDialog.show();
            }
        });

        createapppointmentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // databaseReference.child("Appointments").child(userid).setValue(name);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}