package com.example.yourhistory.view;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.yourhistory.LoginActivity;
import com.example.yourhistory.R;
import com.example.yourhistory.controller.CtlUser;
import com.example.yourhistory.interfaces.UtilSignUp;
import com.example.yourhistory.model.User;
import com.example.yourhistory.model.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class SignUpActivity extends AppCompatActivity {

    TextView lblLogin;
    EditText txtName;
    EditText txtLastName;
    EditText txtBirthDate;
    EditText txtPhone;
    EditText txtUser;
    EditText txtPassword;
    EditText txtConfirmPassword;
    //se encarga de hacer un loading
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        lblLogin = findViewById(R.id.lblLogin);
        txtName = findViewById(R.id.txtName);
        txtLastName = findViewById(R.id.txtLastname);
        txtBirthDate = findViewById(R.id.txtBirthDate);
        txtPhone = findViewById(R.id.txtPhone);
        txtUser = findViewById(R.id.txtUser);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        changeLblLogin(lblLogin);
        changeEditTextToDate(txtBirthDate);
    }

    static public void changeLblLogin(TextView date){
        //class that changes the string to format specific
        SpannableString changeString = new SpannableString(date.getText());

        //initialize format specific
        StyleSpan boldSan = new StyleSpan(Typeface.BOLD);
        UnderlineSpan underline = new UnderlineSpan();
        //apply changes
        changeString.setSpan(boldSan, 0, date.getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        changeString.setSpan(underline, 0, date.getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        date.setText(changeString);
    }

    private void changeEditTextToDate(final EditText editText){
        editText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                DatePickerDialog mDatePicker = new DatePickerDialog(SignUpActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker datepicker, int selectedyear,
                                                  int selectedmonth, int selectedday) {
                                editText.setText(selectedyear + "/" + selectedmonth + "/" + selectedday);
                            }
                        }, mcurrentDate.get(Calendar.YEAR), mcurrentDate.get(Calendar.MONTH),
                        mcurrentDate.get(Calendar.DAY_OF_MONTH));
                mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });
    }

    public void toMenuDrawer(View view){
        startActivity(new Intent(this, MainMenuActivity.class));
    }

    public void signUpUser(View view) {
        String name = txtName.getText().toString(),
            lastName = txtLastName.getText().toString(),
            birthDate = txtBirthDate.getText().toString(),
            phone = txtPhone.getText().toString(),
            user = txtUser.getText().toString(),
            password = txtPassword.getText().toString(),
            confirmPassword = txtConfirmPassword.getText().toString();

        if (name.isEmpty() || lastName.isEmpty() || birthDate.isEmpty() || phone.isEmpty() || user.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            printMessage("must complete all fields");
        }else {
            if(password.equals(confirmPassword)) {
                User u = new User();
                u.setName(name);
                u.setLastName(lastName);
                u.setBirthDate(birthDate);
                u.setPhoneNumber(phone);
                u.setNameUser(user);
                u.setPassword(password);
                u.setActive(true);
                createUser(u);
            }else {
                printMessage("confirm password not equals pasword");
            }
        }
    }

    public void createUser(User user){
        //configuramos el loading
        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setMessage("creating user loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        //y lo mostramos

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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,server_url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //ocultamos el loading
                progressDialog.hide();
                final String result=response.toString();
                //Toast.makeText(context,"result : "+result,Toast.LENGTH_SHORT).show();
                printMessage("successfully created");
                toLogin();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                if(error.networkResponse != null) {
                    String m = new String(error.networkResponse.data);
                    printMessage(""+ m);
                    txtUser.setText("");
                }else {
                    printMessage("Error: "+ error.getMessage());
                }
            }
        });
        VolleySingleton.getIntanciaVolley(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void printMessage (String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void clean() {
        txtName.setText("");
        txtLastName.setText("");
        txtBirthDate.setText("");
        txtPhone.setText("");
        txtPassword.setText("");
        txtConfirmPassword.setText("");
        txtUser.setText("");
    }

    public void toLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}