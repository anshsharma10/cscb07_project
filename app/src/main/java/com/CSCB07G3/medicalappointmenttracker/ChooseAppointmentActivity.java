package com.CSCB07G3.medicalappointmenttracker;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.CSCB07G3.medicalappointmenttracker.databinding.ActivityChooseAppointmentBinding;
import com.CSCB07G3.medicalappointmenttracker.ui.main2.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class ChooseAppointmentActivity extends AppCompatActivity {

    public static final String USERID = "userid";
    private ActivityChooseAppointmentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChooseAppointmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
    }
}