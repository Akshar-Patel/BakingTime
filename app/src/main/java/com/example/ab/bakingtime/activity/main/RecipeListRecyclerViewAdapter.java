package com.example.ab.bakingtime.activity.main;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.ab.bakingtime.R;
import com.example.ab.bakingtime.activity.recipe_step_list.RecipeStepListActivity;
import com.example.ab.bakingtime.model.Recipe;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

class RecipeListRecyclerViewAdapter extends
    RecyclerView.Adapter<RecipeListRecyclerViewAdapter.ViewHolder> {

  private List<Recipe> mRecipeList;
  private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      int recipeId = (int) view.getTag();
      Intent intent = new Intent(view.getContext(), RecipeStepListActivity.class);
      intent.putExtra(RecipeStepListActivity.EXTRA_RECIPE_ID_KEY, recipeId);
      intent.putExtra(RecipeStepListActivity.EXTRA_RECIPE_NAME_KEY,
          mRecipeList.get(recipeId).getName());
      view.getContext().startActivity(intent);
    }
  };

  RecipeListRecyclerViewAdapter(List<Recipe> recipeList) {
    mRecipeList = new ArrayList<>();
    mRecipeList = recipeList;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.activity_main_recipe_list_item, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    holder.mRecipeNameTextView.setText(mRecipeList.get(position).getName());
    holder.mRecipeServingTextView.setText(
        new StringBuilder()
            .append(holder.itemView.getContext().getString(R.string.label_recipe_serving))
            .append(" ")
            .append(String.valueOf(mRecipeList.get(position).getServing())).toString());
    String image = mRecipeList.get(position).getImage();
    if (!TextUtils.isEmpty(image)) {
      Picasso.with(holder.itemView.getContext())
          .load(image)
          .placeholder(R.drawable.recipe_step_placeholder)
          .error(R.drawable.recipe_step_placeholder)
          .into(holder.mRecipeImageView);
    }
    holder.itemView.setTag(position);
    holder.itemView.setOnClickListener(mOnClickListener);
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
    TextView mRecipeServingTextView;
    ImageView mRecipeImageView;
    public ViewHolder(View itemView) {
      super(itemView);
      mRecipeNameTextView = itemView.findViewById(R.id.text_view_recipe_name);
      mRecipeServingTextView = itemView.findViewById(R.id.text_view_recipe_serving);
      mRecipeImageView = itemView.findViewById(R.id.image_view_recipe);
    }
  }
}
