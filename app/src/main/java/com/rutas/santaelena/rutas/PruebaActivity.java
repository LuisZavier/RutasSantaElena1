package com.rutas.santaelena.rutas;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import models.RestfulClient;

public class PruebaActivity extends AppCompatActivity {

    RestfulClient restfulClient = new RestfulClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba);

      //  restfulClient.ExecuteApiRest();

    }

}
