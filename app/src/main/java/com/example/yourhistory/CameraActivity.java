package com.example.yourhistory;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback{

    Camera camera;

    Camera.PictureCallback jpegCallback;

    SurfaceView mSurfaceView;
    SurfaceHolder mSurfaceHolder;

    final int CAMERA_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mSurfaceView = findViewById(R.id.surfaceView);
        mSurfaceHolder = mSurfaceView.getHolder();

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }else{
            mSurfaceHolder.addCallback(this);
            mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }


        Button mCapture = findViewById(R.id.capture);

        mCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage();
            }
        });


        jpegCallback = new Camera.PictureCallback(){
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {

                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                Bitmap rotateBitmap = rotate(decodedBitmap);

                String fileLocation = SaveImageToStorage(rotateBitmap);
                if(fileLocation!= null){
                    //Intent intent = new Intent(getActivity(), ShowCaptureActivity.class);
                    //startActivity(intent);
                    return;
                }
            }
        };
    }

    public String SaveImageToStorage(Bitmap bitmap){
        String fileName = "imageToSend";
        try{
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            FileOutputStream fo = getApplicationContext().openFileOutput(fileName, Context.MODE_PRIVATE);
            fo.write(bytes.toByteArray());
            fo.close();
        }catch(Exception e){
            e.printStackTrace();
            fileName = null;
        }
        return fileName;
    }

    private Bitmap rotate(Bitmap decodedBitmap) {
        int w = decodedBitmap.getWidth();
        int h = decodedBitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(90);
        return Bitmap.createBitmap(decodedBitmap, 0, 0, w, h, matrix, true);
    }

    private void captureImage() {
        camera.takePicture(null, null, jpegCallback);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open();
        Camera.Parameters parameters;
        parameters = camera.getParameters();
        camera.setDisplayOrientation(90);
        parameters.setPreviewFrameRate(30);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

        Camera.Size bestSize = null;
        List<Camera.Size> sizeList = camera.getParameters().getSupportedPreviewSizes();
        bestSize = sizeList.get(0);
        for(int i = 1; i < sizeList.size(); i++){
            if((sizeList.get(i).width * sizeList.get(i).height) > (bestSize.width * bestSize.height)){
                bestSize = sizeList.get(i);
            }
        }
        parameters.setPictureSize(bestSize.width, bestSize.height);
        camera.setParameters(parameters);
        try {
            camera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        camera.startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    mSurfaceHolder.addCallback(this);
                    mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                }else{
                    Toast.makeText(getApplicationContext(), "Please provide the permission", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }
}