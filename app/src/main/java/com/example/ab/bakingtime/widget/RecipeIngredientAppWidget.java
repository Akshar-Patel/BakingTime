package com.example.ab.bakingtime.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;
import com.example.ab.bakingtime.R;

/**
 * Implementation of App Widget functionality. App Widget Configuration implemented in {@link
 * RecipeIngredientAppWidgetConfigureActivity RecipeIngredientAppWidgetConfigureActivity}
 */
public class RecipeIngredientAppWidget extends AppWidgetProvider {

  static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
      int appWidgetId) {

    String recipeText = RecipeIngredientAppWidgetConfigureActivity
        .loadTitlePref(context, appWidgetId);

    Log.d("recipe", recipeText);

    String widgetText = RecipeIngredientAppWidgetConfigureActivity
        .loadIngredientsPref(context, appWidgetId);
    // Construct the RemoteViews object
    RemoteViews views = new RemoteViews(context.getPackageName(),
        R.layout.recipe_ingredient_app_widget);
    views.setTextViewText(R.id.appwidget_text, widgetText);
    views.setTextViewText(R.id.textViewRecipeName, recipeText);

    // Instruct the widget manager to update the widget
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
      RecipeIngredientAppWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
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

