package com.dmims.dmims.common;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.ref.WeakReference;

public class SaveImageHelper implements Target {
    private Context context;
    private WeakReference<AlertDialog> alertDialogWeakReference;
    private WeakReference<ContentResolver> contentResolverWeakReference;
    private String name;
    private String desc;

    public SaveImageHelper(Context context,AlertDialog alertDialog, ContentResolver contentResolver, String name, String desc) {
        this.context=context;
        this.alertDialogWeakReference = new WeakReference<AlertDialog>(alertDialog);
        this.contentResolverWeakReference = new WeakReference<ContentResolver>(contentResolver);
        this.name = name;
        this.desc = desc;
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        ContentResolver resolver=contentResolverWeakReference.get();
        AlertDialog dialog=alertDialogWeakReference.get();
        if(resolver!=null)
        {
            MediaStore.Images.Media.insertImage(resolver,bitmap,name,desc);
            dialog.dismiss();

            // Code to open Gallary
            Intent intent=new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            context.startActivity(Intent.createChooser(intent,"VIEW PICTURE"));
        }

    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
}
