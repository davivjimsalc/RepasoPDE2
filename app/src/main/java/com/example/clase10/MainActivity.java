package com.example.clase10;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements SensorEventListener {


    private SensorManager sensorManager;
    private Sensor accelerometer;
    private TextView textViewX, textViewY, textViewZ;
    private float x, y, z;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Enlazar con los TextView
        textViewX = findViewById(R.id.textViewX);
        textViewY = findViewById(R.id.textViewY);
        textViewZ = findViewById(R.id.textViewZ);

        // Inicializar el SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        // Registrar el listener del sensor
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }

    }


    public void botonesFragment (View view){
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (view.getId() == R.id.button) {
            manejarFragments(fragmentManager, new Fragment1(), "Fragment1");
        } else if (view.getId() == R.id.button2) {
            manejarFragments(fragmentManager, new Fragment2(), "Fragment2");
        }
    }

    private void manejarFragments(FragmentManager fragmentManager, Fragment fragment, String tag){
        Fragment existingFragment = fragmentManager.findFragmentByTag(tag);

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (existingFragment != null && existingFragment.isVisible()) {
            // Si el fragment está visible, lo ocultamos
            transaction.hide(existingFragment).commit();
        } else if (existingFragment != null) {
            // Si el fragment está en el stack pero oculto, lo mostramos
            transaction.show(existingFragment).commit();
        } else {
            // Si el fragment no existe, lo añadimos
            transaction.add(R.id.fragment_container, fragment, tag).commit();
        }
    }



    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];

            textViewX.setText("X: " + x);
            textViewY.setText("Y: " + y);
            textViewZ.setText("Z: " + z);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // No se usa en este ejemplo, pero se requiere implementar
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Detener la escucha del sensor cuando la app está en segundo plano
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reanudar la escucha del sensor cuando la app vuelve a estar activa
        if (sensorManager != null && accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    // Método para compartir los datos del sensor con otras apps
    public void compartirDatos(View view) {
        String mensaje = "Datos del sensor:\n" +
                "X: " + x + "\n" +
                "Y: " + y + "\n" +
                "Z: " + z;

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, mensaje);

        startActivity(Intent.createChooser(intent, "Compartir con"));
    }

}