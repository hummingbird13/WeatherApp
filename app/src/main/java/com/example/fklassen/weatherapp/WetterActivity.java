package com.example.fklassen.weatherapp;

import android.app.Activity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wetter);

        Log.i(TAG4LOGGING, "Willkommen in activity 2 ");

        wetter = (TextView) findViewById(R.id.wetter);
        beschreibung = (TextView) findViewById(R.id.beschreibung);
        icon = (ImageView) findViewById(R.id.icon);


        Intent intent = getIntent();
        String ort = intent.getStringExtra("Ort");

        Log.i(TAG4LOGGING, "intent "+ ort);

        beschreibung.setText(ort.toString());
        //icon.setText(intent.getStringExtra("Ort"));



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
                //In ein try packen weil es sonst sein kann dass du mit ner Null-Referenz weiter arbeitest und das dann sicher ne Exception gibt
                
                try{
                    Log.i(TAG4LOGGING, "Im Thread");
                    jsonResponse = holeDatenVonAPI(_ort);
                    iconSetzen(parseJSON(jsonResponse));

                    Log.i(TAG4LOGGING, "Icon gesetzt");

                }catch(Exception e){
                    //TODO: Exception behandeln
                    Log.e("App_Err",Log.getStackTraceString(e));
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

            public String parseJSON(String jsonString) throws Exception {

                //Temperatur holen
                JSONObject jsonObject = new JSONObject(jsonString);



                JSONObject mainObj = jsonObject.getJSONObject("main");
                final Integer temp = mainObj.getInt("temp");

                JSONArray weatherArray = jsonObject.getJSONArray("weather");
                JSONObject weatherObj = weatherArray.getJSONObject(0);
                final String icon = weatherObj.getString("icon");

                Log.i(TAG4LOGGING, "Icon ID: "+ icon);


                Log.i(TAG4LOGGING, "Ich bin am parsen "+temp);
                //TODO: gesuchte Attribute Abfragen

                //TODO: String für die Ausgabe auf der Activity erzeugen und zur Anzeige bringen

                wetter.post(new Runnable() {
                    @Override
                    public void run() {
                        wetter.setText(temp.toString() + "°C");

                    }
                });
                return icon;



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
     * ***START Icons abrufen und einbinden
     * *****************************************************************************************
     */


    //Icon-Code holen


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
                icon.setImageResource(R.drawable.x01d);
                break;
            case "10d":
                icon.setImageResource(R.drawable.x01d);
                break;
            case "10n":
                icon.setImageResource(R.drawable.x01d);
                break;
            case "11d":
                icon.setImageResource(R.drawable.x01d);
                break;
            case "11n":
                icon.setImageResource(R.drawable.x01d);
                break;
            case "13d":
                icon.setImageResource(R.drawable.x01d);
                break;
            case "13n":
                icon.setImageResource(R.drawable.x01d);
                break;
            case "50d":
                icon.setImageResource(R.drawable.x01d);
                break;
            case "50n":
                icon.setImageResource(R.drawable.x01d);
                break;
            default:
                //Was passiert, wenn kein Code kommt?
                icon.setImageResource(R.drawable.x01d);
                break;
        }
    }


    /**
     * *****************************************************************************************
     * ***Ende Icons abrufen und einbinden
     * *****************************************************************************************
     * */


}
