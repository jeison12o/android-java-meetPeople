package com.example.yourhistory;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.yourhistory.controller.CtlUser;
import com.example.yourhistory.model.User;
import com.example.yourhistory.view.SignUpActivity;

public class LoginActivity extends AppCompatActivity {

    TextView lblSign_up;
    EditText txtUser, txtPassword;
    CtlUser controlador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lblSign_up = findViewById(R.id.lblSign_up);
        txtUser = findViewById(R.id.txtLoginUser);
        txtPassword = findViewById(R.id.txtLoginPassword);
        SignUpActivity.changeLblLogin(lblSign_up);
        controlador = new CtlUser(getApplicationContext());
    }

    public void toSignUp(View view){
        startActivity(new Intent(this, SignUpActivity.class));
    }

    public void login(View view) {
        String user = txtUser.getText().toString(), password = txtPassword.getText().toString();
        if (user.isEmpty() || password.isEmpty()) {
        }else {
           controlador.getNameUser(user);
        }
    }

    private void printMessage (String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}