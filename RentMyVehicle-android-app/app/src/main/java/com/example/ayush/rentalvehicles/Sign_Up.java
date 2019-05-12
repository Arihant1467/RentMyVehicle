package com.example.ayush.rentalvehicles;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Ayush on 23-04-2017.
 */
public class Sign_Up extends AppCompatActivity {

    Button submit;
    EditText nm,em,pssd,mob,ct;
    public static final String sign_up_url = "https://rent-my-vehicle.herokuapp.com/insert_user_registration.php";
    String name,email,password,mobile,city;
    public String getCity()
    {
        return city;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.signup_layout);
        submit = (Button)findViewById(R.id.submit_btn);

        nm = (EditText)findViewById(R.id.name_reg);
        em = (EditText)findViewById(R.id.email_reg);
        pssd = (EditText)findViewById(R.id.password_reg);
        mob = (EditText)findViewById(R.id.mobile_reg);
        ct = (EditText)findViewById(R.id.city_reg);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nm.getText().toString().trim();
                email = em.getText().toString().trim();
                password = pssd.getText().toString().trim();
                mobile = mob.getText().toString().trim();
                city = ct.getText().toString().trim();
                Log.d("values",name+email+password+mobile+city);
                class Signup_Async extends AsyncTask<String,Void,String>{
                    String name,mobile,city,password,email;
                    ProgressDialog progressDialog;
                    public Signup_Async(String name,String mobile,String city,String password,String email)
                    {
                        name=this.name;
                        mobile=this.mobile;
                        city=this.city;
                        password=this.password;
                        email=this.email;
                    }
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        progressDialog = ProgressDialog.show(Sign_Up.this,"","Loading",true);
                    }

                    @Override
                    protected String doInBackground(String... strings)
                    {

                        Response response=null;
                        try{
                        OkHttpClient okHttpClient = new OkHttpClient();
                        RequestBody requestBody = new FormBody.Builder()
                                .add("name",strings[1])
                                .add("email",strings[2])
                                .add("password",strings[3])
                                .add("mobile",strings[4])
                                .add("city",strings[5])
                                .build();
                      Request request = new Request.Builder().url("https://rent-my-vehicle.herokuapp.com/insert_user_registration.php")
                                .post(requestBody)
                                .build();

                            response=okHttpClient.newCall(request).execute();
                            return response.body().string();
                        }
                        catch (IOException e){
                            e.printStackTrace();
                        }
                        catch (Exception e1){
                            e1.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        Log.d("Response",s);
                        try
                        {
                            JSONObject jsonObject = new JSONObject(s);
                            String status=jsonObject.getString("status");
                            if(status.equals("successfull"))
                            {
                                progressDialog.dismiss();
                                Toast.makeText(Sign_Up.this,"You are now a registered member",Toast.LENGTH_LONG).show();
                                Intent i2 = new Intent(Sign_Up.this,Home_page.class);
                                startActivity(i2);
                            }

                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                }

                Online online=new Online(getApplicationContext());
                if(online.isConnected())
                {
                    Signup_Async signup_async=new Signup_Async(name,mobile,city,password,email);
                    signup_async.execute(sign_up_url,name,email,password,mobile,city);

                }
                else
                {
                    Toast.makeText(Sign_Up.this,"No Internet connection available",Toast.LENGTH_LONG).show();
                }

            }
        });

    }
    public String getCognalusUrl(String mobile)
    {
        String COGNALYS_URL="https://www.cognalys.com/api/v1/otp/?";
        String APP_ID="app_id=9ed902d0304d4804a119ac0";
        String ACCESS_TOKEN="access_token=0da0435db8bf8b2c278e17f3bd1671e975d7c1a7";
        String url=COGNALYS_URL+APP_ID+"&"+ACCESS_TOKEN+"&mobile="+mobile;
        return url;
    }

}
