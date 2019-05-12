package com.example.ayush.rentalvehicles;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.*;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Ayush on 24-04-2017.
 */
public class Registration extends AppCompatActivity{

    Button pic,submit_reg_btn;
    static int SELECT_PHOTO = 1;
    public static Bitmap image;
    ImageView imageView;
    EditText e1,e2,e3,e4,e5,e6,e7;
    String segment,model,vnumber,license,aadhar,city,vehicleCost;
    public static final String REGISTRATION="Registration";
    File f;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.registration_layout);

        pic = (Button)findViewById(R.id.upload_picture);
        submit_reg_btn = (Button)findViewById(R.id.reg_btn);
        e1 = (EditText)findViewById(R.id.vehicle_segment);
        e2 = (EditText)findViewById(R.id.vehicle_model);
        e3 = (EditText)findViewById(R.id.vehicle_number);
        e4 = (EditText)findViewById(R.id.license_number);
        e5 = (EditText)findViewById(R.id.aadhar_number);
        e6 = (EditText)findViewById(R.id.owner_city);
        e7=(EditText)findViewById(R.id.vehicle_cost);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
                return;
            }
        }

        submit_reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                segment = e1.getText().toString().trim();
                model = e2.getText().toString().trim();
                vnumber = e3.getText().toString().trim();
                license = e4.getText().toString().trim();
                aadhar = e5.getText().toString().trim();
                city = e6.getText().toString().trim();
                vehicleCost=e7.getText().toString().trim();
                class Reg extends AsyncTask<String,Void,String>{

                    ProgressDialog progressDialog;

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        progressDialog = ProgressDialog.show(Registration.this,"","Loading",true);
                    }

                    @Override
                    protected String doInBackground(String... strings) {
                        Response response;
                        try{
                            OkHttpClient okHttpClient = new OkHttpClient();
                            //File f  = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                            String content_type  = getMimeType(f.getPath());
                            String file_path = f.getAbsolutePath();
                            OkHttpClient client = new OkHttpClient();
                            RequestBody file_body = RequestBody.create(MediaType.parse(content_type),f);

                            RequestBody requestBody = new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("vehicle_no",strings[0])
                                    .addFormDataPart("license_no",strings[1])
                                    .addFormDataPart("city",strings[2])
                                    .addFormDataPart("email",strings[3])
                                    .addFormDataPart("segment",strings[4])
                                    .addFormDataPart("vehicle_name",strings[5])
                                    .addFormDataPart("aadhar",strings[6])
                                    .addFormDataPart("price",strings[7])
                                    .addFormDataPart("image",file_path.substring(file_path.lastIndexOf("/")+1),file_body)
                                    .build();
                            Request request = new Request.Builder().url("https://rent-my-vehicle.herokuapp.com/vehicle_registration.php")
                                    .post(requestBody)
                                    .build();
                            response = okHttpClient.newCall(request).execute();
                            if(response.isSuccessful())
                            {
                                return response.body().string();
                            }
                            else {
                                return null;
                            }
                        }
                        catch(IOException io){
                            io.printStackTrace();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

                        return null;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        try
                        {
                            JSONObject jsonObject=new JSONObject(s);
                            String status=jsonObject.getString("status");
                            progressDialog.dismiss();
                            if(status.equals("successful"))
                            {
                                Toast.makeText(Registration.this,"Registration successful!",Toast.LENGTH_LONG).show();
                                Intent i11 = new Intent(Registration.this, Home_page.class);
                                startActivity(i11);
                            }
                            else
                            {
                                Toast.makeText(Registration.this,"Registration Unsuccessful",Toast.LENGTH_LONG).show();
                            }

                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
                Online online=new Online(getApplicationContext());
                if(online.isConnected())
                {
                    Reg reg = new Reg();
                    reg.execute(vnumber,license,city,Global.E_MAIL,segment,model,aadhar,vehicleCost);
                }
                else
                {
                    Toast.makeText(Registration.this,"No Internet connection available",Toast.LENGTH_LONG).show();
                }


            }
        });


        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent galleryIntent = new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(galleryIntent,SELECT_PHOTO);
                new MaterialFilePicker()
                        .withActivity(Registration.this)
                        .withRequestCode(10)
                        .start();

            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
            Log.d(REGISTRATION,"Yes registered");
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
            }
        }
    }
    private String getMimeType(String path) {

        String extension = MimeTypeMap.getFileExtensionFromUrl(path);

        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10 && resultCode==RESULT_OK){
            Uri uri = data.getData();
            //f=new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
            try {
//
//                image = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                imageView = (ImageView)findViewById(R.id.image_vehicle);
//                imageView.setImageBitmap(image);
//                imageView.setVisibility(View.VISIBLE);
                f=new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                Bitmap myBitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                imageView = (ImageView)findViewById(R.id.image_vehicle);
                imageView.setImageBitmap(myBitmap);
                imageView.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
