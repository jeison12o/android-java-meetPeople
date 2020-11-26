package com.example.yourhistory.Fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.yourhistory.R;
import com.example.yourhistory.model.Multimedia;
import com.example.yourhistory.model.User;
import com.example.yourhistory.model.UserEntered;
import com.example.yourhistory.model.VolleySingleton;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class FragmentPersonalInformation extends Fragment {

    EditText txtName, txtLastName, txtBirhtDate, txtPhoneNumber, txtPassword, txtConfirm;
    Button btnEdit;

    ProgressDialog progressDialog;
    private boolean changeImage=false;

    //camera
    private final int MY_PERMISSIONS = 100;
    final int CODE_SELECTED=10;
    final int CODE_PHOTO=20;

    private final String FOLDER_RAIZ="MyImagesMeetPeople";
    private final String ROUTE_IMAGE= FOLDER_RAIZ;
    private String path="";
    File fileImage;
    Bitmap bitmap;

    ImageView imageProfile;

    //multimedia
    Multimedia multimediaPhoto;

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
        imageProfile = view.findViewById(R.id.imageViewUser);
        Button buIm = view.findViewById(R.id.btnImage);
        buIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multimediaCreate();
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (changeImage){
                }else{
                    edit();
                }
            }
        });
        load();
        changeEditTextToDate(txtBirhtDate);
        if(requestPermissionsHigherVersions()){
            imageProfile.setEnabled(true);
        }else{
            imageProfile.setEnabled(false);
        }
        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogOptions();
            }
        });
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
        if (UserEntered.getUserEntered().getUrlPhotoProfile() != null){
            loadImage(UserEntered.getUserEntered().getUrlPhotoProfile());
        }else{
            imageProfile.setImageResource(R.drawable.usuario);
        }
    }

    private void loadImage(String url){
        url = url.replace(" ", "%20");
        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imageProfile.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error load image: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(imageRequest);
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
        Log.e("update", "actualizando usuario");
        final Gson gson = new Gson();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("updating user loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        final String server_url= getString(R.string.url_serve) + "/user";
        JSONObject json = new JSONObject();
        try {
            json.put("_id", user.get_id());
            json.put("name", user.getName());
            json.put("lastName", user.getLastName());
            json.put("birthDate", user.getBirthDate());
            json.put("phoneNumber", user.getPhoneNumber());
            json.put("nameUser", user.getNameUser());
            json.put("password", user.getPassword());
            json.put("urlPhotoProfile", user.getUrlPhotoProfile());
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

    //permits and camera
    private void showDialogOptions() {
        final CharSequence[] options={"capture photo","choose from gallery","cancel"};
        final AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("choose an option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (options[i].equals("capture photo")){
                    showCamera();
                }else{
                    if (options[i].equals("choose from gallery")){
                        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent,"select"),CODE_SELECTED);
                    }else{
                        dialogInterface.dismiss();
                    }
                }
            }
        });
        builder.show();
    }

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
            intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(fileImage));
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
            case CODE_SELECTED:
                Uri myPath=data.getData();
                imageProfile.setImageURI(myPath);
                try {
                    bitmap=MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), myPath);
                    imageProfile.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case CODE_PHOTO:
                MediaScannerConnection.scanFile(getContext(), new String[]{path}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("Path",""+path);
                            }
                        });
                bitmap= BitmapFactory.decodeFile(path);
                imageProfile.setImageBitmap(bitmap);
                break;
        }
    }

    private String convertirImgString(Bitmap bitmap) {
        ByteArrayOutputStream array=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
        byte[] imagenByte=array.toByteArray();
        String imagenString= Base64.encodeToString(imagenByte,Base64.DEFAULT);
        return imagenString;
    }

    //PERMISOS
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
                imageProfile.setEnabled(true);
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

    //webservice multimedia
    public void multimediaCreate(){
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
                final String result=response.toString();
                printMessage(result);
                multimediaPhoto = gson.fromJson(result, Multimedia.class);
                UserEntered.getUserEntered().setUrlPhotoProfile(multimediaPhoto.getUrl());
                Log.e("multimedia", "creo multimedia");
                changeImage = false;
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
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

}
