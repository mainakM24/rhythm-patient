package com.example.rhythm;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ApiService {

    Context context;
    public String changePasswordURL = "https://apex.oracle.com/pls/apex/wcm/user/v1/changepassword/";
    public String deviceAssignedURL = "https://apex.oracle.com/pls/apex/wcm/patient/v1/assigneddevice/";
    public String doctorAssignedURL = "https://apex.oracle.com/pls/apex/wcm/patient/v1/assigneddoctor/";
    public ApiService(Context context) {
        this.context = context;
    }

    public interface GetApiResponseListener{
        void onResponse(HashMap<String, String> apiLink);
        void onError(String message);
    }

    public void getApi(GetApiResponseListener getApiResponseListener){
        String url = "https://apex.oracle.com/pls/apex/wcm/params/v1/get";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int count = response.getInt("count");
                            HashMap<String, String> apiLinks = new HashMap<>();
                            JSONArray items = response.getJSONArray("items");
                            for(int i = 0; i < count; i++){
                                JSONObject singleItem = items.getJSONObject(i);
                                apiLinks.put(singleItem.getString("key"), singleItem.getString("value"));
                            }

                            getApiResponseListener.onResponse(apiLinks);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

}
