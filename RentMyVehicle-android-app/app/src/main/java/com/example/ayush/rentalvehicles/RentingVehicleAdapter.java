package com.example.ayush.rentalvehicles;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.rey.material.widget.Switch;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by HP on 30-04-2017.
 */
public class RentingVehicleAdapter extends RecyclerView.Adapter<RentingVehicleAdapter.ViewHolder>
{
    ArrayList<RentingVehicle> mRenting;
    Context context;
    ImageLoader imageLoader;
    public RentingVehicleAdapter(ArrayList<RentingVehicle> mRenting,Context context)
    {
        this.mRenting=mRenting;
        this.context=context;
        this.imageLoader=ImageLoader.getInstance();
    }
    @Override
    public RentingVehicleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_vehicle_show,parent,false);
        return new ViewHolder(v);
    }
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imView;
        TextView vNo,vName;
        LinearLayout linearLayout;
        com.rey.material.widget.Switch aSwitch;
        public ViewHolder(View itemView)
        {
            super(itemView);
            imView=(ImageView)itemView.findViewById(R.id.image_profile_vehicle);
            vNo=(TextView)itemView.findViewById(R.id.no_profile_vehicle);
            vName=(TextView)itemView.findViewById(R.id.name_profile_vehicle);
            aSwitch=(com.rey.material.widget.Switch)itemView.findViewById(R.id.profile_switch);
            linearLayout=(LinearLayout)itemView.findViewById(R.id.linear_layout_renting);
        }
    }
    @Override
    public void onBindViewHolder(final RentingVehicleAdapter.ViewHolder holder, int position)
    {
        final RentingVehicle rh=mRenting.get(position);
        holder.vName.setText(rh.getVehicle_name());
        holder.vNo.setText(rh.getVehicle_no());
        int avail=Integer.parseInt(rh.getBooking());
        if(avail==0)
        {
            holder.aSwitch.setChecked(true);
        }
        else
        {
            holder.aSwitch.setChecked(false);
        }
        holder.aSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(Switch view, boolean checked)
            {
                if(checked)
                {
                    UpdateBooking updateBooking=new UpdateBooking(context);
                    updateBooking.execute(rh.getImage_id(),"0");
                }
                else
                {
                    UpdateBooking updateBooking=new UpdateBooking(context);
                    updateBooking.execute(rh.getImage_id(),"1");
                }
            }
        });
        imageLoader.displayImage(rh.getImage_url(), holder.imView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                Toast.makeText(view.getContext(),"Loading failed",Toast.LENGTH_LONG).show();
                //holder.linearLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
            {
                holder.imView.setVisibility(View.VISIBLE);

                holder.linearLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mRenting.size();
    }
}
class UpdateBooking extends AsyncTask<String,Void,String>
{
    ProgressDialog pd;
    Context context;
    public UpdateBooking(Context context)
    {
        pd=new ProgressDialog(context);
        pd.setTitle("Wait..");
        pd.setCancelable(false);
        pd.setMessage("Updating");
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
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
                    .add("image_id",strings[0])
                    .add("booking",strings[1])
                    .build();
            Request request = new Request.Builder().url("https://rent-my-vehicle.herokuapp.com/set_booking.php")
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
                return null;
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
            pd.dismiss();
            return null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            pd.dismiss();
            return null;
        }

    }

    @Override
    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);
        if(!TextUtils.isEmpty(s))
        {
            try
            {
                JSONObject jsonObject=new JSONObject(s);
                String status=jsonObject.getString("status");
                String message=jsonObject.getString("message");
                if(status.equals("successful"))
                {
                    Toast.makeText(context,message,Toast.LENGTH_LONG).show();
                    pd.dismiss();
                }
                else
                {
                    Toast.makeText(context,message,Toast.LENGTH_LONG).show();
                    pd.dismiss();
                }
            }catch (Exception e)
            {
                e.printStackTrace();
                pd.dismiss();
            }
        }
    }

}
