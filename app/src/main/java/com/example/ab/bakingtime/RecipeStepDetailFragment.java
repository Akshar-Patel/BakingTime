package com.example.ab.bakingtime;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.ab.bakingtime.model.Ingredient;
import com.example.ab.bakingtime.model.Recipe;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import java.util.List;


/**
 * A fragment representing a single RecipeStep detail screen.
 * This fragment is either contained in a {@link RecipeStepListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeStepDetailActivity}
 * on handsets.
 */
public class RecipeStepDetailFragment extends Fragment {


  public static final String ARG_RECIPE_STEP_ID_KEY = "step_id";
  public static final String ARG_RECIPE_ID_KEY = "recipe_id";
  public static final String ARG_IS_FOR_INGREDIENT = "is_ingredient";
  private int mStepId;
  private int mRecipeId;
  private boolean mIsForIngredient;
  private List<Recipe> mRecipeList;
  private SimpleExoPlayerView mSimpleExoPlayerView;

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the
   * fragment (e.g. upon screen orientation changes).
   */
  public RecipeStepDetailFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mRecipeList = (List<Recipe>) com.example.ab.bakingtime.util.Util
        .loadFromSharedPref(getContext(), MainActivity.RECIPE_LIST_KEY);

    if (getArguments().containsKey(ARG_RECIPE_ID_KEY)) {
      mRecipeId = getArguments().getInt(ARG_RECIPE_ID_KEY);
    }

    if (getArguments().containsKey(ARG_IS_FOR_INGREDIENT)) {
      mIsForIngredient = getArguments().getBoolean(ARG_IS_FOR_INGREDIENT);
    }

    if (getArguments().containsKey(ARG_RECIPE_STEP_ID_KEY)) {

      mStepId = getArguments().getInt(ARG_RECIPE_STEP_ID_KEY);
      Activity activity = this.getActivity();
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.recipe_step_detail, container, false);
    android.support.v7.app.ActionBar actionBar = ((AppCompatActivity) getActivity())
        .getSupportActionBar();
    RecyclerView ingredientRecyclerView = rootView.findViewById(R.id.recycler_view_ingredients);
    mSimpleExoPlayerView = rootView.findViewById(R.id.exo_player);
    if (mIsForIngredient) {
      if (actionBar != null) {
        actionBar.setTitle(R.string.ingredients);
      }
      mSimpleExoPlayerView.setVisibility(View.GONE);
      IngredientRecyclerViewAdapter ingredientRecyclerViewAdapter = new IngredientRecyclerViewAdapter(
          mRecipeList.get(mRecipeId).getIngredientList());
      ingredientRecyclerView.setAdapter(ingredientRecyclerViewAdapter);
    } else {
      if (actionBar != null) {
        actionBar.setTitle(R.string.title_recipe_step_detail);
      }
      ingredientRecyclerView.setVisibility(View.GONE);
      setUpExoPlayer(rootView);
      makeFullScreenInLandscape(rootView, actionBar);
      ((TextView) rootView.findViewById(R.id.recipe_step_short_desc)).setText(mRecipeList
          .get(mRecipeId).getStepsList().get(mStepId).getDesc());

    }
    return rootView;
  }

  void setUpExoPlayer(View view) {
    // 1. Create a default TrackSelector
    Handler mainHandler = new Handler();
    DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
    TrackSelection.Factory videoTrackSelectionFactory =
        new AdaptiveTrackSelection.Factory(bandwidthMeter);
    TrackSelector trackSelector =
        new DefaultTrackSelector(videoTrackSelectionFactory);

// 2. Create the player
    SimpleExoPlayer player =
        ExoPlayerFactory.newSimpleInstance(view.getContext(), trackSelector);
    mSimpleExoPlayerView.setPlayer(player);

// Produces DataSource instances through which media data is loaded.

    DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(view.getContext(),
        com.google.android.exoplayer2.util.Util
            .getUserAgent(view.getContext(), "yourApplicationName"), bandwidthMeter);
// This is the MediaSource representing the media to be played.
    MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
        .createMediaSource(Uri.parse(
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4"));
// Prepare the player with the source.
    player.prepare(videoSource);
  }

  void makeFullScreenInLandscape(View view, ActionBar actionBar) {
    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
      mSimpleExoPlayerView.setLayoutParams(
          new LinearLayout.LayoutParams(Resources.getSystem().getDisplayMetrics().widthPixels,
              Resources.getSystem().getDisplayMetrics().heightPixels));

      getActivity().findViewById(R.id.app_bar_step_detail)
          .setLayoutParams(new CoordinatorLayout.LayoutParams(0, 0));

      getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
          WindowManager.LayoutParams.FLAG_FULLSCREEN);
      if (actionBar != null) {
        actionBar.hide();
      }
      view.findViewById(R.id.constraint_layout).setVisibility(View.GONE);
    }
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
