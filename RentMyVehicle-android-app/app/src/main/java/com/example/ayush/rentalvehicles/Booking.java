package com.example.ayush.rentalvehicles;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by HP on 28-04-2017.
 */
public class Booking extends Activity
{
    vehicle vh;
    ImageView bookingVehicle;
    Button bookNow;
    ProgressDialog pd;
    ImageLoader imageLoader;
    String dateStart,dateEnd;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_vehicle);
        Toast.makeText(Booking.this,"Welcome",Toast.LENGTH_LONG).show();
        imageLoader=ImageLoader.getInstance();
        bookingVehicle=(ImageView)findViewById(R.id.vehicle_booked);
        bookNow=(Button)findViewById(R.id.book_now);
        pd=new ProgressDialog(Booking.this);
        pd.setTitle("Patience is the key to success");
        Intent in=getIntent();
        String image_url=in.getStringExtra("image_url");
        imageLoader.displayImage(image_url, bookingVehicle, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view)
            {
                pd.show();
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason)
            {
                pd.dismiss();
                Toast.makeText(Booking.this,"Loading Failed",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
            {
                pd.dismiss();
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });

        String vehicle_id=in.getStringExtra("vehicle_id");
        String user_id=in.getStringExtra("user_id");
        String image_id=in.getStringExtra("image_id");
        String vehcile_name=in.getStringExtra("vehicle_name");
        dateStart=in.getStringExtra("date_start");
        dateEnd=in.getStringExtra("date_end");
        String vehicleNo=in.getStringExtra("vehicle_no");
        vh=new vehicle(vehicle_id,user_id,vehcile_name,image_url,image_id);
        pd.dismiss();
        bookNow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View view)
            {
                class BookVehicle extends AsyncTask<String,Void,String>
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
                        RequestBody requestBody;
                        Response response;
                        try
                        {
                            okHttpClient=new OkHttpClient();
                            requestBody=new FormBody.Builder()
                                    .add("email",Global.E_MAIL)
                                    .add("image_id",strings[0])
                                    .add("user_id",strings[1])
                                    .add("vehicle_id",strings[2])
                                    .add("date_start",strings[3])
                                    .add("date_end",strings[4])
                                    .build();
                            Request request=new Request.Builder()
                                    .url("https://rent-my-vehicle.herokuapp.com/booking.php")
                                    .post(requestBody)
                                    .build();
                            response=okHttpClient.newCall(request).execute();
                            if(response.isSuccessful())
                            {
                                return response.body().string();
                            }
                            else
                            {
                                Toast.makeText(Booking.this,"Please Try again later",Toast.LENGTH_LONG);
                                return null;
                            }

                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        return null;
                    }
                    @Override
                    protected void onPostExecute(String s)
                    {
                        super.onPostExecute(s);
                        pd.dismiss();
                        if(TextUtils.isEmpty(s))
                        {
                            Toast.makeText(Booking.this,"Please Try again later",Toast.LENGTH_LONG).show();
                            return;
                        }
                        try
                        {
                            JSONObject jsonObject=new JSONObject(s);
                            AlertDialog ad=new AlertDialog.Builder(view.getContext())
                                    .setTitle("Booking")
                                    .setMessage("Your car has been booked")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            finish();
                                        }
                                    })
                                    .setNeutralButton("", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    })
                                    .create();
                            ad.show();

                            //Toast.makeText(Booking.this,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                            //finish();
                        }catch (JSONException je)
                        {
                            je.printStackTrace();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
                new BookVehicle().execute(vh.getImageId(),vh.getUserId(),vh.getVehicleId(),
                        dateStart,
                        dateEnd);//0-image 1-user 2-vehi 3-start 4-end
            }
        });

    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        return super.onOptionsItemSelected(item);
    }
}
