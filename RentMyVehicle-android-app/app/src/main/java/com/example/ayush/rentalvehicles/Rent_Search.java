package com.example.ayush.rentalvehicles;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Ayush on 23-04-2017.
 */
public class Rent_Search extends AppCompatActivity {

    Calendar calendar;
    DatePicker datePicker1,datePicker2;
    int day,month,year;
    TextView dateView;
    Button search;
    EditText city;
    RecyclerView mrecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutmanager;
    ArrayList<vehicle> vehicleArrayList;
    EditText searchSegment,searchPrice;
    TextView dateStart,dateEnd;
    DateString start_date,end_date;
    public static final int DATE_PICKER_Id=111;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_vehicle);
        searchSegment=(EditText)findViewById(R.id.search_segment);
        searchPrice=(EditText)findViewById(R.id.search_price);
        dateView = (TextView)findViewById(R.id.search_start_date);
        mrecyclerView=(RecyclerView)findViewById(R.id.recycler_view_vehicle);
        mLayoutmanager=new LinearLayoutManager(this);
        mrecyclerView.setLayoutManager(mLayoutmanager);
        vehicleArrayList=new ArrayList<vehicle>();
        dateStart=(TextView)findViewById(R.id.date_start) ;
        dateEnd=(TextView)findViewById(R.id.date_end) ;
        calendar = Calendar.getInstance();
        city=(EditText)findViewById(R.id.search_city);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        search=(Button)findViewById(R.id.search_submit);
        search.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String city_entered=city.getText().toString().trim();
                String price_entered=searchPrice.getText().toString().trim();
                String segment_entered=searchSegment.getText().toString().trim();
                class vehicleRespone extends AsyncTask<String,Void,String>
                {
                    ProgressDialog pd;
                    @Override
                    protected void onPreExecute()
                    {
                        super.onPreExecute();
                        pd=new ProgressDialog(Rent_Search.this);
                        pd.show();
                    }


                    @Override
                    protected String doInBackground(String... strings)
                    {
                        Response response;
                        OkHttpClient okHttpClient;
                        try
                        {
                            okHttpClient=new OkHttpClient();

                            // have to do date start and date end
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("city",strings[0])
                                    .add("segment",strings[1])
                                    .add("date_start",strings[2])
                                    .add("date_end",strings[3])
                                    .build();
                            Request request = new Request.Builder().url("https://rent-my-vehicle.herokuapp.com/vehicle_response.php")
                                    .post(requestBody)
                                    .build();
                            response = okHttpClient.newCall(request).execute();
                            if(response.isSuccessful())
                            {
                                return response.body().string();
                            }
                            else
                            {
                                pd.dismiss();
                                Toast.makeText(Rent_Search.this, "Try again", Toast.LENGTH_SHORT).show();
                                return null;
                            }
                        }
                        catch(IOException e)
                        {
                            e.printStackTrace();
                            pd.dismiss();
                            Toast.makeText(Rent_Search.this, "Try again", Toast.LENGTH_SHORT).show();
                            return null;
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            pd.dismiss();
                            Toast.makeText(Rent_Search.this, "Try again", Toast.LENGTH_SHORT).show();
                            return null;
                        }

                        //return null;
                    }
                    @Override
                    protected void onPostExecute(String s)
                    {
                        super.onPostExecute(s);
                        if(!s.isEmpty())
                        {   vehicleArrayList.clear();
                            vehicleArrayList=new ArrayList<vehicle>();
                            try
                            {
                                JSONObject jsonobject=new JSONObject(s);
                                int count=Integer.parseInt(jsonobject.getString("count"));
                                JSONArray jsonArray=jsonobject.getJSONArray("result");
                                for(int i=0;i<count;++i)
                                {
                                    JSONObject jObject=jsonArray.getJSONObject(i);
                                    vehicle vh=new vehicle(jObject.getString("vehicle_id"),
                                            jObject.getString("user_id"),
                                            jObject.getString("vehicle_name"),
                                            jObject.getString("image_url"),
                                            jObject.getString("image_id"),
                                            jObject.getString("price"),
                                            jObject.getString("vehicle_no"));
                                    vehicleArrayList.add(vh);

                                }
                                mAdapter=new MyAdapter(Rent_Search.this,vehicleArrayList,start_date,end_date);
                                mrecyclerView.setAdapter(mAdapter);
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
                    new vehicleRespone().execute(city_entered,segment_entered,start_date.formattedDate(),end_date.formattedDate());
                }
                else
                {
                    Toast.makeText(Rent_Search.this,"No Internet Connection Available",Toast.LENGTH_LONG).show();
                }

            }
        });


        dateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                datePickerDialog=new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2)
                    {
                        start_date=new DateString(i,i1,i2);
                        dateStart.setText(start_date.formattedDate());
                    }
                },year,month,day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.setTitle("Pick Start Date");
                datePickerDialog.show();
            }
        });
        dateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                datePickerDialog=new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2)
                    {
                        end_date=new DateString(i,i1,i2);
                        dateEnd.setText(end_date.formattedDate());
                    }
                },year,month,day);
                //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.setTitle("Pick End Date");
                datePickerDialog.show();


            }
        });
    }


    }
