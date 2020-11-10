package com.example.yourhistory.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import com.example.yourhistory.LoginActivity;
import com.example.yourhistory.interfaces.UtilSignUp;
import com.example.yourhistory.model.User;
import com.example.yourhistory.model.VolleySingleton;
import com.example.yourhistory.view.SignUpActivity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CtlUser {
    Context context;

    public CtlUser(Context context) {
        this.context = context;
    }

    public void createUser(User user){
        final String server_url= "http://192.168.0.13:1000/api/v1/user";
        JSONObject json = new JSONObject();
        try {
            json.put("name", user.getName());
            json.put("lastName", user.getLastName());
            json.put("birthDate", user.getBirthDate());
            json.put("phoneNumber", user.getPhoneNumber());
            json.put("nameUser", user.getNameUser());
            json.put("password", user.getPassword());
            json.put("active", user.isActive());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.POST,server_url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                final String result=response.toString();
                //Toast.makeText(context,"result : "+result,Toast.LENGTH_SHORT).show();
                Toast.makeText(context,"successfully created",Toast.LENGTH_SHORT).show();
                SignUpActivity.clean();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.networkResponse.data != null) {
                    String m = new String(error.networkResponse.data);
                    Toast.makeText(context, "Error: "+ m,Toast.LENGTH_SHORT).show();
                    SignUpActivity.cleanNameUser();
                }
            }
        });
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(jsonObjectRequest);
    }

    public void getNameUser(String nameUser){
        final Gson gson = new Gson();
        final String server_url= "http://192.168.0.13:1000/api/v1/user/nameUser/"+nameUser;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                final String result=response.toString();
                Toast.makeText(context,"result : "+result,Toast.LENGTH_SHORT).show();
                //gson.fromJson(result, User.class);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.networkResponse.data != null) {
                    String m = new String(error.networkResponse.data);
                    Toast.makeText(context, "Error: "+ m,Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "Error: "+ error.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }
}
