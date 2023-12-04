package com.example.proyectusfinal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Agrega un temporizador para simular la espera de 5 segundos
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Abre la actividad principal después de 5 segundos
                openMainActivity();
            }
        }, 5000);
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();  // Finaliza la actividad actual para que el usuario no pueda volver al splash screen
    }
}