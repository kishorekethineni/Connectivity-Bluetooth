package com.example.bluetooth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class conectivity extends AppCompatActivity {
    ConnectivityManager connectivityManager;
    static ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conectivity);
        iv = findViewById(R.id.image);
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);


    }

    public void download(View view) {
        String imagepath = "https://placeimg.com/640/360";
        String textpath = "https://drive.google.com/open?id=1b3lYX1FMnDBzYp0X6Io6P-WPhqnr_Gja";
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                Toast.makeText(this, "WI FI", Toast.LENGTH_SHORT).show();
                new MyTextTask().execute(textpath);
                new MyImageTask().execute(imagepath);
            }

            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                Toast.makeText(this, "Mobile", Toast.LENGTH_SHORT).show();
                new MyTextTask().execute(textpath);
                new MyImageTask().execute(imagepath);

            }
        }
    }

    class MyImageTask extends AsyncTask<String, Void, Bitmap> {


        @Override
        protected Bitmap doInBackground(String... strings) {
            return downloadImage(strings[0]);

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                conectivity.iv.setImageBitmap(bitmap);
                conectivity.iv.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }

        private Bitmap downloadImage(String string) {

            Bitmap bitmap = null;
            try {
                URL url = new URL(string);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);

                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoInput(true);

                httpURLConnection.connect();
                int code = httpURLConnection.getResponseCode();
                if (code == HttpURLConnection.HTTP_OK) {
                    InputStream stream = httpURLConnection.getInputStream();
                    if (stream != null) {
                        bitmap = BitmapFactory.decodeStream(stream);
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
    }

    class MyTextTask extends AsyncTask<String, Void, String> {
        //TextView tv;
        @Override
        protected String doInBackground(String... strings) {
            return downLoadText(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null)
                Log.d("TAG", s);

        }

        String downLoadText(String path) {
           // tv=findViewById(R.id.textView);
            String text = null;
            try {
                URL url = new URL(path);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(3000);
                httpURLConnection.setReadTimeout(3000);
                httpURLConnection.setRequestMethod("GET");

                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                int code = httpURLConnection.getResponseCode();
                if (code == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    if (inputStream != null) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder sb = new StringBuilder();
                        String line = "";
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }

                        text = sb.toString();
                       // tv.setText(text);

                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return text;

        }
    }
}
