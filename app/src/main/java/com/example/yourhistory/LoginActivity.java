package com.example.yourhistory;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.yourhistory.controller.CtlUser;
import com.example.yourhistory.model.User;
import com.example.yourhistory.model.VolleySingleton;
import com.example.yourhistory.view.MainMenuActivity;
import com.example.yourhistory.view.SignUpActivity;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity {

    TextView lblSign_up;
    EditText txtUser, txtPassword;
    //se encarga de hacer un loading
    ProgressDialog progressDialog;
    //
    private User user;
    private String nameUser, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lblSign_up = findViewById(R.id.lblSign_up);
        txtUser = findViewById(R.id.txtLoginUser);
        txtPassword = findViewById(R.id.txtLoginPassword);
        SignUpActivity.changeLblLogin(lblSign_up);
    }

    public void toSignUp(View view){
        startActivity(new Intent(this, SignUpActivity.class));
    }

    public void login(View view) {
        nameUser = txtUser.getText().toString();
        password = txtPassword.getText().toString();
        if (nameUser.isEmpty() || password.isEmpty()) {
            printMessage("must complete all fields");
        }else {
           getNameUser(nameUser);
        }
    }

    public void getNameUser(String nameUser){
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("looking for user loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        final Gson gson = new Gson();
        final String server_url= "http://192.168.0.13:1000/api/v1/user/nameUser/"+nameUser;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.hide();
                final String result=response.toString();
                //printMessage("result : "+result);
                user = gson.fromJson(result, User.class);
                validatePassword(user);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                if(error.networkResponse != null) {
                    String m = new String(error.networkResponse.data);
                    printMessage(""+ m);
                }else {
                    printMessage("Error: "+ error.getMessage());
                }
            }
        });
        VolleySingleton.getIntanciaVolley(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void validatePassword(User i) {
        if(i.getPassword().equals(password)){
            toMenuDrawer();
        }else{
            printMessage("incorrect password");
        }
    }

    private void printMessage (String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void toMenuDrawer(){
        startActivity(new Intent(this, MainMenuActivity.class));
    }
}