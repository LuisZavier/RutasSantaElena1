package com.rutas.santaelena.rutas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnCallRest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


   /* public void ver_mapa(View view) {
        Intent intent = new Intent(MainActivity.this, MapsActivity2.class);
        startActivity(intent);

    }*/

    }
    public void ver_ruta(View view) {
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        startActivity(intent);
    }


  /*  public void prueba(View view){
        Intent intent=new Intent(MainActivity.this, PruebaActivity.class);
        startActivity(intent);
    }*/
}







