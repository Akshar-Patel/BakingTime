package com.example.ab.bakingtime.widget;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.ab.bakingtime.R;

class Pref {

  private static final String PREF_NAME = "com.example.ab.bakingtime.widget.RecipeIngredientAppWidget";
  private static final String PREF_KEY_RECIPE_KEY = "recipe_name";
  private static final String PREF_KEY_INGREDIENT_LIST = "ingredient_list";

  static void savePrefRecipeName(Context context, int appWidgetId, String text) {
    SharedPreferences.Editor prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        .edit();
    prefs.putString(PREF_KEY_RECIPE_KEY + appWidgetId, text);
    prefs.apply();
  }

  static String loadPrefRecipeName(Context context, int appWidgetId) {
    SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    String titleValue = prefs.getString(PREF_KEY_RECIPE_KEY + appWidgetId, null);
    if (titleValue != null) {
      return titleValue;
    } else {
      return context.getString(R.string.app_widget_text);
    }
  }

  static void deletePrefRecipeName(Context context, int appWidgetId) {
    SharedPreferences.Editor prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        .edit();
    prefs.remove(PREF_KEY_RECIPE_KEY + appWidgetId);
    prefs.apply();
  }

  static void savePrefIngredientList(Context context, int appWidgetId,
      String stringIngredientsList) {
    SharedPreferences.Editor prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        .edit();
    prefs.putString(PREF_KEY_INGREDIENT_LIST + appWidgetId, stringIngredientsList);
    prefs.apply();
  }

  static String loadPrefIngredientList(Context context, int appWidgetId) {
    SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    String stringIngredientsList = prefs.getString(PREF_KEY_INGREDIENT_LIST + appWidgetId, null);
    if (stringIngredientsList != null) {
      return stringIngredientsList;
    } else {
      return context.getString(R.string.app_widget_text);
    }
  }

  static void deletePrefIngredientList(Context context, int appWidgetId) {
    SharedPreferences.Editor prefs = context.getSharedPreferences(PREF_NAME, 0).edit();
    prefs.remove(PREF_KEY_INGREDIENT_LIST + appWidgetId);
    prefs.apply();
  }
}
