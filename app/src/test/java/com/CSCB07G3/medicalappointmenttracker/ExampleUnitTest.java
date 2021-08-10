package com.CSCB07G3.medicalappointmenttracker;

import static org.junit.Assert.assertEquals;

import com.CSCB07G3.medicalappointmenttracker.Model.AppTime;
import com.CSCB07G3.medicalappointmenttracker.Model.Appointment;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public  void AppointmentSort(){
        ArrayList<Appointment> apps1 = new ArrayList<>();
        ArrayList<Appointment> apps2 = new ArrayList<>();
        Appointment a1 = new Appointment("1","d2",new AppTime(2021,9,18,12,0),new AppTime(2021,9,18,13,0));
        Appointment a2 = new Appointment("2","d1",new AppTime(2021,8,18,12,0),new AppTime(2021,8,18,13,0));
        Appointment a3 = new Appointment("3","d1",new AppTime(2021,9,18,12,0),new AppTime(2021,9,18,15,0));
        apps1.add(a1);apps1.add(a2);apps1.add(a3);
        apps2.add(a2);apps2.add(a1);apps2.add(a3);
        Collections.sort(apps1);
        assertEquals(apps1,apps2);
    }

    //@RunWith(MockitoJUnitRunner.class)
}