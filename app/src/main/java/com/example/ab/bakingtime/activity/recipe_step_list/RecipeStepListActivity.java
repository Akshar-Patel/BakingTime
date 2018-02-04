package com.example.ab.bakingtime.activity.recipe_step_list;

import static android.support.v4.app.NavUtils.navigateUpFromSameTask;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.example.ab.bakingtime.R;
import com.example.ab.bakingtime.activity.recipe_step_list.recipe_step_detail.RecipeStepDetailActivity;
import com.example.ab.bakingtime.activity.recipe_step_list.recipe_step_detail.RecipeStepDetailFragment;

public class RecipeStepListActivity extends AppCompatActivity {

  public static final String EXTRA_RECIPE_ID_KEY = "extra_recipe_id";
  public static final String EXTRA_RECIPE_NAME_KEY = "extra_recipe_name";
  private int mRecipeId;
  private String mRecipeName;

  //two pane mode for tablet
  private boolean mTwoPane;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe_step_list);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }

    Intent intent = getIntent();
    if (intent.getExtras() != null) {
      mRecipeId = intent.getExtras().getInt(EXTRA_RECIPE_ID_KEY);
      mRecipeName = intent.getExtras().getString(EXTRA_RECIPE_NAME_KEY);
      getSupportActionBar().setTitle(mRecipeName);
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
          arguments.putInt(RecipeStepDetailFragment.ARG_RECIPE_ID_KEY, mRecipeId);
          arguments.putBoolean(RecipeStepDetailFragment.ARG_IS_FOR_INGREDIENT, true);
          RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
          fragment.setArguments(arguments);
          getSupportFragmentManager().beginTransaction()
              .replace(R.id.frame_layout_recipe_step_detail_container, fragment)
              .commit();
        } else {
          Context context = view.getContext();
          Intent intent = new Intent(context, RecipeStepDetailActivity.class);
          intent.putExtra(RecipeStepDetailFragment.ARG_IS_FOR_INGREDIENT, true);
          intent.putExtra(RecipeStepDetailFragment.ARG_RECIPE_ID_KEY, mRecipeId);
          context.startActivity(intent);
        }
      }
    });

    View recipeListRecyclerView = findViewById(R.id.recycler_view_recipe_step_list);
    setupRecyclerView((RecyclerView) recipeListRecyclerView);
  }


  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putString(EXTRA_RECIPE_NAME_KEY, mRecipeName);
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
    recyclerView.setAdapter(new RecipeStepListRecyclerViewAdapter(this, mRecipeId, mTwoPane));
  }
}
