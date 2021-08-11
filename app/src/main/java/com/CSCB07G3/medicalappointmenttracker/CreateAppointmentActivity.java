package com.CSCB07G3.medicalappointmenttracker;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.CSCB07G3.medicalappointmenttracker.Model.AppTime;
import com.CSCB07G3.medicalappointmenttracker.Model.Appointment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

public class CreateAppointmentActivity extends AppCompatActivity {
    public static final String USERID = "userid";
    Date d;
    EditText edt_date,edt_start_time, edt_end_time;
    Button createapppointmentbtn;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://medical-appointment-trac-30878-default-rtdb.firebaseio.com/").getReference();
    String doctorid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_appointment);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        doctorid = getIntent().getStringExtra(USERID);
        System.out.println(doctorid);

        edt_date = findViewById(R.id.editDate);
        edt_start_time = findViewById(R.id.editTimeslot1);
        edt_end_time = findViewById(R.id.editTimeslot2);
        createapppointmentbtn = findViewById(R.id.createappointmentbtn);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        final int[] start_hour = {calendar.get(Calendar.HOUR_OF_DAY)};
        final int[] start_minute = {calendar.get(Calendar.MINUTE)};
        int end_hour = calendar.get(Calendar.HOUR_OF_DAY);
        int end_minute = calendar.get(Calendar.MINUTE);

        final int[] year1 = new int[1];
        final int[] month1 = new int[1];
        final int[] day1 = new int[1];
        final int[] start_hour1 = new int[1];
        final int[] start_minute1 = new int[1];
        final int[] end_hour1 = new int[1];
        final int[] end_minute1 = new int[1];

        edt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        CreateAppointmentActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        year1[0] = year;
                        month1[0] = month;
                        day1[0] = day;
                        String date;
                        if (month < 10 && day < 10){
                            d = new Date();
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
                        start_minute1[0] = minute;
                        start_hour1[0] = hour;

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
                }, start_hour[0], start_minute[0], true);
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
                        end_hour1[0] = hour;
                        end_minute1[0] = minute;
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
                }, end_hour, end_minute, true);
                timePickerDialog.show();
            }
        });

        createapppointmentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(doctorid);
                final Appointment[] app = new Appointment[1];
                AppTime t1 = new AppTime(year1[0], month1[0], day1[0], start_hour1[0], start_minute1[0]);
                AppTime t2 = new AppTime(year1[0], month1[0], day1[0], end_hour1[0], end_minute1[0]);
                if (t2.convertToDate().before(t1.convertToDate())){
                    Toast.makeText(CreateAppointmentActivity.this, "End time must be later than start time", Toast.LENGTH_SHORT).show();
                }
                else if(t1.convertToDate().before(new Date())){
                    Toast.makeText(CreateAppointmentActivity.this, "The time must be later than now", Toast.LENGTH_SHORT).show();
                }
                else {
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (!snapshot.child("Appointments").hasChild("totalapp")) {
                                databaseReference.child("Appointments").child("totalapp").setValue(1);
                                //databaseReference.child("Appointments").child("1").setValue(doctorid);
                                app[0] = new Appointment("1", doctorid, t1, t2);
                                databaseReference.child("Appointments").child("1").setValue(app[0]);
                            } else {
                                String n = snapshot.child("Appointments").child("totalapp").getValue().toString();
                                int n1 = Integer.parseInt(n) + 1;
                                n = "" + n1;
                                //databaseReference.child("Appointments").child(n).setValue(doctorid);
                                app[0] = new Appointment(n, doctorid, t1, t2);
                                databaseReference.child("Appointments").child(n).setValue(app[0]);
                                databaseReference.child("Appointments").child("totalapp").setValue(n);
                            }
                            DatabaseReference dr = databaseReference.child("Doctors").child(doctorid).child("upcomeApps").child(app[0].getAppointmentId());
                            dr.setValue(app[0]);
                            startActivity(new Intent(CreateAppointmentActivity.this, DoctorTrackAppointmentActivity.class).putExtra(USERID,doctorid));
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }
}