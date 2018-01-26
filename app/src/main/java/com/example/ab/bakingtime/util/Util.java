package com.example.ab.bakingtime.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Util {

  public static List<?> loadFromSharedPref(Context context, String key) {
    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    Gson gson = new Gson();
    String json = sharedPrefs.getString(key, null);
    Type type = new TypeToken<ArrayList<?>>() {
    }.getType();
    return gson.fromJson(json, type);
  }

  public static void saveInSharedPref(Context context, String key, List<?> value) {
    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    Editor editor = sharedPrefs.edit();
    Gson gson = new Gson();
    String json = gson.toJson(value);
    editor.putString(key, json);
    editor.apply();
  }
}
