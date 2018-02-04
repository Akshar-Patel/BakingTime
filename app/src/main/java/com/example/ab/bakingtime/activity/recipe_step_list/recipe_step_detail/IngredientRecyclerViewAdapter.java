package com.example.ab.bakingtime.activity.recipe_step_list.recipe_step_detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.ab.bakingtime.R;
import com.example.ab.bakingtime.model.Ingredient;
import java.util.List;

class IngredientRecyclerViewAdapter extends
    RecyclerView.Adapter<IngredientRecyclerViewAdapter.ViewHolder> {

  List<Ingredient> mIngredientList;

  public IngredientRecyclerViewAdapter(List<Ingredient> ingredientList) {
    mIngredientList = ingredientList;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent,
      int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.fragment_recipe_step_detail_ingredient_list_item, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    String ingredientName = mIngredientList.get(position).getName();
    float quantity = mIngredientList.get(position).getQuantity();
    String measure = mIngredientList.get(position).getMeasure();
    StringBuilder sb = new StringBuilder();
    String[] strings = ingredientName.split(" ");
    sb.append(position + 1);
    sb.append(". ");
    for (String s : strings) {
      sb.append(s.substring(0, 1).toUpperCase());
      sb.append(s.substring(1));
      sb.append(" ");
    }
    holder.mIngredientTextView.setText(sb.toString());
    holder.mQuantityTextView.setText(String.valueOf(quantity));
    holder.mMeasureTextView.setText(measure);
  }

  @Override
  public int getItemCount() {
    if (mIngredientList != null) {
      return mIngredientList.size();
    }
    return 0;
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    TextView mIngredientTextView;
    TextView mQuantityTextView;
    TextView mMeasureTextView;

    public ViewHolder(View itemView) {
      super(itemView);
      mIngredientTextView = itemView.findViewById(R.id.text_view_ingredient_name);
      mQuantityTextView = itemView.findViewById(R.id.text_view_quantity);
      mMeasureTextView = itemView.findViewById(R.id.text_view_measure);
    }
  }
}
