package com.CSCB07G3.medicalappointmenttracker.Presenters;

import androidx.annotation.NonNull;

import com.CSCB07G3.medicalappointmenttracker.Model.Doctor;
import com.CSCB07G3.medicalappointmenttracker.Model.Patient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginPresenter {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://medical-appointment-trac-30878-default-rtdb.firebaseio.com/").getReference();

    public LoginPresenter(){

    }

    public int UserExist(String userId, String passWord){
        final int[] ret = {0};

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // check if userid is not registered before
                if (snapshot.child("Patients").hasChild(userId)) {
                    String getPassword = snapshot.child("Patients").child(userId).getValue(Patient.class).getPassWord();
                    if (getPassword.equals(passWord)) {
                        ret[0] = 1;
                    } else {
                        ret[0] = 2;
                    }
                } else if (snapshot.child("Doctors").hasChild(userId)) {
                    String getPassword = snapshot.child("Doctors").child(userId).getValue(Doctor.class).getPassWord();

                    if (getPassword.equals(passWord)) {
                        ret[0] = 3;
                        System.out.println("chinese1");
                    } else {
                        ret[0] = 4;
                    }
                } else {
                    ret[0] = 5;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        System.out.println(ret[0]);
        return ret[0];
    }
}
