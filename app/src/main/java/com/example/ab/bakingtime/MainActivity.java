package com.example.ab.bakingtime;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.example.ab.bakingtime.model.Recipe;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Recipe>> {

  private static final int RECIPE_LOADER = 0;
  private static String TAG = MainActivity.class.getSimpleName();
  List<Recipe> mRecipeList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    getSupportLoaderManager().initLoader(RECIPE_LOADER, null, this);
  }

  @Override
  public Loader<List<Recipe>> onCreateLoader(int id, Bundle args) {
    return new RecipeLoader(this);
  }

  @Override
  public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> data) {
    mRecipeList = new ArrayList<>();
    mRecipeList = data;
    Log.i(TAG, mRecipeList.get(0).getName());
  }

  @Override
  public void onLoaderReset(Loader<List<Recipe>> loader) {

  }

  public static class RecipeLoader extends AsyncTaskLoader<List<Recipe>> {

    private static final String URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    private static final OkHttpClient client = new OkHttpClient();

    private static final Moshi moshi = new Moshi.Builder().build();
    private static final JsonAdapter<List<Recipe>> recipeJsonAdapter = moshi.adapter(
        Types.newParameterizedType(List.class, Recipe.class));

    public RecipeLoader(Context context) {
      super(context);
    }

    @Override
    protected void onStartLoading() {
      super.onStartLoading();
      forceLoad();
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
        return recipeJsonAdapter.fromJson(response.body().source());
      } catch (IOException e) {
        e.printStackTrace();
      }
      return null;
    }
  }
}
