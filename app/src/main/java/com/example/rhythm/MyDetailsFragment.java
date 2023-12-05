package com.example.rhythm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MyDetailsFragment extends Fragment {
    ProgressBar progressBar;
    TableLayout docTable;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_my_details, container, false);
        progressBar = view.findViewById(R.id.progressBarMD);
        docTable = view.findViewById(R.id.doc_info_row);


        //Toggle table
            TableLayout tableLayout = view.findViewById(R.id.my_info_row);
            ImageView toggle = view.findViewById(R.id.my_info_toggle);
            toggle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tableLayout.getVisibility() == View.GONE) {
                        tableLayout.setVisibility(View.VISIBLE);
                        toggle.setImageResource(R.drawable.baseline_arrow_drop_up_24);
                    } else {
                        tableLayout.setVisibility(View.GONE);
                        toggle.setImageResource(R.drawable.baseline_arrow_drop_down_24);
                    }
                }
            });

            ImageView docToggle = view.findViewById(R.id.doc_info_toggle);
            docToggle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (docTable.getVisibility() == View.GONE) {
                        docTable.setVisibility(View.VISIBLE);
                        docToggle.setImageResource(R.drawable.baseline_arrow_drop_up_24);
                    } else {
                        docTable.setVisibility(View.GONE);
                        docToggle.setImageResource(R.drawable.baseline_arrow_drop_down_24);
                    }
                }
            });

        //My information data fitting
        DataService dataService = new DataService(getContext());
        {
            dataService.getUserDetails(new DataService.UserDetailsResponseListener() {
                @Override
                public void onResponse(UserDataModel user) {

                    String[] userDetail = new String[13];
                    userDetail[0] = user.getPatient_id();
                    userDetail[1] = user.getP_name();
                    userDetail[2] = user.getP_dob();
                    userDetail[3] = user.getP_sex();
                    userDetail[4] = user.getP_house_no();
                    userDetail[5] = user.getP_street_name();
                    userDetail[6] = user.getP_city();
                    userDetail[7] = user.getP_pin_code();
                    userDetail[8] = user.getP_state();
                    userDetail[9] = user.getP_country();
                    userDetail[10] = user.getP_mobile();
                    userDetail[11] = user.getP_email();
                    userDetail[12] = user.getP_start_date();

                    TextView tvnotes = view.findViewById(R.id.tvnotes);
                    tvnotes.setText(user.getP_notes());
                    TableLayout myInfoTable = view.findViewById(R.id.my_info_row);
                    for (int row = 0; row < myInfoTable.getChildCount(); row++) {
                        TableRow tableRow = (TableRow) myInfoTable.getChildAt(row);
                        TextView textView = (TextView) tableRow.getChildAt(1);
                        textView.setText(userDetail[row]);
                    }
                    progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onError(String message) {

                }
            });
        }

        //Doctor assigned table
        dataService.getDoctorDetails(new DataService.DoctorDetailsResponseListener() {
            @Override
            public void onResponse(DoctorDataModel[] doctors) {
                createTable(doctors);
            }

            @Override
            public void onError(String message) {

            }
        });

       return view;
    }

    private void createTable(DoctorDataModel[] doctors) {

        for (int i = 0; i < doctors.length; i++){
            DoctorDataModel doctor = doctors[i];
            TableRow tr = new TableRow(getContext());
            tr.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            TextView docId = new TextView(getContext());
            TextView docName = new TextView(getContext());
            TextView admissionDate = new TextView(getContext());
            TextView hospitalName = new TextView(getContext());
            TextView hospitalid = new TextView(getContext());

            docId.setGravity(Gravity.CENTER);
            docName.setGravity(Gravity.CENTER);
            admissionDate.setGravity(Gravity.CENTER);
            hospitalName.setGravity(Gravity.CENTER);
            hospitalid.setGravity(Gravity.CENTER);

            docId.setPadding(10, 10, 10, 10);
            docName.setPadding(10, 10, 10, 10);
            admissionDate.setPadding(10, 10, 10, 10);
            hospitalName.setPadding(10, 10, 10, 10);
            hospitalid.setPadding(10, 10, 10, 10);

            docId.setText(doctor.doctorID);
            docName.setText(doctor.doctorName);
            admissionDate.setText(doctor.admissionDate);
            hospitalName.setText(doctor.hospitalName);
            hospitalid.setText(doctor.hospitalID);

            tr.addView(docId);
            tr.addView(docName);
            tr.addView(admissionDate);
            tr.addView(hospitalName);
            tr.addView(hospitalid);
            tr.setPadding(10, 10, 10, 10);

            docTable.addView(tr);
        }
    }

}