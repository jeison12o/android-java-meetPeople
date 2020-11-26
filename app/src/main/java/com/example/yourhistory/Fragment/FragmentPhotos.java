package com.example.yourhistory.Fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.yourhistory.R;
import com.example.yourhistory.adapter.AdapterPhotosUser;
import com.example.yourhistory.adapter.AdapterUserImageUrl;
import com.example.yourhistory.model.Multimedia;
import com.example.yourhistory.model.User;
import com.example.yourhistory.model.UserEntered;
import com.example.yourhistory.model.VolleySingleton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class FragmentPhotos extends Fragment {

    RecyclerView recyclerPhotoUser;
    FloatingActionButton floatingButtonCamera;

    //camera
    private final int MY_PERMISSIONS = 100;
    final int CODE_PHOTO=20;

    private final String FOLDER_RAIZ="MyImagesMeetPeople";
    private final String ROUTE_IMAGE= FOLDER_RAIZ;
    private String path="";
    File fileImage;
    Bitmap bitmap;

    //service
    List<Multimedia> listMul;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photos, container, false);
        floatingButtonCamera = view.findViewById(R.id.floatingButtonCamera);
        recyclerPhotoUser = view.findViewById(R.id.recyclerPhotoUser);
        recyclerPhotoUser.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerPhotoUser.setHasFixedSize(true);
        if(requestPermissionsHigherVersions()){
            floatingButtonCamera.setEnabled(true);
        }else{
            floatingButtonCamera.setEnabled(false);
        }
        floatingButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCamera();
            }
        });
        getPhotos();
        return view;
    }

    private void printMessage (String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    //permissions
    private boolean requestPermissionsHigherVersions() {
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return true;
        }
        if((getContext().checkSelfPermission(WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)&&getContext().checkSelfPermission(CAMERA)==PackageManager.PERMISSION_GRANTED){
            return true;
        }
        if ((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)||(shouldShowRequestPermissionRationale(CAMERA)))){
            cargarDialogoRecomendacion();
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==MY_PERMISSIONS){
            if(grantResults.length==2 && grantResults[0]== PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){//el dos representa los 2 permisos
                Toast.makeText(getContext(),"Permisos aceptados",Toast.LENGTH_SHORT);
            }
        }else{
            solicitarPermisosManual();
        }
    }

    private void solicitarPermisosManual() {
        final CharSequence[] opciones={"si","no"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(getContext());//estamos en fragment
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("si")){
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri=Uri.fromParts("package",getContext().getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }else{
                    Toast.makeText(getContext(),"Los permisos no fueron aceptados",Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(getContext());
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");
        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE ,CAMERA},100);
            }
        });
        dialogo.show();
    }

    //camera
    private void showCamera() {
        File miFile=new File(Environment.getExternalStorageDirectory(), ROUTE_IMAGE);
        boolean isCreada=miFile.exists();
        if(isCreada==false){
            isCreada=miFile.mkdirs();
        }
        if(isCreada==true){
            Long consecutivo= System.currentTimeMillis()/1000;
            String nombre=consecutivo.toString()+".jpg";
            path=Environment.getExternalStorageDirectory()+File.separator+ROUTE_IMAGE +File.separator+nombre;
            fileImage=new File(path);
            Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImage));
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
            {
                String authorities=getContext().getPackageName()+".provider";
                Uri imageUri= FileProvider.getUriForFile(getContext(),authorities,fileImage);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            }else{
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImage));
            }
            startActivityForResult(intent, CODE_PHOTO);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CODE_PHOTO:
                MediaScannerConnection.scanFile(getContext(), new String[]{path}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("Path",""+path);
                            }
                        });
                bitmap= BitmapFactory.decodeFile(path);
                multimediaCreate();
                break;
        }
    }


    //service
    private String convertirImgString(Bitmap bitmap) {
        ByteArrayOutputStream array=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
        byte[] imagenByte=array.toByteArray();
        String imagenString= Base64.encodeToString(imagenByte,Base64.DEFAULT);
        return imagenString;
    }

    public void multimediaCreate(){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("save image...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.e("multimedia", "se esta creando multimdeia");
        final Gson gson = new Gson();
        final String server_url=  getString(R.string.url_serve) + "/multimedia";
        JSONObject json = new JSONObject();
        try {
            json.put("_iduserTo", UserEntered.getUserEntered().get_id());
            json.put("type", "photo");
            json.put("url", convertirImgString(bitmap));
            json.put("dateCreation", new Date().toString());
            json.put("category", "none");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,server_url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.hide();
                final String result=response.toString();
                printMessage(result);
                Log.e("multimedia", "creo multimedia");
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
        progressDialog.hide();
        getPhotos();
    }

    private void getPhotos(){
        listMul = new ArrayList();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("loading photos...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        final Gson gson = new Gson();
        final String url= getString(R.string.url_serve) + "/multimedia/userTo/"+UserEntered.getUserEntered().get_id()+"/type/photo";
        JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i= 0; i < response.length(); i++) {
                    Multimedia m = null;
                    try {
                        m = gson.fromJson(response.getJSONObject(i).toString(), Multimedia.class);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    listMul.add(m);
                }
                progressDialog.hide();
                AdapterPhotosUser adapter = new AdapterPhotosUser(listMul, getContext());
                recyclerPhotoUser.setAdapter(adapter);
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

}
