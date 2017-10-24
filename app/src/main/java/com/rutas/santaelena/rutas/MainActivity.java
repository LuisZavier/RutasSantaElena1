package com.rutas.santaelena.rutas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void ver_mapa(View view) {
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        startActivity(intent);

    }


    }

   /* public void ver_ruta(View view){
        Intent intent=new Intent(MainActivity.this, RutaActivity.class);
        startActivity(intent);
    }
    public void prueba(View view){
        Intent intent=new Intent(MainActivity.this, PruebaActivity.class);
        startActivity(intent);
    }*/



