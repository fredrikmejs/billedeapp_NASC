package com.example.billedeapp_nasc.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.billedeapp_nasc.Logic.SaveData;
import com.example.billedeapp_nasc.R;


public class Settings_Fragment extends Fragment {

    private SaveData saveData = new SaveData();
    private EditText editText_TruckNumber, editText_Message, editText_mailAddress, Text_password;
    private String mailAddress, truckNumber, mailMessage;
    private Button ok, cancel;
    private Button save, back;
    private final String password = "SWEA";
    private boolean passOK = false;

    public Settings_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings_, container, false);

        editText_mailAddress = view.findViewById(R.id.edittext_mailAddress);
        editText_Message = view.findViewById(R.id.editText_MailMessage);
        editText_TruckNumber = view.findViewById(R.id.edit_settingTruckNumber);

        if (saveData.loadData(getContext(),"mail_address") != null) {
            mailAddress = saveData.loadData(getContext(),"mail_address");
            editText_mailAddress.setText(mailAddress);
        }

        if (saveData.loadData(getContext(),"mail_message") != null){
            mailMessage = saveData.loadData(getContext(),"mail_message");
            editText_Message.setText(mailMessage);
        }
        if (saveData.loadData(getContext(), "TruckNumber") != null){
            truckNumber = saveData.loadData(getContext(), "TruckNumber");
            editText_TruckNumber.setText(truckNumber);
        }

        back = view.findViewById(R.id.button_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.container, new Take_pictureFragment()).addToBackStack(null).commit();
            }
        });

        save = view.findViewById(R.id.button_saveSettings);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean passReq = false;
                if (!mailAddress.equals(editText_mailAddress.getText().toString())){
                    passReq = true;
                }
                if (!mailMessage.equals(editText_Message.getText().toString())){
                    passReq = true;
                }
                if (!truckNumber.equals(editText_TruckNumber.getText().toString())){
                    saveData.updateData(getContext(),"TruckNumber",editText_TruckNumber.getText().toString());
                }

                if (passReq){
                    showDialog();
                } else{
                    getFragmentManager().beginTransaction().replace(R.id.container, new Take_pictureFragment()).addToBackStack(null).commit();
                }


            }
        });
        return view;
    }



    private void showDialog(){
        AlertDialog.Builder alert;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alert = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
        }
        else  {
            alert = new AlertDialog.Builder(getContext());
        }

        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.password, null);

        Text_password = view.findViewById(R.id.password);
        ok = view.findViewById(R.id.button_passOK);
        cancel = view.findViewById(R.id.button_cancel);

        alert.setView(view);

        final AlertDialog dialog = alert.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredPasswrod = Text_password.getText().toString();
                if (enteredPasswrod.equals(password)){
                    saveData.updateData(getContext(),"mail_message", editText_Message.getText().toString());
                    saveData.updateData(getContext(),"mail_address", editText_mailAddress.getText().toString());
                    Toast.makeText(getContext(),"Ændringer gemt", Toast.LENGTH_LONG).show();
                    getFragmentManager().beginTransaction().replace(R.id.container, new Take_pictureFragment()).addToBackStack(null).commit();
                    dialog.dismiss();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}