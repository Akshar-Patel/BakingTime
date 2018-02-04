package com.example.ab.bakingtime.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.ab.bakingtime.R;
import com.example.ab.bakingtime.model.Ingredient;
import com.example.ab.bakingtime.model.Recipe;
import com.example.ab.bakingtime.util.Util;
import java.util.List;

class RecipeRecyclerViewAdapter extends
    RecyclerView.Adapter<RecipeRecyclerViewAdapter.ViewHolder> {

  private final List<Recipe> mRecipeList;
  private Activity mActivity;
  private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      int recipeId = (int) view.getTag();
      Pref.savePrefRecipeName(view.getContext(),
          RecipeIngredientAppWidgetConfigureActivity.mAppWidgetId,
          mRecipeList.get(recipeId).getName());

      List<Ingredient> ingredientList = mRecipeList.get(recipeId).getIngredientList();
      StringBuilder sb = new StringBuilder();
      int count = 1;
      for (Ingredient ingredient : ingredientList) {
        String ingredientName = ingredient.getName().toLowerCase();
        Float ingredientQuantity = ingredient.getQuantity();
        String ingredientMeasure = ingredient.getMeasure().toLowerCase();

        String[] strings = ingredientName.split(" ");
        sb.append(count++);
        sb.append(". ");
        for (String s : strings) {
          sb.append(s.substring(0, 1).toUpperCase());
          sb.append(s.substring(1));
          sb.append(" ");
        }
        sb.append(" ").append(ingredientQuantity).append(" ").append(ingredientMeasure);
        sb.append("\n");
      }

      Pref.savePrefIngredientList(view.getContext(),
          RecipeIngredientAppWidgetConfigureActivity.mAppWidgetId, sb.toString());

      // It is the responsibility of the configuration activity to update the app widget
      AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(view.getContext());
      RecipeIngredientAppWidget
          .updateAppWidget(view.getContext(), appWidgetManager,
              RecipeIngredientAppWidgetConfigureActivity.mAppWidgetId);

      // Make sure we pass back the original appWidgetId
      Intent resultValue = new Intent();
      resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
          RecipeIngredientAppWidgetConfigureActivity.mAppWidgetId);
      mActivity.setResult(Activity.RESULT_OK, resultValue);
      mActivity.finish();
    }
  };

  RecipeRecyclerViewAdapter(Activity activity) {
    mActivity = activity;
    mRecipeList = Util
        .loadFromSharedPref(mActivity, Util.RECIPE_LIST_KEY);
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.widget_config_activity_recipe_ingredient_list_item, parent,
            false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    holder.mRecipeNameTextView.setText(
        mRecipeList.get(position).getName());
    holder.itemView.setOnClickListener(mOnClickListener);
    holder.itemView.setTag(position);
  }

  @Override
  public int getItemCount() {
    if (mRecipeList != null) {
      return mRecipeList.size();
    }
    return 0;
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    TextView mRecipeNameTextView;

    public ViewHolder(View itemView) {
      super(itemView);
      mRecipeNameTextView = itemView.findViewById(R.id.tv_recipe_name_widget_config);
    }
  }
}
