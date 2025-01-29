package com.example.clase10;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class MyWidgetProvider extends AppWidgetProvider {

    private static final String ACTION_INCREMENT = "com.example.widget.ACTION_INCREMENT";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (ACTION_INCREMENT.equals(intent.getAction())) {
            // Incrementar el contador en las SharedPreferences
            int currentCount = getCounterValue(context);
            setCounterValue(context, currentCount + 1);

            // Actualizar el widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
            if (appWidgetId != -1) {
                updateAppWidget(context, appWidgetManager, appWidgetId);
            }
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        // Obtener el valor del contador
        int counterValue = getCounterValue(context);

        // Actualizar el diseño del widget
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_widget_provider);
        views.setTextViewText(R.id.widget_counter, String.valueOf(counterValue));

        // Crear un PendingIntent para el botón "Incrementar"
        Intent intent = new Intent(context, MyWidgetProvider.class);
        intent.setAction(ACTION_INCREMENT);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, intent, PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.widget_button, pendingIntent);

        // Actualizar el widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static int getCounterValue(Context context) {
        return context.getSharedPreferences("WidgetPrefs", Context.MODE_PRIVATE)
                .getInt("counter", 0);
    }

    private static void setCounterValue(Context context, int value) {
        context.getSharedPreferences("WidgetPrefs", Context.MODE_PRIVATE)
                .edit()
                .putInt("counter", value)
                .apply();
    }
}
