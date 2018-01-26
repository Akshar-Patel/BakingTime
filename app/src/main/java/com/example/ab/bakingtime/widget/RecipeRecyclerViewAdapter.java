package com.example.ab.bakingtime.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.ab.bakingtime.R;

class RecipeRecyclerViewAdapter extends
    RecyclerView.Adapter<RecipeRecyclerViewAdapter.ViewHolder> {

  private Activity mActivity;
  private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      Log.d("click", "click");
      int recipeId = (int) view.getTag();
      RecipeIngredientAppWidgetConfigureActivity
          .saveTitlePref(view.getContext(), RecipeIngredientAppWidgetConfigureActivity.mAppWidgetId,
              RecipeIngredientAppWidgetConfigureActivity.mRecipeList.get(recipeId).getName());

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

  public RecipeRecyclerViewAdapter(Activity activity) {
    mActivity = activity;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.recipe_ingredient_app_widget_configure_recycler_view_item, parent,
            false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    holder.mRecipeNameTextView.setText(
        RecipeIngredientAppWidgetConfigureActivity.mRecipeList.get(position).getName());
    holder.itemView.setOnClickListener(mOnClickListener);
    holder.itemView.setTag(position);
    Log.d("recipe item", String.valueOf(
        RecipeIngredientAppWidgetConfigureActivity.mRecipeList.get(position).getName()));
  }

  @Override
  public int getItemCount() {
    if (RecipeIngredientAppWidgetConfigureActivity.mRecipeList != null) {
      return RecipeIngredientAppWidgetConfigureActivity.mRecipeList.size();
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
