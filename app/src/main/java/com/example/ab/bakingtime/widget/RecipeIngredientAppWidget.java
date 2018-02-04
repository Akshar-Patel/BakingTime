package com.example.ab.bakingtime.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;
import com.example.ab.bakingtime.R;

public class RecipeIngredientAppWidget extends AppWidgetProvider {

  static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
      int appWidgetId) {

    String stringRecipeName = Pref
        .loadPrefRecipeName(context, appWidgetId);

    String stringIngredientList = Pref
        .loadPrefIngredientList(context, appWidgetId);

    RemoteViews views = new RemoteViews(context.getPackageName(),
        R.layout.widget_recipe_ingredient);
    views.setTextViewText(R.id.textViewRecipeName, stringRecipeName);
    views.setTextViewText(R.id.appwidget_text, stringIngredientList);

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
  public void onDeleted(Context context, int[] appWidgetIds) {
    // When the user deletes the widget, delete the preference associated with it.
    for (int appWidgetId : appWidgetIds) {
      Pref.deletePrefRecipeName(context, appWidgetId);
      Pref.deletePrefIngredientList(context, appWidgetId);
    }
  }
}

