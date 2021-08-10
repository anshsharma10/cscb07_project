package com.CSCB07G3.medicalappointmenttracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.CSCB07G3.medicalappointmenttracker.databinding.ActivityDoctorTrackAppointmentBinding;
import com.CSCB07G3.medicalappointmenttracker.ui.main.SectionsPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class DoctorTrackAppointmentActivity extends AppCompatActivity {
    public static final String USERID = "userid";
    private ActivityDoctorTrackAppointmentBinding binding;
    public String doctorId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDoctorTrackAppointmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        doctorId = getIntent().getStringExtra(USERID);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = binding.fab;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), CreateAppointmentActivity.class);
                i.putExtra(USERID,doctorId);
                startActivity(i);
            }
        });
    }
}