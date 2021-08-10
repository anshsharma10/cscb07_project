package com.CSCB07G3.medicalappointmenttracker;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.CSCB07G3.medicalappointmenttracker.Model.LoginModel;
import com.CSCB07G3.medicalappointmenttracker.Presenters.LoginPresenter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {
    @Mock
    LoginModel loginModel;
    @Mock
    LoginActivity loginView;
    @Test
    public void testEmptyUsername(){
        when(loginView.getUsername()).thenReturn("");
        LoginPresenter presenter = new LoginPresenter(loginModel,loginView);
        presenter.Login();
        verify(loginView).setUsernameError("Username cannot be empty");
    }

    @Test
    public void testEmptyPassword(){
        when(loginView.getUsername()).thenReturn("abc");
        when(loginView.getPassWord()).thenReturn("");
        LoginPresenter presenter = new LoginPresenter(loginModel,loginView);
        presenter.Login();
        verify(loginView).setPasswordError("Password cannot be empty");
    }

    @Test
    public void testUserNotExist(){
        when(loginView.getUsername()).thenReturn("abc");
        when(loginView.getPassWord()).thenReturn("abc");
        when(loginModel.foundUser("abc")).thenReturn(null);
        LoginPresenter presenter = new LoginPresenter(loginModel,loginView);
        presenter.Login();
        verify(loginView).showMessage("User not found");
    }

    //@Test
    //public  void AppointmentSort(){
      //  ArrayList<Appointment> apps1 = new ArrayList<>();
        //ArrayList<Appointment> apps2 = new ArrayList<>();
        //Appointment a1 = new Appointment("1","d2",new AppTime(2021,9,18,12,0),new AppTime(2021,9,18,13,0));
        //Appointment a2 = new Appointment("2","d1",new AppTime(2021,8,18,12,0),new AppTime(2021,8,18,13,0));
        //Appointment a3 = new Appointment("3","d1",new AppTime(2021,9,18,12,0),new AppTime(2021,9,18,15,0));
        //apps1.add(a1);apps1.add(a2);apps1.add(a3);
        //apps2.add(a2);apps2.add(a1);apps2.add(a3);
        //Collections.sort(apps1);
        //assertEquals(apps1,apps2);
    //}
}