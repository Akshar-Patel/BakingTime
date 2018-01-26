package com.example.ab.bakingtime;

import static android.support.v4.app.NavUtils.navigateUpFromSameTask;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.ab.bakingtime.model.Recipe;
import com.example.ab.bakingtime.model.Step;
import com.example.ab.bakingtime.util.Util;
import java.util.List;

public class RecipeStepListActivity extends AppCompatActivity {

  public static final String EXTRA_RECIPE_ID_KEY = "extra_recipe_id";
  public static int mRecipeId;

  //two pane mode for tablet
  private boolean mTwoPane;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe_step_list);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setTitle(getTitle());

    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }

    Intent intent = getIntent();
    if (intent.getExtras() != null) {
      mRecipeId = intent.getExtras().getInt(EXTRA_RECIPE_ID_KEY);
    }

    if (findViewById(R.id.frame_layout_recipe_step_detail_container) != null) {
      // The detail container view will be present only in the
      // large-screen layouts (res/values-w900dp).
      // If this view is present, then the
      // activity should be in two-pane mode.
      mTwoPane = true;
    }

    TextView ingredientTextView = findViewById(R.id.text_view_ingredient);
    ingredientTextView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (mTwoPane) {
          Bundle arguments = new Bundle();
          arguments.putInt(RecipeStepDetailFragment.ARG_RECIPE_INGREDIENT_KEY, mRecipeId);
          RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
          fragment.setArguments(arguments);
          getSupportFragmentManager().beginTransaction()
              .replace(R.id.frame_layout_recipe_step_detail_container, fragment)
              .commit();
        } else {
          Context context = view.getContext();
          Intent intent = new Intent(context, RecipeStepDetailActivity.class);
          intent.putExtra(RecipeStepDetailFragment.ARG_RECIPE_INGREDIENT_KEY, mRecipeId);
          context.startActivity(intent);
        }
      }
    });

    View recipeListRecyclerView = findViewById(R.id.recycler_view_recipe_step_list);
    setupRecyclerView((RecyclerView) recipeListRecyclerView);
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == android.R.id.home) {
      // This ID represents the Home or Up button. In the case of this
      // activity, the Up button is shown. Use NavUtils to allow users
      // to navigate up one level in the application structure. For
      // more details, see the Navigation pattern on Android Design:
      //
      // http://developer.android.com/design/patterns/navigation.html#up-vs-back
      //
      navigateUpFromSameTask(this);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
    recyclerView.setAdapter(new RecipeListRecyclerViewAdapter(this, mRecipeId, mTwoPane));
  }

  public static class RecipeListRecyclerViewAdapter
      extends RecyclerView.Adapter<RecipeListRecyclerViewAdapter.ViewHolder> {

    private final RecipeStepListActivity mParentActivity;
    private final List<Step> mValues;
    private final boolean mTwoPane;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        int stepId = (int) view.getTag();
        if (mTwoPane) {
          Bundle arguments = new Bundle();
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
          context.startActivity(intent);
        }
      }
    };

    RecipeListRecyclerViewAdapter(RecipeStepListActivity parent,
        int recipeId,
        boolean twoPane) {
      List<Recipe> recipeList = (List<Recipe>) Util
          .loadFromSharedPref(parent, MainActivity.RECIPE_LIST_KEY);
      mValues = recipeList.get(recipeId).getStepsList();
      mParentActivity = parent;
      mTwoPane = twoPane;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.recipe_step_list_content, parent, false);
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
}
