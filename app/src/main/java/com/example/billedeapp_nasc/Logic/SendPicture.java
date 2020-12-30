package com.example.billedeapp_nasc.Logic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.StrictMode;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SendPicture {
    private Bitmap bitmap_picture;
    private Context context;

    public SendPicture(Bitmap bitmap_picture, Context context){
        this.bitmap_picture = bitmap_picture;
        this.context = context;

    }

    public Intent sendPicture(){
        return email(context);
    }

    public static File saveImageToInternalStorage(Context context, Bitmap finalBitmap) {


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        File f = new File(context.getExternalCacheDir(), "NASC.PNG");

//Convert bitmap to byte array
        Bitmap bitmap = finalBitmap;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
        if (f.exists()){
            f.delete();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return f;
    }


    public Intent email(Context context){

            File file = saveImageToInternalStorage(context, bitmap_picture);
            SaveData saveData = new SaveData();

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {saveData.loadData(context,"mail_address")});
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Bild fra " + saveData.loadData(context, "TruckNumber"));
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, saveData.loadData(context, "mail_message"));
            emailIntent.setType("image/PNG"); // accept any image
            //attach the file to the intent
            emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

            return emailIntent;
    }
}
