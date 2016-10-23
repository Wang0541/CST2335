package com.example.yu.lab1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;

import java.net.URL;


import static java.lang.System.in;


public class WeatherForecast extends AppCompatActivity {

    TextView temp;
    TextView tempMin;
    TextView tempMax;
    ImageView weatherImage;
    public static final String LOCAL_IMAGE = "local";
    public static final String DOWNLOAD_IMAGE = "download";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        temp = (TextView)findViewById(R.id.temp_textView);
        tempMin = (TextView)findViewById(R.id.temp_min_textView);
        tempMax = (TextView)findViewById(R.id.temp_max_textView);
        weatherImage = (ImageView) findViewById(R.id.weather_imageView);

        ForecastQuery fq = new ForecastQuery();
        fq.execute();
    }

    public static Bitmap getImage(URL url) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                return BitmapFactory.decodeStream(connection.getInputStream());
            } else
                return null;
        } catch (Exception e) {
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }


    private class ForecastQuery extends AsyncTask<String,Integer,String>{

        String weatherURL = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric";
        String temperatureText=null;
        String temMinText =null;
        String temMaxText =null;
        String iconName = null;
        Bitmap image;

        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL(weatherURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();

                try {
                    XmlPullParser parser = Xml.newPullParser();
                    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    parser.setInput(conn.getInputStream(), null);
                    parser.nextTag();



                    while (parser.next() != XmlPullParser.END_DOCUMENT) {
                        if (parser.getEventType() != XmlPullParser.START_TAG) {
                            continue;
                        }
                        String name = parser.getName();

                        if (name.equals("temperature")) {
                            temperatureText = parser.getAttributeValue(null, "value");
                            SystemClock.sleep(1000);
                            publishProgress(25);
                            temMinText = parser.getAttributeValue(null,"min");
                            SystemClock.sleep(1000);
                            publishProgress(50);
                            temMaxText = parser.getAttributeValue(null,"max");
                            SystemClock.sleep(1000);
                            publishProgress(75);
                        }
                        if(name.equals("weather")){
                            iconName = parser.getAttributeValue(null,"icon");
                            String imageURL_Str = "http://openweathermap.org/img/w/" + iconName + ".png";
                            String fileName = iconName + ".png";

                            File file = getBaseContext().getFileStreamPath(fileName);
                            if(file.exists()){
                                //when this file exists
                                FileInputStream fis = null;

                                try {
                                    fis = new FileInputStream(file);
                                }catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                image = BitmapFactory.decodeStream(fis);
                                Log.i(LOCAL_IMAGE,"Found the image locally");

                            }else{
                                //download image
                                image = getImage(new URL(imageURL_Str));
                                Log.i(DOWNLOAD_IMAGE,"Can't find image locally, download it");
                                FileOutputStream outputStream = openFileOutput( iconName + ".png", Context.MODE_PRIVATE);
                                image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                outputStream.flush();
                                outputStream.close();

                            }

                            if(image != null){
                                publishProgress(100);
                            }


                        }
                    }


                } catch ( XmlPullParserException ex ){
                    return null;
                }finally {
                    in.close();
                }
                return "";

            }catch (IOException ioe){
                Log.i("IOException"," 1231231231");
                return null;
            }

        }



        @Override
        protected void onProgressUpdate(Integer... progress) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(progress[0]);

        }

        @Override
        protected void onPostExecute(String result) {
            temp.setText("Temperature = "+temperatureText);
            tempMin.setText("Minimum temperature = "+temMinText);
            tempMax.setText("Maximum temperature = "+temMaxText);
            weatherImage.setImageBitmap(image);
            progressBar.setVisibility(View.INVISIBLE);
        }


    }



}
