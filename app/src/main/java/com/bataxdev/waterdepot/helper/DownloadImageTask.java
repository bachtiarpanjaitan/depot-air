package com.bataxdev.waterdepot.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

public class DownloadImageTask extends AsyncTask<String,Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage)
    {
        this.bmImage = bmImage;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        String urlDisplay = strings[0];
        Bitmap mIcon = null;

        try {
            InputStream in = new java.net.URL(urlDisplay).openStream();
            mIcon = BitmapFactory.decodeStream(in);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mIcon;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        bmImage.setImageBitmap(bitmap);
    }
}
