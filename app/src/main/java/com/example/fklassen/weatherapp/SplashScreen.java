package com.example.fklassen.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by fklassen on 22.03.2017.
 */

public class SplashScreen extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstancesState){
        super.onCreate(savedInstancesState);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}


