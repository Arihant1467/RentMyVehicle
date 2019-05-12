package com.example.ayush.rentalvehicles;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.List;
import  com.rey.material.widget.ProgressView;
/**
 * Created by HP on 27-04-2017.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>
{
    ImageLoader imageLoader;
    ArrayList<vehicle> vh;
    Activity activity;
    DateString dateStart,dateEnd;
    public MyAdapter() {
        super();
    }

    public MyAdapter(Activity activity,ArrayList<vehicle> vh,DateString dateStart,DateString dateEnd)
    {
        this.vh=vh;
        this.activity=activity;
        imageLoader=ImageLoader.getInstance();
        this.dateStart=dateStart;
        this.dateEnd=dateEnd;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicle_show,parent,false);

        return new ViewHolder(v);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView vehicleShow;
        TextView priceShow,nameShow;
        LinearLayout mainLinear;
        ProgressView pv;


        public ViewHolder(View itemView)
        {
            super(itemView);
            vehicleShow=(ImageView) itemView.findViewById(R.id.image_vehicle);
            priceShow=(TextView) itemView.findViewById(R.id.price_tag);
            mainLinear=(LinearLayout)itemView.findViewById(R.id.main_linear);
            nameShow=(TextView) itemView.findViewById(R.id.vehicle_name);
            pv=(ProgressView)itemView.findViewById(R.id.progressbar);
            pv.start();
            pv.setVisibility(View.VISIBLE);


        }
    }
    @Override
    public void onBindViewHolder(final MyAdapter.ViewHolder holder, int position)
    {
        final vehicle vehi =vh.get(position);
        holder.priceShow.setText(vehi.getPrice());
        holder.nameShow.setText(vehi.getVehicleName());
        imageLoader.displayImage(vehi.getImageUrl(), holder.vehicleShow, new ImageLoadingListener()
        {
            ProgressDialog p;
            @Override
            public void onLoadingStarted(String imageUri, View view)
            {
                p=new ProgressDialog(view.getContext());
                p.show();
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                p.dismiss();
                Toast.makeText(view.getContext(),"Loading failed",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                p.dismiss();
                holder.mainLinear.setVisibility(View.VISIBLE);
                holder.pv.stop();
                holder.pv.setVisibility(View.GONE);
                holder.vehicleShow.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });

        holder.vehicleShow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {

                        Intent in=new Intent("android.intent.action.BOOKING");
                        in.putExtra("vehicle_id",vehi.getVehicleId());
                        in.putExtra("user_id",vehi.getUserId());
                        in.putExtra("image_id",vehi.getImageId());
                        in.putExtra("image_url",vehi.getImageUrl());
                        in.putExtra("vehicle_name",vehi.getVehicleName());
                        in.putExtra("date_start",dateStart.formattedDate());
                        in.putExtra("date_end",dateEnd.formattedDate());
                        in.putExtra("vehicle_no",vehi.getVehicleNo());
                        v.getContext().startActivity(in);
                    }
                });


    }

    @Override
    public int getItemCount() {
        return vh.size();
    }
}
