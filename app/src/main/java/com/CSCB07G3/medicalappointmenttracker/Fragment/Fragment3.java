package com.CSCB07G3.medicalappointmenttracker.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.CSCB07G3.medicalappointmenttracker.Model.Patient;
import com.CSCB07G3.medicalappointmenttracker.R;
import com.google.android.gms.common.internal.Asserts;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Fragment3 extends Fragment {
    public static final String USERID = "userid";
    private ListView listPatient;
    private ArrayAdapter gender_spinner_adapter;
    private String gender,name;
    private ArrayList<Patient> patientList;
    private PatientAdapter patientadapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    class PatientAdapter extends BaseAdapter implements Filterable {

        private ArrayList<Patient> originPatients; // doctors before filtered
        private ArrayList<Patient> displayPatients;    // doctors after filtered
        LayoutInflater inflater;

        public PatientAdapter(Context context, ArrayList<Patient> patients) {
            this.originPatients = patients;
            this.displayPatients = patients;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return displayPatients.size();
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
            TextView patientName,patientGender;
            Button btn_view;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.row, null);
                holder.tlContainer = convertView.findViewById(R.id.tlContainer);
                holder.patientName = convertView.findViewById(R.id.userName);
                holder.patientGender = convertView.findViewById(R.id.userGender);
                holder.btn_view = convertView.findViewById(R.id.btn_view_appointment);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.patientName.setText(displayPatients.get(position).getName());
            holder.patientGender.setText(displayPatients.get(position).getGender());
            holder.btn_view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.i("Choose Appointment",getActivity().getIntent().getStringExtra(USERID)+" select "+displayPatients.get(position).getUserId());
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
                    displayPatients = (ArrayList<Patient>) results.values; // has the filtered values
                    Asserts.checkNotNull(results.values);
                    notifyDataSetChanged();  // notifies the data with new filtered values
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    System.out.println(constraint);
                    FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                    ArrayList<Patient>  FilteredList = new ArrayList<>();

                    if (originPatients == null) {
                        originPatients = new ArrayList<>(displayPatients);
                    }

                    if (constraint == null || constraint.length() == 2) {
                        System.out.println("ckgt");
                        // set the Original result to return
                        results.count = originPatients.size();
                        results.values = originPatients;
                    } else {
                        String filter = constraint.toString();
                        int tmp = filter.lastIndexOf(";");
                        if (tmp < 0) {
                            results.count = originPatients.size();
                            results.values = originPatients;
                            return results;
                        }
                        String filter_name = filter.substring(0,tmp);
                        //Log.i("name",filter_name);
                        //String filter_spec = constraint.toString().substring(constraint.toString().lastIndexOf(";")+1);
                        //Log.i("spec",filter_spec);
                        String filter_gender = filter.substring(tmp+1);
                        //Log.i("gender",filter_gender);
                        for (int i = 0; i < originPatients.size(); i++) {
                            Patient data = originPatients.get(i);
                            if (data.getName().toLowerCase().contains(filter_name.toLowerCase()) && (filter_gender.equals("- -") || data.getGender().equals(filter_gender))) {
                                FilteredList.add(data);
                            }
                        }
                        // set the Filtered result to return
                        results.count = FilteredList.size();
                        results.values = FilteredList;
                    }
                    return results;
                }
            };
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment3_layout, container, false);
        name="";
        EditText searchPatient = v.findViewById(R.id.searchDoctor1);
        listPatient = v.findViewById(R.id.listDoctor1);
        Spinner gender_spinner = v.findViewById(R.id.spn_doctor_gender1);
        gender_spinner_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.genders, android.R.layout.simple_spinner_item);
        gender_spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender_spinner.setAdapter(gender_spinner_adapter);
        gender_spinner.setVisibility(View.VISIBLE);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Patients");
        patientList = new ArrayList<Patient>();
        patientadapter = new PatientAdapter(getActivity().getApplicationContext(),patientList);
        listPatient.setAdapter(patientadapter);
        ValueEventListener doctorListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                patientList = new ArrayList<>();
                if(dataSnapshot.exists()){
                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                        Patient patient = singleSnapshot.getValue(Patient.class);
                        patientList.add(patient);
                    }
                    patientadapter = new PatientAdapter(getActivity().getApplicationContext(),patientList);
                    listPatient.setAdapter(patientadapter);
                    patientadapter.getFilter().filter(name+";"+gender);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mDatabase.addValueEventListener(doctorListener);
        gender_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                gender= gender_spinner_adapter.getItem(position).toString();
                patientadapter.getFilter().filter(name+";"+gender);
            }
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
        searchPatient.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                name = charSequence.toString();
                patientadapter.getFilter().filter(name+";"+gender);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        return v;
    }
}
