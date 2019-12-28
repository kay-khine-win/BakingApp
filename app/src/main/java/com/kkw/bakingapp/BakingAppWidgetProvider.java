package com.kkw.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import com.kkw.bakingapp.BakingService;
import com.kkw.bakingapp.data.Recipe;

import android.util.Log;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidgetProvider extends AppWidgetProvider {
    RemoteViews remoteViews;
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        views.setOnClickPendingIntent(R.id.widget_baking_image, pendingIntent);


        Intent bakingIntent = new Intent(context, BakingService.class);
        bakingIntent.setAction(BakingService.ACTION_EXTRACT_INGRE);
        PendingIntent bakingPendingIntent = PendingIntent.getService(
                context,
                0,
                bakingIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_ingre_button, bakingPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);


    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

