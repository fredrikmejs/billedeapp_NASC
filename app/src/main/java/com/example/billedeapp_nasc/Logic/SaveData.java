package com.example.billedeapp_nasc.Logic;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class SaveData {


    public void saveLocal(Context context, String saveName, String saveData){

        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(saveName, saveData).apply();

    }

    public String loadData(Context context, String loadName) {

        return PreferenceManager.getDefaultSharedPreferences(context).getString(loadName, null);

    }

    public void deleteData(Context context, String deleteName){
        SharedPreferences sharedPreferences = context.getSharedPreferences(deleteName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(deleteName).apply();
    }

    public void updateData(Context context, String updateName, String data){
        deleteData(context,updateName);
        saveLocal(context,updateName,data);
    }
}
