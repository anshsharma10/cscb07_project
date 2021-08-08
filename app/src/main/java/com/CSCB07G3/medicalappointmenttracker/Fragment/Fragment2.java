package com.CSCB07G3.medicalappointmenttracker.Fragment;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.CSCB07G3.medicalappointmenttracker.Model.Appointment;
import com.CSCB07G3.medicalappointmenttracker.Model.Doctor;
import com.CSCB07G3.medicalappointmenttracker.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Fragment2 extends Fragment {
    String userId,filter_date,filter_time;
    Spinner date_spn,time_spn;
    ArrayList<String> dateList;
    LinkedHashMap<String, Appointment> appointmentMap;
    ListView listappointments;
    Fragment2.PatientUpComeAppointmentAdapter patientUpComeAppointmentAdapter;
    ArrayAdapter<String> date_adapter,time_adapter;
    DatabaseReference mDatabase;
    HashMap<String,ArrayList<String>> timeList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment2_layout, container, false);
        dateList = new ArrayList<>();
        timeList = new HashMap<>();
        dateList.add("- -");
        timeList.put("- -", new ArrayList<>());
        timeList.get("- -").add("- -");
        appointmentMap = new LinkedHashMap<>();
        userId = getActivity().getIntent().getStringExtra(USERID);
        listappointments = v.findViewById(R.id.listUppcomingAppointments);
        date_spn = (Spinner) v.findViewById(R.id.spn_appointment_date);
        time_spn = (Spinner) v.findViewById(R.id.spn_appointment_time);
        patientUpComeAppointmentAdapter = new PatientUpComeAppointmentAdapter(getActivity().getApplicationContext(),appointmentMap);
        listappointments.setAdapter(patientUpComeAppointmentAdapter);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Patients").child(userId).child("allApps").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dateList = new ArrayList<>();
                timeList = new HashMap<>();
                dateList.add("- -");
                timeList.put("- -", new ArrayList<>());
                timeList.get("- -").add("- -");
                appointmentMap = new LinkedHashMap<>();
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    Appointment availability = child.getValue(Appointment.class);
                    if(! availability.isPast()){
                        mDatabase.child("Doctors").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.hasChild(availability.getDoctorId())){
                                    appointmentMap.put(child.getKey(),availability);
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
                                }else{
                                    mDatabase.child("Patients").child(userId).child("allApps").child(child.getKey()).removeValue();
                                    mDatabase.child("Appointments").child(child.getKey()).removeValue();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
                Collections.sort(dateList);
                date_adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, dateList);
                date_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                date_spn.setAdapter(date_adapter);
                patientUpComeAppointmentAdapter = new Fragment2.PatientUpComeAppointmentAdapter(getActivity(),appointmentMap);
                listappointments.setAdapter(patientUpComeAppointmentAdapter);
                patientUpComeAppointmentAdapter.getFilter().filter(filter_date+";"+filter_time);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        date_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filter_date = date_adapter.getItem(position);
                time_adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, timeList.get(filter_date));
                time_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                time_spn.setAdapter(time_adapter);
                patientUpComeAppointmentAdapter.getFilter().filter(filter_date+";"+filter_time);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        time_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filter_time = time_adapter.getItem(position);
                patientUpComeAppointmentAdapter.getFilter().filter(filter_date+";"+filter_time);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return v;
    }

    class PatientUpComeAppointmentAdapter extends BaseAdapter implements Filterable {

        private LinkedHashMap<String,Appointment> originAppointments; // appointments before filtered
        private LinkedHashMap<String,Appointment> displayAppointments;    // appointments after filtered
        LayoutInflater inflater;

        public PatientUpComeAppointmentAdapter(Context context, LinkedHashMap<String,Appointment> availabilities) {
            this.originAppointments = availabilities;
            this.displayAppointments = availabilities;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return displayAppointments.size();
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
            TextView appDate,appStartTime,appEndTime,doctorName,appSpec;
            Button btn_cancel;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            Fragment2.PatientUpComeAppointmentAdapter.ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.patient_upcoming_appointment_row, null);
                holder.tlContainer = convertView.findViewById(R.id.tlContainer);
                holder.appDate = convertView.findViewById(R.id.appointmentDate);
                holder.doctorName = convertView.findViewById(R.id.doctorName);
                holder.appStartTime = convertView.findViewById(R.id.start_time);
                holder.appEndTime = convertView.findViewById(R.id.end_time);
                holder.btn_cancel = convertView.findViewById(R.id.btn_patient_cancel_app);
                holder.appSpec = convertView.findViewById(R.id.app_spec);
                convertView.setTag(holder);
            } else {
                holder = (PatientUpComeAppointmentAdapter.ViewHolder) convertView.getTag();
            }
            String event_key = (displayAppointments.keySet().toArray())[position].toString();
            holder.appDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(displayAppointments.get(event_key).getStartTime().convertToDate()));
            holder.appStartTime.setText(new SimpleDateFormat("kk:mm").format(displayAppointments.get(event_key).getStartTime().convertToDate()));
            holder.appEndTime.setText(new SimpleDateFormat("kk:mm").format(displayAppointments.get(event_key).getEndTime().convertToDate()));
            mDatabase.child("Doctors").child(displayAppointments.get(event_key).getDoctorId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        holder.doctorName.setText(snapshot.getValue(Doctor.class).getName());
                        holder.appSpec.setText(snapshot.getValue(Doctor.class).getSpecialization());
                    }else{
                        holder.doctorName.setText("(Removed)");
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            holder.btn_cancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mDatabase.child("Patients").child(userId).child("allApps").child(event_key).removeValue();
                    mDatabase.child("Appointments").child(event_key).child("patientId").setValue("");
                    mDatabase.child("Patients").child(userId).child("allApps").child(event_key).removeValue();
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
                    displayAppointments = (LinkedHashMap<String, Appointment>) results.values; // has the filtered values
                    notifyDataSetChanged();  // notifies the data with new filtered values
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                    LinkedHashMap<String,Appointment>  FilteredMap = new LinkedHashMap<>();

                    if (originAppointments == null) {
                        originAppointments = new LinkedHashMap<>(displayAppointments);
                    }

                    if (constraint == null || constraint.length() <=7) {
                        // set the Original result to return
                        results.count = originAppointments.size();
                        results.values = originAppointments;
                    }else {
                        String filter_d =constraint.toString().split(";")[0];
                        String filter_t =constraint.toString().split(";")[1];
                        for (String key: originAppointments.keySet()){
                            Appointment data = originAppointments.get(key);
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
