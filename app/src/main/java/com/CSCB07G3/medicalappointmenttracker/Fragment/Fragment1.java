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

import com.CSCB07G3.medicalappointmenttracker.Model.Doctor;
import com.CSCB07G3.medicalappointmenttracker.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Fragment1 extends Fragment {
    public static final String USERID = "userid";
    public static final String DOCTOR_SELECTED = "doctor_selected";
    private ListView listDoctor;
    private ArrayAdapter gender_spinner_adapter,spec_spinner_adapter;
    private String gender,spec,name;
    private ArrayList<Doctor> doctorList;
    private DoctorAdapter doctoradapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    class DoctorAdapter extends BaseAdapter implements Filterable {

        private ArrayList<Doctor> originDoctors; // doctors before filtered
        private ArrayList<Doctor> displayDoctors;    // doctors after filtered
        LayoutInflater inflater;

        public DoctorAdapter(Context context, ArrayList<Doctor> doctors) {
            this.originDoctors = doctors;
            this.displayDoctors = doctors;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return displayDoctors.size();
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
            TextView doctorName,doctorGender,doctorSpec;
            Button btn_view;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.row, null);
                holder.tlContainer = convertView.findViewById(R.id.tlContainer);
                holder.doctorName = convertView.findViewById(R.id.userName);
                holder.doctorGender = convertView.findViewById(R.id.userGender);
                holder.doctorSpec = convertView.findViewById(R.id.userInfo);
                holder.btn_view = convertView.findViewById(R.id.btn_view_appointment);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.doctorName.setText(displayDoctors.get(position).getName());
            holder.doctorGender.setText(displayDoctors.get(position).getGender());
            holder.doctorSpec.setText(displayDoctors.get(position).getSpecialization());
            holder.btn_view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.i("Choose Appointment",getActivity().getIntent().getStringExtra(USERID)+" select "+displayDoctors.get(position).getUserId());
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
                    displayDoctors = (ArrayList<Doctor>) results.values; // has the filtered values
                    notifyDataSetChanged();  // notifies the data with new filtered values
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                    ArrayList<Doctor>  FilteredList = new ArrayList<>();

                    if (originDoctors == null) {
                        originDoctors = new ArrayList<>(displayDoctors);
                    }

                    if (constraint == null || constraint.length() == 2) {
                        // set the Original result to return
                        results.count = originDoctors.size();
                        results.values = originDoctors;
                    } else {
                        String filter = constraint.toString().substring(0,constraint.toString().lastIndexOf(";"));
                        String filter_name = filter.substring(0,filter.lastIndexOf(";"));
                        //Log.i("name",filter_name);
                        String filter_spec = constraint.toString().substring(constraint.toString().lastIndexOf(";")+1);
                        //Log.i("spec",filter_spec);
                        String filter_gender = filter.substring(filter.lastIndexOf(";")+1);
                        //Log.i("gender",filter_gender);
                        for (int i = 0; i < originDoctors.size(); i++) {
                            Doctor data = originDoctors.get(i);
                            if (data.getName().toLowerCase().contains(filter_name.toLowerCase()) && (filter_gender.equals("- -") || data.getGender().equals(filter_gender)) && (filter_spec.equals("- -") || data.getSpecialization().equals(filter_spec))) {
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
        View v = inflater.inflate(R.layout.fragment1_layout, container, false);
        name="";
        EditText searchDoctor = v.findViewById(R.id.searchDoctor);
        listDoctor = v.findViewById(R.id.listDoctor);
        Spinner gender_spinner = v.findViewById(R.id.spn_doctor_gender);
        Spinner spec_spinner = v.findViewById(R.id.spn_doctor_spec);
        gender_spinner_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.genders, android.R.layout.simple_spinner_item);
        gender_spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender_spinner.setAdapter(gender_spinner_adapter);
        gender_spinner.setVisibility(View.VISIBLE);
        spec_spinner_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.specializations, android.R.layout.simple_spinner_item);
        spec_spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spec_spinner.setAdapter(spec_spinner_adapter);
        spec_spinner.setVisibility(View.VISIBLE);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Doctors");
        doctorList = new ArrayList<>();
        doctoradapter = new DoctorAdapter(getActivity().getApplicationContext(),doctorList);
        listDoctor.setAdapter(doctoradapter);
        ValueEventListener doctorListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                doctorList = new ArrayList<>();
                if(dataSnapshot.exists()){
                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                        Doctor doctor = singleSnapshot.getValue(Doctor.class);
                        doctorList.add(doctor);
                        doctoradapter = new DoctorAdapter(getActivity().getApplicationContext(),doctorList);
                        listDoctor.setAdapter(doctoradapter);
                        doctoradapter.getFilter().filter(name+";"+gender+";"+spec);
                    }
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
                doctoradapter.getFilter().filter(name+";"+gender+";"+spec);
            }
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
        spec_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                spec= spec_spinner_adapter.getItem(position).toString();
                doctoradapter.getFilter().filter(name+";"+gender+";"+spec);
            }
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
        searchDoctor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                name = charSequence.toString();
                doctoradapter.getFilter().filter(name+";"+gender+";"+spec);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        return v;
    }
}
