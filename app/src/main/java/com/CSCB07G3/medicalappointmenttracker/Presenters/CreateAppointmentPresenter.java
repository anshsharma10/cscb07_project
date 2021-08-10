package com.CSCB07G3.medicalappointmenttracker.Presenters;

import androidx.annotation.NonNull;

import com.CSCB07G3.medicalappointmenttracker.Model.AppTime;
import com.CSCB07G3.medicalappointmenttracker.Model.Appointment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateAppointmentPresenter {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://medical-appointment-trac-30878-default-rtdb.firebaseio.com/").getReference();

    public CreateAppointmentPresenter(){

    }

    public int AddAppointment(String doctorid, int year, int month, int day, int start_hour, int start_minute, int end_hour, int end_minute){
        final Appointment[] app = new Appointment[1];
        AppTime t1 = new AppTime(year, month, day, start_hour, start_minute);
        AppTime t2 = new AppTime(year, month, day, end_hour, end_minute);
        if (t1.compareTo(t2) == 1 || t1.compareTo(t2) == 0){
            return 1;
        }
        else {
            databaseReference.addValueEventListener(new ValueEventListener() {
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
                    DatabaseReference dr = databaseReference.child("Doctors").child(doctorid).child("allApps").child(app[0].getAppointmentId());
                    dr.setValue(app[0]);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        return 0;
    }
}
