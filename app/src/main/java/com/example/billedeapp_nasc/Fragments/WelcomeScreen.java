package com.example.billedeapp_nasc.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.billedeapp_nasc.Logic.SaveData;
import com.example.billedeapp_nasc.R;

public class WelcomeScreen extends Fragment {

    private Button button_continue;
    private EditText truckNumber;
    private SaveData saveData = new SaveData();

    public WelcomeScreen() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        if (saveData.loadData(getContext(),"mail_address") == null) {
            saveData.saveLocal(getContext(), "mail_address", "Fredrik200619@hotmail.com");
        }

        if (saveData.loadData(getContext(),"mail_message") == null) {
            saveData.saveLocal(getContext(), "mail_message", "");
        }

        if (saveData.loadData(getContext(), "TruckNumber") != null){
            getFragmentManager().beginTransaction().replace(R.id.container, new Take_pictureFragment()).addToBackStack(null).commit();
        }


        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, 100);
        }
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome_screen, container, false);

        button_continue = view.findViewById(R.id.button_welcomePage);

        button_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (truckNumber.length() == 0){
                    Toast.makeText(getContext(), "Bil nummer saknas", Toast.LENGTH_LONG).show();
                } else {
                    String truckNum = truckNumber.getText().toString();
                    saveData.saveLocal(getContext(), "TruckNumber", truckNum);
                    getFragmentManager().beginTransaction().replace(R.id.container, new Take_pictureFragment()).addToBackStack(null).commit();
                }
            }
        });

        truckNumber = view.findViewById(R.id.edittext_truckNumber);


        return view;
    }
}