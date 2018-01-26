package com.example.ab.bakingtime;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.ab.bakingtime.model.Ingredient;
import com.example.ab.bakingtime.model.Recipe;
import com.example.ab.bakingtime.util.Util;
import java.util.List;

/**
 * A fragment representing a single RecipeStep detail screen.
 * This fragment is either contained in a {@link RecipeStepListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeStepDetailActivity}
 * on handsets.
 */
public class RecipeStepDetailFragment extends Fragment {


  public static final String ARG_RECIPE_STEP_ID_KEY = "item_id";
  public static final String ARG_RECIPE_INGREDIENT_KEY = "ingredient";
  private int mStepId;
  private int mRecipeId;
  private CollapsingToolbarLayout mAppBarLayout;
  private List<Recipe> mRecipeList;

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the
   * fragment (e.g. upon screen orientation changes).
   */
  public RecipeStepDetailFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mRecipeList = (List<Recipe>) Util
        .loadFromSharedPref(getContext(), MainActivity.RECIPE_LIST_KEY);

    if (getArguments().containsKey(ARG_RECIPE_STEP_ID_KEY)) {
      // Load the dummy content specified by the fragment
      // arguments. In a real-world scenario, use a Loader
      // to load content from a content provider.
      mRecipeId = getArguments().getInt(ARG_RECIPE_INGREDIENT_KEY);
      mStepId = getArguments().getInt(ARG_RECIPE_STEP_ID_KEY);
      Activity activity = this.getActivity();
      mAppBarLayout = (CollapsingToolbarLayout) activity
          .findViewById(R.id.toolbar_layout);
      if (mAppBarLayout != null) {
        mAppBarLayout
            .setTitle(mRecipeList.get(RecipeStepListActivity.mRecipeId).getName());
      }
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.recipe_step_detail, container, false);

    if (mRecipeId >= 0) {
      mAppBarLayout
          .setTitle(getString(R.string.ingredients));
      RecyclerView ingredientRecyclerView = rootView.findViewById(R.id.recycler_view_ingredients);
      IngredientRecyclerViewAdapter ingredientRecyclerViewAdapter = new IngredientRecyclerViewAdapter(
          mRecipeList.get(mRecipeId).getIngredientList());
      ingredientRecyclerView.setAdapter(ingredientRecyclerViewAdapter);
    } else {
      ((TextView) rootView.findViewById(R.id.recipestep_detail)).setText(mRecipeList
          .get(RecipeStepListActivity.mRecipeId).getStepsList().get(mStepId).getDesc());
    }
    return rootView;
  }

  static class IngredientRecyclerViewAdapter extends
      RecyclerView.Adapter<IngredientRecyclerViewAdapter.ViewHolder> {

    List<Ingredient> mIngredientList;

    public IngredientRecyclerViewAdapter(List<Ingredient> ingredientList) {
      mIngredientList = ingredientList;
    }

    @Override
    public IngredientRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
        int viewType) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.recycler_view_item_recipe_ingredient, parent, false);
      return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientRecyclerViewAdapter.ViewHolder holder, int position) {
      holder.ingredientTextView.setText(mIngredientList.get(position).getName());
    }

    @Override
    public int getItemCount() {
      if (mIngredientList != null) {
        return mIngredientList.size();
      }
      return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

      TextView ingredientTextView;

      public ViewHolder(View itemView) {
        super(itemView);
        ingredientTextView = itemView.findViewById(R.id.tv_ingredient_item);
      }
    }
  }
}
