package com.example.ayush.rentalvehicles;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

/**
 * Created by Ayush on 23-04-2017.
 */
public class Image extends AppCompatActivity {

    Button cam, gal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.image_upload);

        cam = (Button)findViewById(R.id.camera_upload);
        gal = (Button)findViewById(R.id.gallery_upload);



    }
}
