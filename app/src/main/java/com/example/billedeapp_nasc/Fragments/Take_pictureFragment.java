package com.example.billedeapp_nasc.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.billedeapp_nasc.Logic.SaveData;
import com.example.billedeapp_nasc.R;
import com.example.billedeapp_nasc.Logic.SendPicture;
import com.example.billedeapp_nasc.Logic.ShowCamera;


public class Take_pictureFragment extends Fragment {
    private Camera camera;
    private Button button_takePicture, button_sendPicture;
    private ImageButton buttonSettings;
    private FrameLayout frameLayout_camera, frameLayout_photo;
    private Bitmap bitmap_picture;
    boolean isPictureTaken = false;
    private TextView truckNumberShow;
    private SaveData saveData = new SaveData();

    public Take_pictureFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_take_picture, container, false);

        frameLayout_camera = view.findViewById(R.id.camera);
        frameLayout_camera.setVisibility(View.VISIBLE);
        frameLayout_photo = view.findViewById(R.id.show_picture);
        frameLayout_photo.setVisibility(View.INVISIBLE);

        startCamera();

        truckNumberShow = view.findViewById(R.id.textView_showTruckNumber);


        truckNumberShow.setText("Bil " + saveData.loadData(getContext(), "TruckNumber"));

        button_sendPicture = view.findViewById(R.id.send_picture);
        button_sendPicture.setVisibility(View.INVISIBLE);

        button_sendPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendPicture sendPicture = new SendPicture(bitmap_picture, getContext());
                Intent emailIntent = sendPicture.sendPicture();

                if (Build.VERSION.SDK_INT >= 24) {
                    startActivity(Intent.createChooser(emailIntent, "send your emil in:"));
                }
            }
        });

        button_takePicture = view.findViewById(R.id.take_picture);
        button_takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (camera != null && !isPictureTaken){
                    camera.takePicture(null,null,mPictureCallback);
                    button_takePicture.setText("Retake picture");
                    isPictureTaken = true;
                    frameLayout_camera.setVisibility(View.INVISIBLE);
                    frameLayout_photo.setVisibility(View.VISIBLE);
                    button_sendPicture.setVisibility(View.VISIBLE);
                } else {
                    button_takePicture.setText("Ta kort");
                    isPictureTaken = false;
                    startCamera();
                    frameLayout_camera.setVisibility(View.VISIBLE);
                    frameLayout_photo.setVisibility(View.INVISIBLE);
                    button_sendPicture.setVisibility(View.INVISIBLE);
                }
            }
        });

        buttonSettings = view.findViewById(R.id.settings);
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.container, new Settings_Fragment()).addToBackStack(null).commit();
            }
        });
        return view;
    }

    private void startCamera(){
        camera = Camera.open();
        ShowCamera showCamera = new ShowCamera(getContext(), camera);
        frameLayout_camera.addView(showCamera);
    }

    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            bitmap_picture = BitmapFactory.decodeByteArray(data, 0, data.length);
            Drawable d = new BitmapDrawable(getResources(), bitmap_picture);
            frameLayout_photo.setForeground(d);
            if (camera != null) {
                camera.release();
                frameLayout_camera.removeAllViews();
            }

        }
    };
}