package com.example.ab.bakingtime.activity.recipe_step_list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.ab.bakingtime.R;
import com.example.ab.bakingtime.activity.recipe_step_list.recipe_step_detail.RecipeStepDetailActivity;
import com.example.ab.bakingtime.activity.recipe_step_list.recipe_step_detail.RecipeStepDetailFragment;
import com.example.ab.bakingtime.model.Recipe;
import com.example.ab.bakingtime.model.Step;
import com.example.ab.bakingtime.util.Util;
import java.util.List;

public class RecipeStepListRecyclerViewAdapter
    extends RecyclerView.Adapter<RecipeStepListRecyclerViewAdapter.ViewHolder> {

  private final RecipeStepListActivity mParentActivity;
  private final List<Step> mValues;
  private final boolean mTwoPane;
  private int mRecipeId;
  private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      int stepId = (int) view.getTag();
      if (mTwoPane) {
        Bundle arguments = new Bundle();
        arguments.putInt(
            RecipeStepDetailFragment.ARG_RECIPE_ID_KEY, mRecipeId);
        arguments.putInt(RecipeStepDetailFragment.ARG_RECIPE_STEP_ID_KEY, stepId);
        RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
        fragment.setArguments(arguments);
        mParentActivity.getSupportFragmentManager().beginTransaction()
            .replace(R.id.frame_layout_recipe_step_detail_container, fragment)
            .commit();
      } else {
        Context context = view.getContext();
        Intent intent = new Intent(context, RecipeStepDetailActivity.class);
        intent.putExtra(RecipeStepDetailFragment.ARG_RECIPE_STEP_ID_KEY, stepId);
        intent.putExtra(RecipeStepDetailFragment.ARG_RECIPE_ID_KEY, mRecipeId);
        context.startActivity(intent);
      }
    }
  };
  private String mRecipeName;


  RecipeStepListRecyclerViewAdapter(RecipeStepListActivity parent,
      int recipeId,
      boolean twoPane) {
    List<Recipe> recipeList = Util
        .loadFromSharedPref(parent, Util.RECIPE_LIST_KEY);
    mRecipeId = recipeId;
    mRecipeName = recipeList.get(recipeId).getName();
    mValues = recipeList.get(recipeId).getStepsList();
    mParentActivity = parent;
    mTwoPane = twoPane;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.activity_recipe_step_list_item, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, int position) {
    holder.mStepNameTextView.setText(mValues.get(position).getShortDesc());
    holder.itemView.setTag(position);
    holder.itemView.setOnClickListener(mOnClickListener);
  }

  @Override
  public int getItemCount() {
    return mValues.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    final TextView mStepNameTextView;

    ViewHolder(View view) {
      super(view);
      mStepNameTextView = view.findViewById(R.id.text_view_content);
    }
  }
}
