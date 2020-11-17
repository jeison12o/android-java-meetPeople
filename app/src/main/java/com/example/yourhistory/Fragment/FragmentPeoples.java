package com.example.yourhistory.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.yourhistory.LoginActivity;
import com.example.yourhistory.R;
import com.example.yourhistory.adapter.AdapterUserImageUrl;
import com.example.yourhistory.model.User;
import com.example.yourhistory.model.UserEntered;
import com.example.yourhistory.model.VolleySingleton;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentPeoples extends Fragment {

    RecyclerView recyclerP;
    List<User>  listUsers;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_peoples, container, false);
        recyclerP = view.findViewById(R.id.recyclerPeoples);
        recyclerP.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerP.setHasFixedSize(true);
        listUsers = new ArrayList();
        getUsers();
        return view;
    }

    private void getUsers(){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("loading users...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        final Gson gson = new Gson();
        final String url= "http://192.168.0.13:1000/api/v1/user";
        JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i= 0; i < response.length(); i++) {
                    User u = null;
                    try {
                        u = gson.fromJson(response.getJSONObject(i).toString(), User.class);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (!u.get_id().equals(UserEntered.getUserEntered().get_id())) listUsers.add(u);
                }
                progressDialog.hide();
                AdapterUserImageUrl adapter = new AdapterUserImageUrl(listUsers, getContext());
                recyclerP.setAdapter(adapter);
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
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonRequest);
    }

    private void printMessage (String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
