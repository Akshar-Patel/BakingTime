package com.example.ab.bakingtime.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import com.example.ab.bakingtime.model.Recipe;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Util {

  public static final String RECIPE_LIST_KEY = "recipe_list";

  public static List<Recipe> loadFromSharedPref(Context context, String key) {
    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    Gson gson = new Gson();
    String json = sharedPrefs.getString(key, null);
    Type type = new TypeToken<ArrayList<Recipe>>() {
    }.getType();
    return gson.fromJson(json, type);
  }

  public static void saveInSharedPref(Context context, String key, List<Recipe> value) {
    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    Editor editor = sharedPrefs.edit();
    Gson gson = new Gson();
    String json = gson.toJson(value);
    editor.putString(key, json);
    editor.apply();
  }

  public static boolean isTablet(Context context) {
    boolean xlarge = ((context.getResources().getConfiguration().screenLayout
        & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE);
    boolean large = ((context.getResources().getConfiguration().screenLayout
        & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
    return (xlarge || large);
  }
}
