package com.example.fklassen.weatherapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {

    //Member-Variablen
    public static final String TAG4LOGGING = "Note";
    protected EditText input;
    protected Button abschicken = null;
    protected Toast toast = null;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Member-Variablen füllen
        input = (EditText) findViewById(R.id.eingabe);
        abschicken = (Button) findViewById(R.id.weiter);

        //Funktion für den Button einfügen
        abschicken.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        intent = new Intent(this, WetterActivity.class);

        String text;
        int duration = Toast.LENGTH_SHORT;

        //Toast declaration if empty

        if (v.getId() == abschicken.getId()) {
            if (input.getText().toString().equals("")) {
                text = "Sorry, du hast nichts eingegeben";
                toast = Toast.makeText(this, text, duration);
                toast.show();
                return;
            }
        }

        //Werte auslesen und an den Intent übergeben

        intent.putExtra("Ort", input.getText().toString());

        Log.i(TAG4LOGGING, "input wurde weitergegeben " + input.getText());

        startActivity(intent);
    }
}
