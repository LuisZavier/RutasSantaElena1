package inicio;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.rutas.santaelena.rutas.Home;
import com.rutas.santaelena.rutas.R;

;

public class SplashInicio extends Activity {
    private final int DURACION_SPLASH = 3000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_inicio);

        new Handler().postDelayed(new Runnable(){
            public void run(){
                Intent intent = new Intent(SplashInicio.this, Home.class);
                startActivity(intent);
                finish();
            };
        }, DURACION_SPLASH);
    }
}
