package com.CSCB07G3.medicalappointmenttracker;

import static com.CSCB07G3.medicalappointmenttracker.Fragment.Fragment1.DOCTOR_SELECTED;
import static com.CSCB07G3.medicalappointmenttracker.Fragment.Fragment1.USERID;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.CSCB07G3.medicalappointmenttracker.Model.AppTime;
import com.CSCB07G3.medicalappointmenttracker.Model.Appointment;
import com.CSCB07G3.medicalappointmenttracker.Model.Doctor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.Objects;

public class PatientViewDoctorAvailabilityActivity extends AppCompatActivity {
    String doctorId,userId,filter_date;
    Spinner date_spn;
    ArrayList<String> dateList;
    LinkedHashMap<String,Appointment> availabilityMap;
    ListView listavailability;
    AvailabilityAdapter availabilityAdapter;
    ArrayAdapter<String> date_adapter;
    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dateList = new ArrayList<>();
        filter_date= "- - ";
        dateList.add("- -");
        availabilityMap = new LinkedHashMap<>();
        setContentView(R.layout.activity_patient_view_doctor_avaibility);
        doctorId = getIntent().getStringExtra(DOCTOR_SELECTED);
        userId = getIntent().getStringExtra(USERID);
        TextView title = findViewById(R.id.view_availability_title);
        listavailability = findViewById(R.id.listAvailability);
        date_spn = (Spinner) findViewById(R.id.spn_appointment_date);
        availabilityAdapter = new AvailabilityAdapter(getApplicationContext(),availabilityMap);
        listavailability.setAdapter(availabilityAdapter);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Doctors").child(doctorId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    title.setText("Availability of Doctor " + snapshot.getValue(Doctor.class).getName());
                }else{
                    Toast.makeText(PatientViewDoctorAvailabilityActivity.this,"Doctor removed",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        Query query = mDatabase.child("Appointments").orderByChild("doctorId").equalTo(doctorId);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dateList = new ArrayList<>();
                dateList.add("- -");
                availabilityMap = new LinkedHashMap<>();
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    Appointment availability = child.getValue(Appointment.class);
                    if(Objects.equals(availability.getPatientId(), "") && ! availability.isPast()){
                        availabilityMap.put(child.getKey(),availability);
                        Date d = new GregorianCalendar(availability.getStartTime().getYear(), availability.getStartTime().getMonth()-1, availability.getStartTime().getDay()).getTime();
                        String date = new SimpleDateFormat("dd/MM/yyyy").format(d);
                        if(! dateList.contains(date)){
                            dateList.add(date);
                        }
                    }
                }
                date_adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, dateList);
                date_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                date_spn.setAdapter(date_adapter);
                availabilityAdapter = new AvailabilityAdapter(getApplicationContext(),availabilityMap);
                listavailability.setAdapter(availabilityAdapter);
                availabilityAdapter.getFilter().filter(filter_date);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        query.addValueEventListener(valueEventListener);
        date_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filter_date = date_adapter.getItem(position);
                availabilityAdapter.getFilter().filter(filter_date);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    class AvailabilityAdapter extends BaseAdapter implements Filterable {

        private LinkedHashMap<String,Appointment> originAvailabilities; // availabilities before filtered
        private LinkedHashMap<String,Appointment> displayAvailabilities;    // availabilities after filtered
        LayoutInflater inflater;

        public AvailabilityAdapter(Context context, LinkedHashMap<String,Appointment> availabilities) {
            this.originAvailabilities = availabilities;
            this.displayAvailabilities = availabilities;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return displayAvailabilities.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {
            LinearLayout tlContainer,llContainer;
            TextView appDate,appStartTime,appEndTime,start_time_txt,end_time_txt;
            TableRow time_container,time_txt_container;
            TableLayout time_table;
            Button btn_book;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            AvailabilityAdapter.ViewHolder holder;

            if (convertView == null) {
                holder = new AvailabilityAdapter.ViewHolder();
                convertView = inflater.inflate(R.layout.row3, null);
                holder.tlContainer = convertView.findViewById(R.id.tlContainer);
                holder.llContainer = convertView.findViewById(R.id.llContainer);
                holder.appDate = convertView.findViewById(R.id.appointmentDate);
                holder.appStartTime = convertView.findViewById(R.id.start_time);
                holder.start_time_txt = convertView.findViewById(R.id.start_time_txt);
                holder.end_time_txt = convertView.findViewById(R.id.end_time_txt);
                holder.time_table = convertView.findViewById(R.id.time_table);
                holder.time_container = convertView.findViewById(R.id.time_container);
                holder.time_txt_container = convertView.findViewById(R.id.time_txt_container);
                holder.appEndTime = convertView.findViewById(R.id.end_time);
                holder.btn_book = convertView.findViewById(R.id.btn_book_appointment);
                convertView.setTag(holder);
            } else {
                holder = (AvailabilityAdapter.ViewHolder) convertView.getTag();
            }
            String event_key = (displayAvailabilities.keySet().toArray())[position].toString();
            AppTime start_event = displayAvailabilities.get(event_key).getStartTime();
            AppTime end_event = displayAvailabilities.get(event_key).getEndTime();
            Calendar calendar = Calendar.getInstance();
            calendar.set(start_event.getYear(),start_event.getMonth()-1,start_event.getDay(),start_event.getHour(),start_event.getMinute());
            holder.appDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime()));
            holder.appStartTime.setText(new SimpleDateFormat("hh:mm").format(calendar.getTime()));
            calendar.set(end_event.getYear(),end_event.getMonth()-1,end_event.getDay(),end_event.getHour(),end_event.getMinute());
            holder.appEndTime.setText(new SimpleDateFormat("hh:mm").format(calendar.getTime()));
            holder.btn_book.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mDatabase.child("Appointments").child(event_key).child("patientId").setValue(getIntent().getStringExtra(USERID));
                }
            });
            return convertView;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint,FilterResults results) {
                    displayAvailabilities = (LinkedHashMap<String, Appointment>) results.values; // has the filtered values
                    notifyDataSetChanged();  // notifies the data with new filtered values
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                    LinkedHashMap<String,Appointment>  FilteredMap = new LinkedHashMap<>();

                    if (originAvailabilities == null) {
                        originAvailabilities = new LinkedHashMap<>(displayAvailabilities);
                    }

                    if (constraint == null || constraint.length() == 0) {
                        // set the Original result to return
                        results.count = originAvailabilities.size();
                        results.values = originAvailabilities;
                    }else if(constraint == "- -"){
                        // set the Original result to return
                        results.count = originAvailabilities.size();
                        results.values = originAvailabilities;
                    }else {
                        for (String key: originAvailabilities.keySet()){
                            Appointment data = originAvailabilities.get(key);

                            if(new SimpleDateFormat("dd/MM/yyyy").format(new GregorianCalendar(data.getStartTime().getYear(), data.getStartTime().getMonth()-1, data.getStartTime().getDay()).getTime()).equals(constraint.toString())){
                                FilteredMap.put(key,data);
                            }
                        }
                        // set the Filtered result to return
                        results.count = FilteredMap.size();
                        results.values = FilteredMap;
                    }
                    return results;
                }
            };
        }
    }
}