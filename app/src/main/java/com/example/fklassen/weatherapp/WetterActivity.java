package com.example.fklassen.weatherapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.repackaged.com.google.common.base.Throwables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Created by fklassen on 22.03.2017.
 */

public class WetterActivity extends Activity {


    public static final String TAG4LOGGING = "Pupsgesicht";
    public static final String key_API = "31f9d45c5d615fabdac3b88c54c9b7b2";
    public static final String units = "metric";



    //Member Variablen
    protected TextView wetter;
    protected TextView beschreibung;
    protected ImageView icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wetter);

        Log.i(TAG4LOGGING, "Willkommen in activity 2");

        wetter = (TextView) findViewById(R.id.wetter);
        beschreibung = (TextView) findViewById(R.id.beschreibung);
        icon = (ImageView) findViewById(R.id.icon);


        Intent intent = getIntent();
        String ort = intent.getStringExtra("Ort");

        Log.i(TAG4LOGGING, "intent " + ort);

        /**
         * *****************************************************************************************
         * ***START innere Klasse MeinTread - RUN Methode
         * *****************************************************************************************
         */

        class MeinThread extends Thread {
            protected String _ort;
            protected String jsonResponse;
            protected String _iconID;

            public MeinThread(String _location) {
                _ort = _location;

            }

            @Override
            public void run() {
                try {
                    Log.i(TAG4LOGGING, "Im Thread");
                    jsonResponse = holeDatenVonAPI(_ort);
                    parseJSON(jsonResponse);

                    Log.i(TAG4LOGGING, "Icon gesetzt" + icon);

                } catch (Exception e) {

                    //TODO: Hier die Internetverbindung abfragen, bzw. behandeln?
                    Log.e("App_Err", Log.getStackTraceString(e));
                    Log.i(TAG4LOGGING, "catch Block");
                    //iconSetzen("internet");
                    wetter.post(new Runnable() {
                        @Override
                        public void run() {
                            wetter.setText("Bitte prüfen Sie ihre Internetverbindung");
                            iconSetzen("internet");
                        }
                    });

                }
            }
            /**
             * *****************************************************************************************
             * ***Start JSON abholen
             * *****************************************************************************************
             */

            public String holeDatenVonAPI(String ort) throws Exception {

                Log.i(TAG4LOGGING, "In holeDatenVonAPI Methode");

                //Schritt 1: Request Factory holen
                HttpTransport httpTransport = new NetHttpTransport();
                HttpRequestFactory requestFactory = httpTransport.createRequestFactory();

                Log.i(TAG4LOGGING, "Schritt 1 beendet");

                //Schritt 2: URL erzeugen und Parameter hinzufÃ¼gen
                //Link: api.openweathermap.org/data/2.5/weather?q=City&units=metric&APPID=31f9d45c5d615fabdac3b88c54c9b7b2

                GenericUrl url = new GenericUrl("http://api.openweathermap.org/data/2.5/weather");
                Log.i(TAG4LOGGING, "Generic Url beendet" + url);
                url.put("q", ort);
                Log.i(TAG4LOGGING, "q " + ort);
                url.put("units", units);
                Log.i(TAG4LOGGING, "units " + units);
                url.put("APPID", key_API);
                Log.i(TAG4LOGGING, "APPID " + key_API);
                Log.i(TAG4LOGGING, "URL erzeugt: " + url);

                //Schritt 3: Absetzen des Requests
                HttpRequest httpRequest = requestFactory.buildGetRequest(url);
                Log.i(TAG4LOGGING, "httpRequest deklariert");
                HttpResponse httpResponse = httpRequest.execute();
                Log.i(TAG4LOGGING, "httpRequest execute" + httpRequest);


                //Schritt 4: Antwort-String (JSON-Format) zurÃ¼ckgeben
                String jsonResponseString = httpResponse.parseAsString();

                Log.i(TAG4LOGGING, "JSON-String erhalten: " + jsonResponseString);

                return jsonResponseString;
            }

            /**
             * *****************************************************************************************
             * ***ENDE JSON abholen
             * *****************************************************************************************
             */


            /**
             * *****************************************************************************************
             * ***START JSON parsen
             * *****************************************************************************************
             */

            public void parseJSON(final String jsonString) throws Exception {

                //cod holen
                //JSONObject jsonObject = new JSONObject(jsonString);
                //final Integer status = jsonObject.getInt("cod");

                wetter.post(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            JSONObject jsonObject = new JSONObject(jsonString);
                            final Integer status = jsonObject.getInt("cod");

                            if (status != 200) {
                                _iconID = "Fehler!";
                                iconSetzen(_iconID);
                                beschreibung.setText("Stadt nicht gefunden");
                            } else {
                                final String name = jsonObject.getString("name");

                                JSONObject mainObj = jsonObject.getJSONObject("main");
                                final Integer temp = mainObj.getInt("temp");

                                JSONArray weatherArray = jsonObject.getJSONArray("weather");
                                JSONObject weatherObj = weatherArray.getJSONObject(0);

                                _iconID = weatherObj.getString("icon");

                                Log.i(TAG4LOGGING, "Icon ID: " + _iconID);
                                Log.i(TAG4LOGGING, "Ich bin am parsen " + temp);

                                iconSetzen(_iconID);
                                wetter.setText(temp.toString() + "°C");
                                beschreibung.setText(name);
                            }
                        } catch (Exception e) {
                        }

                    }
                });
            }

            /**
             * *****************************************************************************************
             * ***ENDE JSON parsen
             * *****************************************************************************************
             */

            //
        }

        /*
         * *****************************************************************************************
         * ***ENDE innere Klasse MeinTread
         * *****************************************************************************************
         */

        //Thread starten, in dem der http Request abgesetzt wird
        MeinThread meinThread = new MeinThread(intent.getStringExtra("Ort"));
        meinThread.start();


        /**
         * *****************************************************************************************
         * ***START Icons abrufen und einbinden
         * *****************************************************************************************
         */

    }

    public void iconSetzen(String i) {
        switch (i) {
            case "01d":
                icon.setImageResource(R.drawable.x01d);
                break;
            case "01n":
                icon.setImageResource(R.drawable.x01n);
                break;
            case "02d":
                icon.setImageResource(R.drawable.x02d);
                break;
            case "02n":
                icon.setImageResource(R.drawable.x02n);
                break;
            case "03d":
                icon.setImageResource(R.drawable.x03d);
                break;
            case "03n":
                icon.setImageResource(R.drawable.x03n);
                break;
            case "04d":
                icon.setImageResource(R.drawable.x04d);
                break;
            case "04n":
                icon.setImageResource(R.drawable.x04n);
                break;
            case "09d":
                icon.setImageResource(R.drawable.x09d);
                break;
            case "09n":
                icon.setImageResource(R.drawable.x09n);
                break;
            case "10d":
                icon.setImageResource(R.drawable.x10d);
                break;
            case "10n":
                icon.setImageResource(R.drawable.x10n);
                break;
            case "11d":
                icon.setImageResource(R.drawable.x11d);
                break;
            case "11n":
                icon.setImageResource(R.drawable.x11n);
                break;
            case "13d":
                icon.setImageResource(R.drawable.x13d);
                break;
            case "13n":
                icon.setImageResource(R.drawable.x13n);
                break;
            case "50d":
                icon.setImageResource(R.drawable.x50d);
                break;
            case "50n":
                icon.setImageResource(R.drawable.x50n);
                break;
            case "internet":
                icon.setImageResource(R.drawable.ni);
            default:
                icon.setImageResource(R.drawable.q);
                break;
        }
    }

    /**
     * *****************************************************************************************
     * ***Ende Icons abrufen und einbinden
     * *****************************************************************************************
     * */


}
