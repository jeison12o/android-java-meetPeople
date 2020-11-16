package com.example.yourhistory.Fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.yourhistory.R;
import com.example.yourhistory.SignUpActivity;
import com.example.yourhistory.model.User;
import com.example.yourhistory.model.UserEntered;
import com.example.yourhistory.model.VolleySingleton;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class FragmentPersonalInformation extends Fragment {

    EditText txtName, txtLastName, txtBirhtDate, txtPhoneNumber, txtPassword, txtConfirm;
    Button btnEdit;

    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_information, container, false);
        txtName = view.findViewById(R.id.txtPersonalName);
        txtLastName = view.findViewById(R.id.txtPersonalLastname);
        txtBirhtDate = view.findViewById(R.id.txtPersonalBirth_date);
        txtPhoneNumber = view.findViewById(R.id.txtPersonalPhone);
        txtPassword = view.findViewById(R.id.txtPersonalPassword);
        txtConfirm = view.findViewById(R.id.txtPersonalConfirm_password);
        btnEdit = view.findViewById(R.id.btnEditProfile);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit();
            }
        });
        load();
        changeEditTextToDate(txtBirhtDate);
        return view;
    }

    private void changeEditTextToDate(final EditText editText){
        editText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Date birthDate = new Date(UserEntered.getUserEntered().getBirthDate());
                Calendar mcurrentDate = Calendar.getInstance();
                mcurrentDate.setTime(birthDate);
                DatePickerDialog mDatePicker = new DatePickerDialog(getContext(),
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

    private void load() {
        txtName.setText(UserEntered.getUserEntered().getName());
        txtLastName.setText(UserEntered.getUserEntered().getLastName());
        txtBirhtDate.setText(UserEntered.getUserEntered().getBirthDate());
        txtPhoneNumber.setText(UserEntered.getUserEntered().getPhoneNumber());
        txtPassword.setText(UserEntered.getUserEntered().getPassword());
        txtConfirm.setText(UserEntered.getUserEntered().getPassword());
    }

    private void edit(){
        String name = txtName.getText().toString(),
                lastName = txtLastName.getText().toString(),
                birthDate = txtBirhtDate.getText().toString(),
                phone = txtPhoneNumber.getText().toString(),
                password = txtPassword.getText().toString(),
                confirmPassword = txtConfirm.getText().toString();

        if (name.isEmpty() || lastName.isEmpty() || birthDate.isEmpty() || phone.isEmpty() ||  password.isEmpty() || confirmPassword.isEmpty()) {
            printMessage("must complete all fields");
        }else {
            if(password.equals(confirmPassword)) {
                User u = new User();
                u.set_id(UserEntered.getUserEntered().get_id());
                u.setName(name);
                u.setLastName(lastName);
                u.setBirthDate(birthDate);
                u.setPhoneNumber(phone);
                u.setNameUser(UserEntered.getUserEntered().getNameUser());
                u.setPassword(password);
                u.setActive(true);
                updateUser(u);
            }else {
                printMessage("confirm password not equals pasword");
            }
        }
    }

    public void updateUser(User user){
        final Gson gson = new Gson();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("updating user loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        final String server_url= "http://192.168.0.13:1000/api/v1/user";
        JSONObject json = new JSONObject();
        try {
            json.put("_id", user.get_id());
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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT,server_url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //ocultamos el loading
                progressDialog.hide();
                final String result=response.toString();
                //Toast.makeText(context,"result : "+result,Toast.LENGTH_SHORT).show();
                printMessage("successfully update");
                UserEntered.setUserEntered(gson.fromJson(result, User.class));
                load();
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
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void printMessage (String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
