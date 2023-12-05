package com.example.rhythm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.time.Duration;

public class DataService {

    public int days;

    Context context;
    public DataService(Context context) {this.context = context;}


    public interface UserValidateResponseListener{
        void onResponse(boolean valid);
        void onError(String message);
    }
    public interface UserDetailsResponseListener{
        void onResponse(UserDataModel user);
        void onError(String message);
    }
    public interface SessionDetailsResponseListener{
        void onResponse(SessionDataModel[] session);
        void onError(String message);
    }
    public interface DoctorDetailsResponseListener{
        void onResponse(DoctorDataModel[] doctors);
        void onError(String message);
    }

    public void userValidate( String username, String password, UserValidateResponseListener userValidateResponseListener){

     ApiService apiService = new ApiService(context);
     apiService.getApi(new ApiService.GetApiResponseListener() {
         @Override
         public void onResponse(HashMap<String, String> apiLink) {


             String URL = apiLink.get("USER_VALIDATE") + "/" + username + "/" + password;
             JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                     (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                         @Override
                         public void onResponse(JSONObject response) {
                             boolean valid;
                             try {
                                 valid = response.getInt("count") == 1 ;
                                 userValidateResponseListener.onResponse(valid);

                             } catch (JSONException e) {
                                 throw new RuntimeException(e);
                             }
                         }
                     }, new Response.ErrorListener() {
                         @Override
                         public void onErrorResponse(VolleyError error) {

                         }
                     });
             MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);

         }

         @Override
         public void onError(String message) {

         }
     });

    }

    public void getUserDetails(UserDetailsResponseListener userDetailsResponseListener){
        SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "error");
        String password = sharedPreferences.getString("password", "error");
        ApiService apiService = new ApiService(context);
        apiService.getApi(new ApiService.GetApiResponseListener() {
            @Override
            public void onResponse(HashMap<String, String> apiLink) {

                String URL = apiLink.get("USER_VALIDATE") + "/" + username + "/" + password;

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    JSONArray items = response.getJSONArray("items");
                                    JSONObject item = items.getJSONObject(0);
                                    String id = item.getString("id");
                                    String patient_id = item.getString("patient_id");
                                    String p_name = item.getString("p_name");
                                    String p_dob = item.getString("p_dob");
                                    String p_sex = item.getString("p_sex");
                                    String p_street_name = item.getString("p_street_name");
                                    String p_mobile = item.getString("p_mobile");
                                    String p_notes = item.getString("p_notes");
                                    String p_start_date = item.getString("p_start_date");
                                    String p_status = item.getString("p_status");
                                    String p_email = item.getString("p_email");
                                    String p_house_no = item.getString("p_house_no");
                                    String p_city = item.getString("p_city");
                                    String p_state = item.getString("p_state");
                                    String p_country = item.getString("p_country");
                                    String p_pin_code = item.getString("p_pin_cdoe");
                                    UserDataModel userDataModel = new UserDataModel(id, patient_id, p_name, p_dob, p_sex, p_street_name, p_mobile, p_notes, p_start_date, p_status, p_email, p_house_no, p_city, p_state, p_country, p_pin_code);
                                    userDetailsResponseListener.onResponse(userDataModel);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    public void getSessionDetails(SessionDetailsResponseListener sessionDetailsResponseListener){
        SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "error");

        ApiService apiService = new ApiService(context);
        apiService.getApi(new ApiService.GetApiResponseListener() {
            @Override
            public void onResponse(HashMap<String, String> apiLink) {

                LocalDate currentDate = LocalDate.now();
                LocalDate dateAllTime = currentDate.minusDays(3650);
                LocalDate date1YearAgo = currentDate.minusDays(365);
                LocalDate date30DaysAgo = currentDate.minusDays(30);
                LocalDate date7DaysAgo = currentDate.minusDays(7);

                String stCurrentDate = currentDate.format(DateTimeFormatter.BASIC_ISO_DATE) + "000000";
                String stDate30DaysAgo = date30DaysAgo.format(DateTimeFormatter.BASIC_ISO_DATE) + "000000";
                String stDate7DaysAgo = date7DaysAgo.format(DateTimeFormatter.BASIC_ISO_DATE) + "000000";
                String stDate1YearAgo = date1YearAgo.format(DateTimeFormatter.BASIC_ISO_DATE) + "000000";
                String stDateAllTime = dateAllTime.format(DateTimeFormatter.BASIC_ISO_DATE) + "000000";
                String startDate = stDateAllTime;
                String endDate = stCurrentDate;

                if (days == 7){
                    startDate = stDate7DaysAgo;
                    endDate = stCurrentDate;
                } else if (days == 30) {
                    startDate = stDate30DaysAgo;
//                    startDate = "20221013000000";
                } else if (days == 1) {
                    startDate = stDate1YearAgo;
                }

                String URL = apiLink.get("GET_SUMMARY_DATA") + "/" + username + "/" + startDate + "/" + endDate;

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    JSONArray items = response.getJSONArray("items");
                                    SessionDataModel[] sessionDataModel = new SessionDataModel[items.length()];
                                    for (int i = 0; i < items.length(); i++){
                                        JSONObject item = items.getJSONObject(i);
                                        sessionDataModel[i] = new SessionDataModel();

                                        int total_beat = item.getInt("total_analyzed_beat");
                                        int avg_beat = item.getInt("median_bpm_count");
                                        int normal_beat = item.getInt("normal_beat_count");
                                        sessionDataModel[i].setTotal_analyzed_beat(Integer.toString(total_beat));
                                        sessionDataModel[i].setMedian_bpm_count(Integer.toString(avg_beat));
                                        sessionDataModel[i].setNormal_beat_count(Integer.toString(normal_beat));
                                        sessionDataModel[i].setSession_id(item.getString("session_id"));
                                        sessionDataModel[i].setMax_bpm_count(item.getInt("max_bpm_count"));
                                        sessionDataModel[i].setMin_bpm_count(item.getInt("min_bpm_count"));

                                        String start = item.getString("start_time");
                                        String end = item.getString("end_time");
                                        Instant startTime = Instant.parse(start);
                                        Instant endTime = Instant.parse(end);
                                        Duration duration = Duration.between(startTime, endTime);
                                        long minutes = duration.toMinutes();

                                        sessionDataModel[i].setStart_time(start);
                                        sessionDataModel[i].setDuration(Long.toString(minutes) + " mins");

                                        sessionDataModel[i].setAp_beat_count(item.getInt("ap_beat_count"));
                                        sessionDataModel[i].setInt_normal_beat_count(item.getInt("normal_beat_count"));
                                        sessionDataModel[i].setSvf_beat_count(item.getInt("svf_beat_count"));

                                    }

                                    sessionDetailsResponseListener.onResponse(sessionDataModel);

                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }, error -> {
                            // TODO: Handle error
                        });

                MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    public void changePassword(String patientID, String currentPassword, String newPassword){

        ApiService apiService = new ApiService(context);
        String URL = apiService.changePasswordURL + patientID + "/" + currentPassword + "/" + newPassword;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                response -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Password changed successfully!!!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).show();
                },
                error -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Error")
                            .setMessage("You entered wrong password, try again!!!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).show();
                });
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    public void getDoctorDetails(DoctorDetailsResponseListener doctorDetailsResponseListener){
        SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "error");
        ApiService apiService = new ApiService(context);
        String baseURL = apiService.doctorAssignedURL;
        String URL = baseURL + username;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray items = response.getJSONArray("items");
                            DoctorDataModel[] doctorDataModels = new DoctorDataModel[items.length()];
                            for (int i = 0; i < items.length(); i++){
                                JSONObject item = items.getJSONObject(i);
                                doctorDataModels[i] = new DoctorDataModel();

                                doctorDataModels[i].doctorID = item.getString("doctor_id");
                                doctorDataModels[i].doctorName = item.getString("d_name");
                                doctorDataModels[i].hospitalName = item.getString("pd_hospital_name");
                                doctorDataModels[i].hospitalID = item.getString("boarding_id");
                                //formatting date
                                String input = item.getString("pd_admission_date");
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
                                String admiddionDate = LocalDateTime.parse(input, formatter).format(
                                        DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm a"));
                                doctorDataModels[i].admissionDate = admiddionDate;
                            }
                            doctorDetailsResponseListener.onResponse(doctorDataModels);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);

    }


}
