package com.example.ayush.rentalvehicles;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RentingActivity extends Activity
{
    private String email;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<RentingVehicle> mRentingVehicle;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        email=Global.E_MAIL;
        setContentView(R.layout.start_stop_renting);
        pd=new ProgressDialog(this);
        pd.setTitle("Fetching....");
        pd.setCancelable(false);
        pd.setMessage("It will take some time");
        mRecyclerView=(RecyclerView)findViewById(R.id.start_stop_recycler);
        mLayoutManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        class FetchProfile extends AsyncTask<String,Void,String>
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                pd.show();
            }

            @Override
            protected String doInBackground(String... strings)
            {
                OkHttpClient okHttpClient;
                Response response;
                try
                {
                    okHttpClient=new OkHttpClient();
                    RequestBody requestBody=new FormBody.Builder()
                                .add("email",strings[0])
                                .build();
                    Request request=new Request.Builder()
                                    .post(requestBody)
                                    .url("https://rent-my-vehicle.herokuapp.com/renting_profile.php")
                                    .build();
                    response=okHttpClient.newCall(request).execute();
                    if(response.isSuccessful())
                    {
                        return response.body().string();
                    }
                    else
                    {
                        return null;
                    }

                }catch(Exception e)
                {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(!TextUtils.isEmpty(s))
                {
                    try
                    {

                        JSONObject jsonObject=new JSONObject(s);
                        int count=Integer.parseInt(jsonObject.getString("count"));
                        JSONArray jsonArray=jsonObject.getJSONArray("vehicles");
                        mRentingVehicle=new ArrayList<RentingVehicle>();
                        for(int i=0;i<count;++i)
                        {
                            JSONObject jObject=jsonArray.getJSONObject(i);
                            RentingVehicle rVehicle=new RentingVehicle(jObject.getString("vehicle_id"),
                                    jObject.getString("vehicle_name"),
                                    jObject.getString("vehicle_no"),
                                    jObject.getString("image_id"),
                                    jObject.getString("image_url"),
                                    jObject.getString("booking"),
                                    jObject.getString("segment"));
                            mRentingVehicle.add(rVehicle);
                        }
                        //mRentingVehicle=new ArrayList<RentingVehicle>();
                        mAdapter=new RentingVehicleAdapter(mRentingVehicle,RentingActivity.this);
                        mRecyclerView.setAdapter(mAdapter);
                        //mAdapter.notifyDataSetChanged();
                        pd.dismiss();
                    }catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }


        }
        Online online=new Online(getApplicationContext());
        if(online.isConnected())
        {
            new FetchProfile().execute(this.email);
        }
        else
        {
            Toast.makeText(RentingActivity.this,"No Internet connection Available",Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
