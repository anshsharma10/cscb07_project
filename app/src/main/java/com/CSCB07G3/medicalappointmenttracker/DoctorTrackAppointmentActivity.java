package com.CSCB07G3.medicalappointmenttracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.CSCB07G3.medicalappointmenttracker.Fragment.Fragment3;
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
        Intent intent = new Intent(getApplicationContext(), Fragment3.class).putExtra(USERID, doctorId);
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
        Button refresh = binding.btnRefresh;
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent().putExtra(USERID,getIntent().getStringExtra(USERID)).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                startActivity(intent);
            }
        });
    }
}