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

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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


        verFormateoDatos();
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

    private void verFormateoDatos(){

        //Formateo de Números con Separadores de Miles
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        String formattedNumber = numberFormat.format(1234567.89); // Formateará a "1,234,567.89" dependiendo de la configuración regional
        TextView numberView = (TextView) findViewById(R.id.numberTextView);
        numberView.setText(formattedNumber);


        //Formateo de Porcentajes
        NumberFormat percentFormat = NumberFormat.getPercentInstance();
        String formattedPercentage = percentFormat.format(0.85); // Formateará a "85%" dependiendo de la configuración regional
        TextView percentageView = (TextView) findViewById(R.id.percentageTextView);
        percentageView.setText(formattedPercentage);


        //Formateo de Fechas en un Formato Personalizado
        SimpleDateFormat customDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = customDateFormat.format(new Date()); // Ejemplo: "30/01/2025"
        TextView dateView = (TextView) findViewById(R.id.customDateTextView);
        dateView.setText(formattedDate);


        //Formateo de Fechas con Hora
        DateFormat dateTimeFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT);
        String formattedDateTime = dateTimeFormat.format(new Date()); // Ejemplo: "January 30, 2025 12:30 PM"
        TextView dateTimeView = (TextView) findViewById(R.id.dateTimeTextView);
        dateTimeView.setText(formattedDateTime);


        //Formateo de Moneda en una Configuración Regional Específica
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        String formattedCurrency = currencyFormat.format(1234.56); // Ejemplo: "$1,234.56"
        TextView currencyView = (TextView) findViewById(R.id.currencyTextView);
        currencyView.setText(formattedCurrency);


        //Formateo de Hora en 24 horas
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String formattedTime = timeFormat.format(new Date()); // Ejemplo: "14:30"
        TextView timeView = (TextView) findViewById(R.id.timeTextView);
        timeView.setText(formattedTime);

    }
}