package com.example.ab.bakingtime.activity.main;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.ab.bakingtime.BuildConfig;
import com.example.ab.bakingtime.R;
import com.example.ab.bakingtime.model.Recipe;
import com.example.ab.bakingtime.util.Util;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Recipe>> {

  private static final int RECIPE_LOADER = 0;
  public List<Recipe> mRecipeList;

  RecyclerView mRecipeRecyclerView;
  RecipeListRecyclerViewAdapter mRecipeListRecyclerViewAdapter;

  @Nullable
  private SimpleIdlingResource mSimpleIdlingResource;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mRecipeRecyclerView = findViewById(R.id.recycler_view_recipe_list);
    GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
      gridLayoutManager.setSpanCount(3);
    }
    mRecipeRecyclerView.setLayoutManager(gridLayoutManager);

    getSupportLoaderManager().initLoader(RECIPE_LOADER, null, this);
  }

  @Override
  public Loader<List<Recipe>> onCreateLoader(int id, Bundle args) {
    return new RecipeLoader(this, mSimpleIdlingResource);
  }

  @Override
  public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> data) {
    mRecipeList = data;
    mRecipeListRecyclerViewAdapter = new RecipeListRecyclerViewAdapter(mRecipeList);
    mRecipeRecyclerView.setAdapter(mRecipeListRecyclerViewAdapter);
    Util.saveInSharedPref(this, Util.RECIPE_LIST_KEY, mRecipeList);
  }

  @Override
  public void onLoaderReset(Loader<List<Recipe>> loader) {

  }

  /**
   * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
   */
  @VisibleForTesting
  @NonNull
  public SimpleIdlingResource getSimpleIdlingResource() {
    if (mSimpleIdlingResource == null) {
      mSimpleIdlingResource = new SimpleIdlingResource();
    }
    return mSimpleIdlingResource;
  }

  static class RecipeLoader extends AsyncTaskLoader<List<Recipe>> {

    private static final String URL = BuildConfig.API_URL;
    private static final OkHttpClient client = new OkHttpClient();
    private static final Moshi moshi = new Moshi.Builder().build();
    private static final JsonAdapter<List<Recipe>> recipeJsonAdapter = moshi.adapter(
        Types.newParameterizedType(List.class, Recipe.class));
    List<Recipe> mRecipeList;
    private SimpleIdlingResource mSimpleIdlingResource;

    RecipeLoader(Context context, SimpleIdlingResource simpleIdlingResource) {
      super(context);
      this.mSimpleIdlingResource = simpleIdlingResource;
    }

    @Override
    protected void onStartLoading() {
      if (mSimpleIdlingResource != null) {
        mSimpleIdlingResource.setIdleState(false);
      }
      if (mRecipeList != null) {
        deliverResult(mRecipeList);
      } else {
        forceLoad();
      }
      super.onStartLoading();
    }

    @Override
    public List<Recipe> loadInBackground() {
      Request request = new Request.Builder()
          .url(URL)
          .build();
      Response response;
      try {
        response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
          throw new IOException("Unexpected code " + response);
        }
        if (mSimpleIdlingResource != null) {
          mSimpleIdlingResource.setIdleState(true);
        }
        return recipeJsonAdapter.fromJson(response.body().source());
      } catch (IOException e) {
        e.printStackTrace();
      }
      return null;
    }

    @Override
    public void deliverResult(List<Recipe> data) {
      mRecipeList = data;
      super.deliverResult(data);
    }
  }

}
