package com.example.fklassen.weatherapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fklassen on 22.03.2017.
 */

public class WetterActivity extends Activity {


    public static final String TAG4LOGGING = "Pupsgesicht";
    public static final String key_API = "31f9d45c5d615fabdac3b88c54c9b7b2";
    public static final String units = "metric";



    //Member Variablen
    protected TextView wetter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wetter);

        Log.i(TAG4LOGGING, "willkomemen in activity 2 ");
        wetter = (TextView) findViewById(R.id.wetter);

        Intent intent = getIntent();

        String ort = intent.getStringExtra("Ort");

        Log.i(TAG4LOGGING, "intent "+ort);
        //wetter.setText(intent.getStringExtra("Ort"));


        /**
         * *****************************************************************************************
         * ***START innere Klasse MeinTread - RUN Methode
         * *****************************************************************************************
         */

        class MeinThread extends Thread{
            protected String _ort;
            protected String jsonResponse;

            public MeinThread(String _location){
                _ort   = _location;

            }

            @Override
            public void run(){
                try{
                    Log.i(TAG4LOGGING, "Im Thread");
                    jsonResponse = holeDatenVonAPI(_ort);
                }catch(Exception e){
                    //TODO: Exception behandeln
                }

                try{
                    parseJSON(jsonResponse);
                   //Log.i(TAG4LOGGING, "Ausgabe ausgeben" + ausgabe);
                }catch(Exception e){
                    //TODO: Exception behandeln
                }
            }


            public String holeDatenVonAPI(String ort) throws Exception{

                Log.i(TAG4LOGGING, "In holeDatenVonAPI Methode");
                //Schritt 1: Request Factory holen
                HttpTransport httpTransport = new NetHttpTransport();
                HttpRequestFactory requestFactory = httpTransport.createRequestFactory();

                Log.i(TAG4LOGGING, "Schritt 1 beendet");
                //Schritt 2: URL erzeugen und Parameter hinzufügen
                //Link: api.openweathermap.org/data/2.5/weather?q=City&units=metric&APPID=31f9d45c5d615fabdac3b88c54c9b7b2
                GenericUrl url = new GenericUrl("http://api.openweathermap.org/data/2.5/weather");
                Log.i(TAG4LOGGING, "Generic Url beendet"+ url);
                url.put("q", ort);
                Log.i(TAG4LOGGING, "q " + ort);
                url.put("units", units);
                Log.i(TAG4LOGGING, "units " + units);
                url.put("APPID",key_API);
                Log.i(TAG4LOGGING, "APPID " + key_API);


                Log.i(TAG4LOGGING, "URL erzeugt: " + url);

                //Schritt 3: Absetzen des Requests
                HttpRequest httpRequest = requestFactory.buildGetRequest(url);
                Log.i(TAG4LOGGING, "httpRequest deklariert");
                HttpResponse httpResponse = httpRequest.execute();
                Log.i(TAG4LOGGING, "httpRequest execute");


                //Schritt 4: Antwort-String (JSON-Format) zurückgeben
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

            public void parseJSON(String jsonString) throws Exception {

                JSONObject jsonObject = new JSONObject(jsonString);
                JSONObject mainObj = jsonObject.getJSONObject("main");
                final Double temp = mainObj.getDouble("temp");

                Log.i(TAG4LOGGING, "Ich bin am parsen "+temp);
                //TODO: gesuchte Attribute Abfragen

                //TODO: String für die Ausgabe auf der Activity erzeugen und zur Anzeige bringen
                //wetter.setText(intent.getStringExtra("JSON"));

                wetter.post(new Runnable() {
                    @Override
                    public void run() {
                        wetter.setText(temp.toString());
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





    }

    /**
     * *****************************************************************************************
     * ***START JSON abholen
     * *****************************************************************************************
     */


}
