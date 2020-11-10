package com.example.yourhistory.view;

import android.app.DatePickerDialog;
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

import com.example.yourhistory.LoginActivity;
import com.example.yourhistory.R;
import com.example.yourhistory.controller.CtlUser;
import com.example.yourhistory.interfaces.UtilSignUp;
import com.example.yourhistory.model.User;

import java.util.Calendar;
import java.util.Date;

public class SignUpActivity extends AppCompatActivity {

    TextView lblLogin;
    static EditText txtName;
    static EditText txtLastName;
    static EditText txtBirthDate;
    static EditText txtPhone;
    static EditText txtUser;
    static EditText txtPassword;
    static EditText txtConfirmPassword;
    CtlUser controller;

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
        controller = new CtlUser(getApplicationContext());
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
                controller.createUser(u);
            }else {
                printMessage("confirm password not equals pasword");
            }
        }
    }

    private void printMessage (String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public static void cleanNameUser() {
        txtUser.setText("");
    }

    public static void clean() {
        txtName.setText("");
        txtLastName.setText("");
        txtBirthDate.setText("");
        txtPhone.setText("");
        txtPassword.setText("");
        txtConfirmPassword.setText("");
        txtUser.setText("");
    }

    public  void toLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}