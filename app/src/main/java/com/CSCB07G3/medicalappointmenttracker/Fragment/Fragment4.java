package com.CSCB07G3.medicalappointmenttracker.Fragment;

import static com.CSCB07G3.medicalappointmenttracker.Fragment.Fragment1.USERID;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import com.CSCB07G3.medicalappointmenttracker.Model.Patient;
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

public class Fragment4 extends Fragment {
    String userId,filter_date,filter_time;
    Spinner date_spn,time_spn;
    ArrayList<String> dateList;
    ArrayList<Appointment> appointmentList;
    ListView listappointments;
    Fragment4.DoctorUpComeAppointmentAdapter doctorUpComeAppointmentAdapter;
    ArrayAdapter<String> date_adapter,time_adapter;
    DatabaseReference mDatabase;
    HashMap<String,ArrayList<String>> timeList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment4_layout, container, false);
        dateList = new ArrayList<>();
        timeList = new HashMap<>();
        appointmentList = new ArrayList<>();
        dateList.add("- -");
        timeList.put("- -", new ArrayList<>());
        timeList.get("- -").add("- -");
        userId = getActivity().getIntent().getStringExtra(USERID);
        listappointments = v.findViewById(R.id.listUppcomingAppointments1);
        date_spn = v.findViewById(R.id.spn_appointment_date1);
        time_spn = v.findViewById(R.id.spn_appointment_time1);
        doctorUpComeAppointmentAdapter = new DoctorUpComeAppointmentAdapter(v.getContext(),appointmentList);
        listappointments.setAdapter(doctorUpComeAppointmentAdapter);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if(userId != null){
            mDatabase.child("Doctors").child(userId).child("allApps").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    dateList = new ArrayList<>();
                    timeList = new HashMap<>();
                    appointmentList = new ArrayList<>();
                    dateList.add("- -");
                    timeList.put("- -", new ArrayList<>());
                    timeList.get("- -").add("- -");
                    for(DataSnapshot child : dataSnapshot.getChildren()) {
                        Appointment availability = child.getValue(Appointment.class);
                        if(availability.checkNull()){
                            Log.i("info","something wrong with "+child.getKey());
                        }else if(! availability.isPast()){
                            if(!appointmentList.contains(availability)){
                                appointmentList.add(availability);
                            }
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
                    date_adapter = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_spinner_item, dateList);
                    date_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    date_spn.setAdapter(date_adapter);
                    doctorUpComeAppointmentAdapter = new Fragment4.DoctorUpComeAppointmentAdapter(v.getContext(),appointmentList);
                    listappointments.setAdapter(doctorUpComeAppointmentAdapter);
                    doctorUpComeAppointmentAdapter.getFilter().filter(filter_date+";"+filter_time);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }

        date_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filter_date = date_adapter.getItem(position);
                time_adapter = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_spinner_item, timeList.get(filter_date));
                time_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                time_spn.setAdapter(time_adapter);
                doctorUpComeAppointmentAdapter.getFilter().filter(filter_date+";"+filter_time);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        time_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filter_time = time_adapter.getItem(position);
                doctorUpComeAppointmentAdapter.getFilter().filter(filter_date+";"+filter_time);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return v;
    }

    class DoctorUpComeAppointmentAdapter extends BaseAdapter implements Filterable {

        private ArrayList<Appointment> originAppointments; // appointments before filtered
        private ArrayList<Appointment> displayAppointments;    // appointments after filtered
        LayoutInflater inflater;

        public DoctorUpComeAppointmentAdapter(Context context, ArrayList<Appointment> appointmentList) {
            if(appointmentList == null){
                this.originAppointments = new ArrayList<>();
                this.displayAppointments = new ArrayList<>();
            }else{
                this.originAppointments = appointmentList;
                this.displayAppointments = appointmentList;
            }
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {

            if(displayAppointments == null){
                return 0;
            }
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
            TextView appDate,appStartTime,appEndTime,patientName,appMedInfo;
            Button btn_cancel;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            Fragment4.DoctorUpComeAppointmentAdapter.ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.doctor_upcoming_appointment_row, null);
                holder.tlContainer = convertView.findViewById(R.id.tlContainer1);
                holder.appDate = convertView.findViewById(R.id.appointmentDate1);
                holder.patientName = convertView.findViewById(R.id.doctorName1);
                holder.appStartTime = convertView.findViewById(R.id.start_time1);
                holder.appEndTime = convertView.findViewById(R.id.end_time1);
                holder.btn_cancel = convertView.findViewById(R.id.btn_patient_cancel_app1);
                holder.appMedInfo = convertView.findViewById(R.id.app_spec1);
                convertView.setTag(holder);
            } else {
                holder = (DoctorUpComeAppointmentAdapter.ViewHolder) convertView.getTag();
            }
            Appointment curr_app = displayAppointments.get(position);
            holder.appDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(curr_app.getStartTime().convertToDate()));
            holder.appStartTime.setText(new SimpleDateFormat("kk:mm").format(curr_app.getStartTime().convertToDate()));
            holder.appEndTime.setText(new SimpleDateFormat("kk:mm").format(curr_app.getEndTime().convertToDate()));
            if(curr_app.getPatientId() == null || curr_app.getPatientId().isEmpty()){
                holder.patientName.setText("(Empty)");
            }else{
                mDatabase.child("Patients").child(curr_app.getPatientId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            holder.patientName.setText(snapshot.getValue(Patient.class).getName());
                            holder.appMedInfo.setText(snapshot.getValue(Patient.class).getMedInfo());
                        }else{
                            holder.patientName.setText("(Removed)");
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                holder.btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDatabase.child("Appointments").child(curr_app.getAppointmentId()).child("patientId").setValue("");
                        if(! holder.patientName.getText().equals("(Removed)")){
                            mDatabase.child("Patients").child(curr_app.getPatientId()).child("allApps").child(curr_app.getAppointmentId()).removeValue();
                        }
                        mDatabase.child("Doctors").child(userId).child("allApps").child(curr_app.getAppointmentId()).child("patientId").setValue("");
                    }
                });
            }
            return convertView;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint,FilterResults results) {
                    displayAppointments = (ArrayList<Appointment>) results.values; // has the filtered values
                    notifyDataSetChanged();  // notifies the data with new filtered values
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                    ArrayList<Appointment>  FilteredList = new ArrayList<>();
                    if (originAppointments == null) {
                        originAppointments = new ArrayList<>(displayAppointments);
                    }
                    Collections.sort(originAppointments);
                    if (constraint == null || constraint.length() <=7) {
                        // set the Original result to return
                        results.count = originAppointments.size();
                        results.values = originAppointments;
                    }else {
                        String filter_d =constraint.toString().split(";")[0];
                        String filter_t =constraint.toString().split(";")[1];
                        for (Appointment data: originAppointments){
                            String data_d = new SimpleDateFormat("dd/MM/yyyy").format(data.getStartTime().convertToDate());
                            String data_t = new SimpleDateFormat("kk:mm").format(data.getStartTime().convertToDate())+" - "+ new SimpleDateFormat("kk:mm").format(data.getEndTime().convertToDate());
                            if((data_d.equals(filter_d)||filter_d.equals("- -")) && (data_t.equals(filter_t)|| filter_t.equals("- -"))){
                                FilteredList.add(data);
                            }
                        }
                        // set the Filtered result to return
                        Collections.sort(FilteredList);
                        results.count = FilteredList.size();
                        results.values = FilteredList;
                    }
                    return results;
                }
            };
        }
    }
}