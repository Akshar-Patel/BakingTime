package com.example.ab.bakingtime.activity.recipe_step_list.recipe_step_detail;

import static com.example.ab.bakingtime.util.Util.isTablet;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.ab.bakingtime.R;
import com.example.ab.bakingtime.activity.main.SimpleIdlingResource;
import com.example.ab.bakingtime.activity.recipe_step_list.RecipeStepListActivity;
import com.example.ab.bakingtime.model.Recipe;
import com.example.ab.bakingtime.util.Util;
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
  private static final String KEY_RECIPE_STEP_ID = "step_id";
  private static final String KEY_PLAYER_POSITION = "player_position";
  private static final String KEY_PLAYER_STATE = "player_state";
  private static final String KEY_VIDEO_URL = "video_url";
  private int mStepId;
  private int mRecipeId;
  private boolean mIsForIngredient;
  private List<Recipe> mRecipeList;
  private SimpleExoPlayerView mSimpleExoPlayerView;
  private TextView mNoVideoTextView;
  private TextView mShortDescTextView;
  private TextView mLongDescTextView;
  private SimpleIdlingResource mSimpleIdlingResource;
  private SimpleExoPlayer mPlayer;
  private long mPlayerPosition;
  private boolean mPlayerState;
  private MediaSource mVideoSource;
  private String mVideoUrl;

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
        .loadFromSharedPref(getContext(), Util.RECIPE_LIST_KEY);

    if (getArguments().containsKey(ARG_RECIPE_ID_KEY)) {
      mRecipeId = getArguments().getInt(ARG_RECIPE_ID_KEY);
    }

    if (getArguments().containsKey(ARG_IS_FOR_INGREDIENT)) {
      mIsForIngredient = getArguments().getBoolean(ARG_IS_FOR_INGREDIENT);
    }

    if (savedInstanceState != null) {
      mStepId = savedInstanceState.getInt(KEY_RECIPE_STEP_ID);
      mPlayerPosition = savedInstanceState.getLong(KEY_PLAYER_POSITION);
      mPlayerState = savedInstanceState.getBoolean(KEY_PLAYER_STATE);
    } else {
      if (getArguments().containsKey(ARG_RECIPE_STEP_ID_KEY)) {
        mStepId = getArguments().getInt(ARG_RECIPE_STEP_ID_KEY);
      }
    }

  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt(KEY_RECIPE_STEP_ID, mStepId);
    if (mPlayer != null) {
      outState.putLong(KEY_PLAYER_POSITION, mPlayer.getCurrentPosition());
      outState.putBoolean(KEY_PLAYER_STATE, mPlayer.getPlayWhenReady());
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    final View rootView = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);
    final android.support.v7.app.ActionBar actionBar = ((AppCompatActivity) getActivity())
        .getSupportActionBar();
    RecyclerView ingredientRecyclerView = rootView.findViewById(R.id.recycler_view_ingredients);
    mSimpleExoPlayerView = rootView.findViewById(R.id.exo_player);
    mNoVideoTextView = rootView.findViewById(R.id.text_view_no_video);
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
        actionBar.setTitle(mRecipeList.get(mRecipeId).getName());
      }
      ingredientRecyclerView.setVisibility(View.GONE);

      mVideoUrl = mRecipeList.get(mRecipeId).getStepsList().get(mStepId).getVideoUrl();
      if (!TextUtils.isEmpty(mVideoUrl)) {
        setUpExoPlayer(rootView, mVideoUrl);
      } else {
        mSimpleExoPlayerView.setVisibility(View.GONE);
        mNoVideoTextView.setVisibility(View.VISIBLE);
      }
      makeFullScreenInLandscape(rootView, actionBar);
      ((TextView) rootView.findViewById(R.id.text_view_recipe_step_short_desc)).setText(mRecipeList
          .get(mRecipeId).getStepsList().get(mStepId).getDesc());

      setUpDescription(rootView, mStepId);

      Button prevButton = rootView.findViewById(R.id.button_recipe_step_prev);
      prevButton.setOnClickListener(view -> loadPrevStep(rootView));
      Button nextButton = rootView.findViewById(R.id.button_recipe_step_next);
      nextButton.setOnClickListener(view -> loadNextStep(rootView));
    }
    if (mSimpleIdlingResource != null) {
      mSimpleIdlingResource.setIdleState(true);
    }
    return rootView;
  }

  @Override
  public void onPause() {
    super.onPause();
    SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
    Editor editor = sharedPreferences.edit();
    editor.putString(KEY_VIDEO_URL, mVideoUrl);
    if (mPlayer != null) {
      editor.putLong(KEY_PLAYER_POSITION, mPlayer.getCurrentPosition());
      editor.putBoolean(KEY_PLAYER_STATE, mPlayer.getPlayWhenReady());
    }
    editor.apply();
    if (mVideoSource != null) {
      mVideoSource.releaseSource();
    }
    if (mPlayer != null) {
      mPlayer.release();
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
    mVideoUrl = sharedPreferences.getString(KEY_VIDEO_URL, "");
    mPlayerPosition = sharedPreferences.getLong(KEY_PLAYER_POSITION, 0L);
    mPlayerState = sharedPreferences.getBoolean(KEY_PLAYER_STATE, true);
    if (!TextUtils.isEmpty(mVideoUrl)) {
      setUpExoPlayer(getView(), mVideoUrl);
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
    Editor editor = sharedPreferences.edit();
    editor.remove(KEY_VIDEO_URL);
    editor.remove(KEY_PLAYER_POSITION);
    editor.remove(KEY_PLAYER_STATE);
    editor.apply();
  }

  void loadPrevStep(View view) {
    int prevStepId = mStepId - 1;
    if (prevStepId >= 0) {
      mVideoUrl = mRecipeList.get(mRecipeId).getStepsList().get(prevStepId).getVideoUrl();
      if (!TextUtils.isEmpty(mVideoUrl)) {
        mNoVideoTextView.setVisibility(View.GONE);
        mSimpleExoPlayerView.setVisibility(View.VISIBLE);
        setUpExoPlayer(view, mVideoUrl);
      } else {
        mSimpleExoPlayerView.setVisibility(View.GONE);
        mNoVideoTextView.setVisibility(View.VISIBLE);
      }
      setUpDescription(view, prevStepId);
      mStepId = prevStepId;
    }

  }

  void loadNextStep(View view) {
    int nextStepId = mStepId + 1;
    if (nextStepId < mRecipeList.get(mRecipeId).getStepsList().size()) {
      String videoUrl = mRecipeList.get(mRecipeId).getStepsList().get(nextStepId).getVideoUrl();
      if (!videoUrl.equals("")) {
        mNoVideoTextView.setVisibility(View.GONE);
        mSimpleExoPlayerView.setVisibility(View.VISIBLE);
        setUpExoPlayer(view, videoUrl);
      } else {
        mSimpleExoPlayerView.setVisibility(View.GONE);
        mNoVideoTextView.setVisibility(View.VISIBLE);
      }
      setUpDescription(view, nextStepId);
      mStepId = nextStepId;
    }

  }

  private void setUpDescription(View view, int stepId) {
    mShortDescTextView = view.findViewById(R.id.text_view_recipe_step_short_desc);
    mShortDescTextView
        .setText(mRecipeList.get(mRecipeId).getStepsList().get(stepId).getShortDesc());
    mLongDescTextView = view.findViewById(R.id.text_view_recipe_step_long_desc);
    mLongDescTextView.setText(mRecipeList.get(mRecipeId).getStepsList().get(stepId).getDesc());
  }

  void setUpExoPlayer(View view, String url) {
    // 1. Create a default TrackSelector
    Handler mainHandler = new Handler();
    DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
    TrackSelection.Factory videoTrackSelectionFactory =
        new AdaptiveTrackSelection.Factory(bandwidthMeter);
    TrackSelector trackSelector =
        new DefaultTrackSelector(videoTrackSelectionFactory);

    // 2. Create the player
    mPlayer = ExoPlayerFactory.newSimpleInstance(view.getContext(), trackSelector);
    mSimpleExoPlayerView.setPlayer(mPlayer);
    mSimpleExoPlayerView.setUseArtwork(true);

    // Produces DataSource instances through which media data is loaded.
    DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(view.getContext(),
        com.google.android.exoplayer2.util.Util
            .getUserAgent(view.getContext(), getString(R.string.app_name)), bandwidthMeter);

    // This is the MediaSource representing the media to be played.
    mVideoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
        .createMediaSource(Uri.parse(url));

    // Prepare the player with the source.
    mPlayer.prepare(mVideoSource);
    if (mPlayerPosition > 0L) {
      mPlayer.seekTo(mPlayerPosition);
    }
    mPlayer.setPlayWhenReady(mPlayerState);
  }


  void makeFullScreenInLandscape(View view, ActionBar actionBar) {
    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
        && !isTablet(view.getContext())) {
      mSimpleExoPlayerView.setLayoutParams(
          new LinearLayout.LayoutParams(Resources.getSystem().getDisplayMetrics().widthPixels,
              Resources.getSystem().getDisplayMetrics().heightPixels));

      if (getActivity().findViewById(R.id.app_bar_step_detail) != null) {
        getActivity().findViewById(R.id.app_bar_step_detail)
            .setLayoutParams(new CoordinatorLayout.LayoutParams(0, 0));
      }
      getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
          WindowManager.LayoutParams.FLAG_FULLSCREEN);
      if (actionBar != null) {
        actionBar.hide();
      }
      view.findViewById(R.id.constraint_layout).setVisibility(View.GONE);
    }
  }


}
