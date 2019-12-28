package com.kkw.bakingapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BakingService extends IntentService {
    public static final String ACTION_EXTRACT_INGRE = "com.kkw.baking";

    public BakingService() {
        super("BakingService");
    }


    public static void startExtract(Context context) {
        Intent intent = new Intent(context, BakingService.class);
        intent.setAction(ACTION_EXTRACT_INGRE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_EXTRACT_INGRE.equals(action)) {
                handleActionExtract();
            }
        }
    }

    public void handleActionExtract() {
        List<String> textList = new ArrayList<String>();
        SharedPreferences sharedPreferences= getSharedPreferences("FIR",MODE_PRIVATE);
        int size = sharedPreferences.getInt("ingredients_size", 0);


        for(int i=0;i<size;i++)
        {
           textList.add(sharedPreferences.getString("Ingredient_" + i, null));
           textList.add(sharedPreferences.getString("quantity_" + i, null));
            textList.add(sharedPreferences.getString("measure_" + i, null));
            textList.add("\n");


        }
        String formatIngredientList = textList.toString()
                .replace(",", "")
                .replace("[", "")
                .replace("]", "");


        RemoteViews view = new RemoteViews(getPackageName(), R.layout.baking_app_widget);
        view.setTextViewText(R.id.ingredient_list, formatIngredientList);

        ComponentName theWidget = new ComponentName(this, BakingAppWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        manager.updateAppWidget(theWidget, view);


    }


}
