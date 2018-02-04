package com.example.ab.bakingtime.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.ab.bakingtime.R;
import com.example.ab.bakingtime.model.Recipe;
import com.example.ab.bakingtime.util.Util;
import java.util.List;

/**
 * The configuration screen for the {@link RecipeIngredientAppWidget RecipeIngredientAppWidget}
 * AppWidget.
 */
public class RecipeIngredientAppWidgetConfigureActivity extends Activity {

  static int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
  public List<Recipe> mRecipeList;
  RecyclerView mRecyclerViewRecipes;
  RecipeRecyclerViewAdapter mRecipeRecyclerViewAdapter;

  public RecipeIngredientAppWidgetConfigureActivity() {
    super();
  }

  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);

    // Set the result to CANCELED.  This will cause the widget host to cancel
    // out of the widget placement if the user presses the back button.
    setResult(RESULT_CANCELED);

    setContentView(R.layout.widget_config_activity_recipe_ingredient_list);

    mRecipeList = Util
        .loadFromSharedPref(getApplicationContext(), Util.RECIPE_LIST_KEY);

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
    }
  }

}

