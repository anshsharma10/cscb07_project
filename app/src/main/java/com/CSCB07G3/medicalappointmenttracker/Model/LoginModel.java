package com.CSCB07G3.medicalappointmenttracker.Model;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class LoginModel {
    HashMap<String,User> doctors;
    HashMap<String,User> patients;
    public LoginModel(){
        doctors = new HashMap<>();
        patients = new HashMap<>();
        DatabaseReference Ref = FirebaseDatabase.getInstance("https://medical-appointment-trac-30878-default-rtdb.firebaseio.com/").getReference();
        Ref.child("Doctors").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                doctors = new HashMap<>();
                for(DataSnapshot child: snapshot.getChildren()){
                    if(!child.getValue(Doctor.class).checkNull()){
                        doctors.put(child.getKey(),child.getValue(Doctor.class));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Ref.child("Patients").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                patients = new HashMap<>();
                for(DataSnapshot child: snapshot.getChildren()){
                    if(!child.getValue(Patient.class).checkNull()){
                        patients.put(child.getKey(),child.getValue(Patient.class));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public interface OnLoginListener{
        void loginSuccess(User user);
        void loginFailed(String s);
    }

    public void checkLogin(String userId,String passWord,OnLoginListener onLoginListener){
        HashMap<String,User> users = doctors;
        users.putAll(patients);
        if(users.containsKey(userId)){
            if(users.get(userId).getPassWord().equals(passWord)){
                onLoginListener.loginSuccess(users.get(userId));
            }else{
                onLoginListener.loginFailed("Wrong password");
            }
        }else{
            onLoginListener.loginFailed("User doesn't exist");
        }
    }
}
