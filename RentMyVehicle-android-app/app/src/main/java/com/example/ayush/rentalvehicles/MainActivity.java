package com.example.ayush.rentalvehicles;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    Button login,signup;
    EditText e1,e2;
    String login_name,passwrd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        login = (Button)findViewById(R.id.login_btn);
        signup = (Button)findViewById(R.id.signup_btn);
        e1 = (EditText)findViewById(R.id.username_field);
        e2 = (EditText)findViewById(R.id.password_field);
        ImageLoaderConfiguration imageLoaderConfiguration=new ImageLoaderConfiguration.Builder(this)
                .build();
        ImageLoader.getInstance().init(imageLoaderConfiguration);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                login_name = e1.getText().toString().trim();
                passwrd = e2.getText().toString().trim();
                class Login extends AsyncTask<String,Void,String>{
                    ProgressDialog progressDialog;
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        progressDialog = ProgressDialog.show(MainActivity.this,"","Loading",true);
                    }

                    @Override
                    protected String doInBackground(String... strings) {
                        Response response;
                        try{
                            OkHttpClient okHttpClient = new OkHttpClient();
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("email",strings[0])
                                    .add("password",strings[1])
                                    .build();
                            Request request = new Request.Builder().url("https://rent-my-vehicle.herokuapp.com/login.php")
                                    .post(requestBody)
                                    .build();
                            response = okHttpClient.newCall(request).execute();
                            if(response.isSuccessful())
                            {
                                return response.body().string();
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(MainActivity.this,"Please try again ", Toast.LENGTH_SHORT).show();
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
                            if(status.equals("successfull"))
                            {
                                Toast.makeText(MainActivity.this,"Login successful!",Toast.LENGTH_LONG).show();
                                Global.E_MAIL = login_name;
                                Intent i1 = new Intent(MainActivity.this, Home_page.class);
                                startActivity(i1);
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this,"Invalid Username or Password",Toast.LENGTH_LONG).show();
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
                    Login login=new Login();
                    login.execute(login_name,passwrd);
                }
                else
                {
                    Toast.makeText(MainActivity.this,"No Internet connection available",Toast.LENGTH_LONG).show();
                }


            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(MainActivity.this,Sign_Up.class);
                startActivity(intent2);
            }
        });

    }
}
