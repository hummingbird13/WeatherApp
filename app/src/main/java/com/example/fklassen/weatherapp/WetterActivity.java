package com.example.fklassen.weatherapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class WetterActivity extends Activity {


    public static final String TAG4LOGGING = "Note";
    public static final String key_API = "31f9d45c5d615fabdac3b88c54c9b7b2";
    public static final String units = "metric";

    //Member Variablen
    protected TextView wetter;
    protected TextView beschreibung;
    protected ImageView icon;

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
                jsonResponse = holeDatenVonAPI(_ort);
                parseJSON(jsonResponse);
                Log.i(TAG4LOGGING, "Icon gesetzt" + icon);
            } catch (Exception e) {
                Log.getStackTraceString(e);
                wetter.post(new Runnable() {
                    @Override
                    public void run() {
                        wetter.setText("Da ist was schief gelaufen");
                        _iconID = "Fehler!";
                        iconSetzen(_iconID);
                    }
                });
            }
        }

        /**
         * *****************************************************************************************
         * ***Start JSON abholen
         * *****************************************************************************************
         */

        public String holeDatenVonAPI(String ort) throws IOException {

            Log.i(TAG4LOGGING, "In holeDatenVonAPI Methode");

            //Schritt 1: Request Factory holen
            HttpTransport httpTransport = new NetHttpTransport();
            HttpRequestFactory requestFactory = httpTransport.createRequestFactory();

            //Schritt 2: URL erzeugen und Parameter hinzufügen
            GenericUrl url = new GenericUrl("http://api.openweathermap.org/data/2.5/weather");
            url.put("q", ort);
            url.put("units", units);
            url.put("APPID", key_API);
            Log.i(TAG4LOGGING, "URL erzeugt: " + url);

            //Schritt 3: Absetzen des Requests
            HttpRequest httpRequest = requestFactory.buildGetRequest(url);
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

        public void parseJSON(final String jsonString) throws JSONException {

            JSONObject jsonObject = new JSONObject(jsonString);
            final Integer status = jsonObject.getInt("cod");
            final String name = jsonObject.getString("name");

            JSONObject mainObj = jsonObject.getJSONObject("main");
            final Integer temp = mainObj.getInt("temp");

            JSONArray weatherArray = jsonObject.getJSONArray("weather");
            JSONObject weatherObj = weatherArray.getJSONObject(0);

            _iconID = weatherObj.getString("icon");

            Log.i(TAG4LOGGING, "parseJSON: " + temp);


            /**
             * *****************************************************************************************
             * ***ENDE JSON parsen
             * *****************************************************************************************
             */

            //


            //UI befüllen

            wetter.post(new Runnable() {
                @Override
                public void run() {

                    iconSetzen(_iconID);
                    wetter.setText(temp.toString() + "°C");
                    beschreibung.setText(name);
                }
            });
        }

        /**
         * *****************************************************************************************
         * ***ENDE JSON parsen
         * *****************************************************************************************
         */

        /*
         * *****************************************************************************************
         * ***ENDE innere Klasse MeinTread
         * *****************************************************************************************
         */

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wetter);

        wetter = (TextView) findViewById(R.id.wetter);
        beschreibung = (TextView) findViewById(R.id.beschreibung);
        icon = (ImageView) findViewById(R.id.icon);

        Intent intent = getIntent();
        String ort = intent.getStringExtra("Ort");

        Log.i(TAG4LOGGING, "intent " + ort);

        //Thread starten, in dem der http Request abgesetzt wird
        MeinThread meinThread = new MeinThread(intent.getStringExtra("Ort"));
        meinThread.start();
    }

    /**
     * *****************************************************************************************
     * ***START Icons abrufen und einbinden
     * *****************************************************************************************
     */

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
            case "404":
                icon.setImageResource(R.drawable.ni);
                break;
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
