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
import com.example.ab.bakingtime.model.Ingredient;
import java.util.List;

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

      List<Ingredient> ingredientList = RecipeIngredientAppWidgetConfigureActivity.mRecipeList
          .get(recipeId).getIngredientList();
      StringBuilder sb = new StringBuilder();
      int count = 1;
      for (Ingredient ingredient : ingredientList) {
        String string = ingredient.getName().toLowerCase();
        String[] strings = string.split(" ");

        sb.append(count++);
        sb.append(". ");
        for (String s : strings) {

          sb.append(s.substring(0, 1).toUpperCase());
          sb.append(s.substring(1));
          sb.append(" ");
        }
        //sb.append(string);
        sb.append("\n");
      }
      RecipeIngredientAppWidgetConfigureActivity.saveIngredientsPref(view.getContext(),
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
