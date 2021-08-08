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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;

public class PatientViewDoctorAvailabilityActivity extends AppCompatActivity {
    String doctorId,userId,filter_date,filter_time;
    Spinner date_spn,time_spn;
    ArrayList<String> dateList;
    LinkedHashMap<String,Appointment> availabilityMap;
    ListView listavailability;
    AvailabilityAdapter availabilityAdapter;
    ArrayAdapter<String> date_adapter,time_adapter;
    DatabaseReference mDatabase;
    HashMap<String,ArrayList<String>> timeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dateList = new ArrayList<>();
        timeList = new HashMap<>();
        dateList.add("- -");
        timeList.put("- -", new ArrayList<>());
        timeList.get("- -").add("- -");
        availabilityMap = new LinkedHashMap<>();
        setContentView(R.layout.activity_patient_view_doctor_avaibility);
        doctorId = getIntent().getStringExtra(DOCTOR_SELECTED);
        userId = getIntent().getStringExtra(USERID);
        TextView title = findViewById(R.id.view_availability_title);
        listavailability = findViewById(R.id.listAvailability);
        date_spn = (Spinner) findViewById(R.id.spn_appointment_date);
        time_spn = (Spinner) findViewById(R.id.spn_appointment_time);
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
                timeList = new HashMap<>();
                dateList.add("- -");
                timeList.put("- -", new ArrayList<>());
                timeList.get("- -").add("- -");
                availabilityMap = new LinkedHashMap<>();
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    Appointment availability = child.getValue(Appointment.class);
                    if(Objects.equals(availability.getPatientId(), "") && ! availability.isPast()){
                        availabilityMap.put(child.getKey(),availability);
                        String date = new SimpleDateFormat("dd/MM/yyyy").format(availability.getStartTime().convertToDate());
                        String time = new SimpleDateFormat("kk:mm").format(availability.getStartTime().convertToDate()) +" - "+ new SimpleDateFormat("kk:mm").format(availability.getEndTime().convertToDate());
                        if(! dateList.contains(date)){
                            dateList.add(date);
                            timeList.put(date,new ArrayList<>());
                            timeList.get(date).add("- -");
                            timeList.get(date).add(time);
                        }else if(! timeList.get(date).contains(time)){
                            timeList.get(date).add(time);
                        }
                    }
                }
                Collections.sort(dateList);
                date_adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, dateList);
                date_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                date_spn.setAdapter(date_adapter);
                availabilityAdapter = new AvailabilityAdapter(getApplicationContext(),availabilityMap);
                listavailability.setAdapter(availabilityAdapter);
                availabilityAdapter.getFilter().filter(filter_date+";"+filter_time);
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
                time_adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, timeList.get(filter_date));
                time_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                time_spn.setAdapter(time_adapter);
                availabilityAdapter.getFilter().filter(filter_date+";"+filter_time);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        time_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filter_time = time_adapter.getItem(position);
                availabilityAdapter.getFilter().filter(filter_date+";"+filter_time);
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
            LinearLayout tlContainer;
            TextView appDate,appStartTime,appEndTime;
            Button btn_book;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            AvailabilityAdapter.ViewHolder holder;

            if (convertView == null) {
                holder = new AvailabilityAdapter.ViewHolder();
                convertView = inflater.inflate(R.layout.doctor_availability_row, null);
                holder.tlContainer = convertView.findViewById(R.id.tlContainer);
                holder.appDate = convertView.findViewById(R.id.appointmentDate);
                holder.appStartTime = convertView.findViewById(R.id.start_time);
                holder.appEndTime = convertView.findViewById(R.id.end_time);
                holder.btn_book = convertView.findViewById(R.id.btn_book_appointment);
                convertView.setTag(holder);
            } else {
                holder = (AvailabilityAdapter.ViewHolder) convertView.getTag();
            }
            String event_key = (displayAvailabilities.keySet().toArray())[position].toString();
            holder.appDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(displayAvailabilities.get(event_key).getStartTime().convertToDate()));
            holder.appStartTime.setText(new SimpleDateFormat("kk:mm").format(displayAvailabilities.get(event_key).getStartTime().convertToDate()));
            holder.appEndTime.setText(new SimpleDateFormat("kk:mm").format(displayAvailabilities.get(event_key).getEndTime().convertToDate()));
            holder.btn_book.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //Start add appointment to patient appointment list
                    DatabaseReference AppRef = mDatabase.child("Appointments").child(event_key);
                    DatabaseReference dr = mDatabase.child("Patients").child(userId).child("allApps").child(event_key);
                    AppRef.child("patientId").setValue(userId);
                    displayAvailabilities.get(event_key).setPatientId(userId);
                    dr.setValue(displayAvailabilities.get(event_key));

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

                    if (constraint == null || constraint.length() <=7) {
                        // set the Original result to return
                        results.count = originAvailabilities.size();
                        results.values = originAvailabilities;
                    }else {
                        String filter_d =constraint.toString().split(";")[0];
                        String filter_t =constraint.toString().split(";")[1];
                        for (String key: originAvailabilities.keySet()){
                            Appointment data = originAvailabilities.get(key);
                            String data_d = new SimpleDateFormat("dd/MM/yyyy").format(data.getStartTime().convertToDate());
                            String data_t = new SimpleDateFormat("kk:mm").format(data.getStartTime().convertToDate())+" - "+ new SimpleDateFormat("kk:mm").format(data.getEndTime().convertToDate());
                            if((data_d.equals(filter_d)||filter_d.equals("- -")) && (data_t.equals(filter_t)|| filter_t.equals("- -"))){
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