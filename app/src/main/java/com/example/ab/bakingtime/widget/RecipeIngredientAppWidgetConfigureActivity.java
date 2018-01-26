package com.example.ab.bakingtime.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import com.example.ab.bakingtime.MainActivity;
import com.example.ab.bakingtime.R;
import com.example.ab.bakingtime.model.Recipe;
import com.example.ab.bakingtime.util.Util;
import java.util.List;

/**
 * The configuration screen for the {@link RecipeIngredientAppWidget RecipeIngredientAppWidget}
 * AppWidget.
 */
public class RecipeIngredientAppWidgetConfigureActivity extends Activity {

  private static final String PREFS_NAME = "com.example.ab.bakingtime.widget.RecipeIngredientAppWidget";
  private static final String PREF_PREFIX_KEY = "appwidget_";
  public static List<Recipe> mRecipeList;
  static int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
  RecyclerView mRecyclerViewRecipes;
  RecipeRecyclerViewAdapter mRecipeRecyclerViewAdapter;
  EditText mAppWidgetText;
  View.OnClickListener mOnClickListener = new View.OnClickListener() {
    public void onClick(View v) {
      final Context context = RecipeIngredientAppWidgetConfigureActivity.this;

      // When the button is clicked, store the string locally
      String widgetText = mAppWidgetText.getText().toString();
      saveTitlePref(context, mAppWidgetId, widgetText);

      // It is the responsibility of the configuration activity to update the app widget
      AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
      RecipeIngredientAppWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

      // Make sure we pass back the original appWidgetId
      Intent resultValue = new Intent();
      resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
      setResult(RESULT_OK, resultValue);
      finish();
    }
  };

  public RecipeIngredientAppWidgetConfigureActivity() {
    super();
  }

  // Write the prefix to the SharedPreferences object for this widget
  static void saveTitlePref(Context context, int appWidgetId, String text) {
    SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
    prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
    prefs.apply();
  }

  // Read the prefix from the SharedPreferences object for this widget.
  // If there is no preference saved, get the default from a resource
  static String loadTitlePref(Context context, int appWidgetId) {
    SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
    String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
    if (titleValue != null) {
      return titleValue;
    } else {
      return context.getString(R.string.appwidget_text);
    }
  }

  static void deleteTitlePref(Context context, int appWidgetId) {
    SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
    prefs.remove(PREF_PREFIX_KEY + appWidgetId);
    prefs.apply();
  }

  void passBack() {
    Intent resultValue = new Intent();
    resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
    setResult(RESULT_OK, resultValue);
    finish();
  }

  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);

    // Set the result to CANCELED.  This will cause the widget host to cancel
    // out of the widget placement if the user presses the back button.
    setResult(RESULT_CANCELED);

    setContentView(R.layout.recipe_ingredient_app_widget_configure);
    mAppWidgetText = (EditText) findViewById(R.id.appwidget_text);
    findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

    mRecipeList = (List<Recipe>) Util
        .loadFromSharedPref(getApplicationContext(), MainActivity.RECIPE_LIST_KEY);

    mRecyclerViewRecipes = findViewById(R.id.rv_recipes_widget_config);
    mRecyclerViewRecipes
        .setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    mRecipeRecyclerViewAdapter = new RecipeRecyclerViewAdapter(this);
    mRecyclerViewRecipes.setAdapter(mRecipeRecyclerViewAdapter);

    // Find the widget id from the intent.
    Intent intent = getIntent();
    Bundle extras = intent.getExtras();
    if (extras != null) {

      mAppWidgetId = extras.getInt(
          AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    // If this activity was started with an intent without an app widget ID, finish with an error.
    if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
      finish();
      return;
    }

    mAppWidgetText
        .setText(loadTitlePref(RecipeIngredientAppWidgetConfigureActivity.this, mAppWidgetId));
  }

}

