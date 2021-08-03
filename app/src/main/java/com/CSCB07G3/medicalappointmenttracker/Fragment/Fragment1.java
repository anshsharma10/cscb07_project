package com.CSCB07G3.medicalappointmenttracker.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.CSCB07G3.medicalappointmenttracker.ChooseAppointmentActivity;
import com.CSCB07G3.medicalappointmenttracker.Model.Doctor;
import com.CSCB07G3.medicalappointmenttracker.Model.User;
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
    private DatabaseReference mDatabase;
    private LinearLayout llContainer;
    private EditText searchDoctor;
    private ListView listDoctor;

    private ArrayList<User> doctorList;
    private UserAdapter useradapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        doctorList = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference("Doctors");
        ValueEventListener doctorListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                        Doctor doctor = singleSnapshot.getValue(Doctor.class);
                        doctorList.add(doctor);
                    }
                }
                useradapter = new UserAdapter(getActivity().getApplicationContext(),doctorList);
                listDoctor.setAdapter(useradapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.addValueEventListener(doctorListener);
    }
    class UserAdapter extends BaseAdapter implements Filterable {

        private ArrayList<User> originUsers; // users before filtered
        private ArrayList<User> displayUsers;    // users after filtered
        LayoutInflater inflater;

        public UserAdapter(Context context, ArrayList<User> users) {
            this.originUsers = users;
            this.displayUsers = users;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return displayUsers.size();
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
            LinearLayout llContainer;
            TextView userName;
            Button btn_view;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.row, null);
                holder.llContainer = convertView.findViewById(R.id.llContainer);
                holder.userName = convertView.findViewById(R.id.userName);
                holder.btn_view = convertView.findViewById(R.id.btn_view_appointment);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.userName.setText(displayUsers.get(position).getName());

            holder.btn_view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), ChooseAppointmentActivity.class);
                    intent.putExtra(DOCTOR_SELECTED,displayUsers.get(position).getUserId());
                    startActivity(intent);
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
                    displayUsers = (ArrayList<User>) results.values; // has the filtered values
                    notifyDataSetChanged();  // notifies the data with new filtered values
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                    ArrayList<User>  FilteredList = new ArrayList<>();

                    if (originUsers == null) {
                        originUsers = new ArrayList<>(displayUsers); // saves the original data in mOriginalValues
                    }

                    if (constraint == null || constraint.length() == 0) {

                        // set the Original result to return
                        results.count = originUsers.size();
                        results.values = originUsers;
                    } else {
                        constraint = constraint.toString().toLowerCase();
                        for (int i = 0; i < originUsers.size(); i++) {
                            User data = originUsers.get(i);
                            if (data.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
        searchDoctor = v.findViewById(R.id.searchDoctor);
        listDoctor = v.findViewById(R.id.listDoctor);

        searchDoctor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                useradapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return v;
    }
}
