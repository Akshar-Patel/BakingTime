package com.example.ab.bakingtime;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.ab.bakingtime.model.Recipe;
import com.example.ab.bakingtime.util.Util;
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

  public static final String RECIPE_LIST_KEY = "recipe_list";
  private static final int RECIPE_LOADER = 0;
  public List<Recipe> mRecipeList;

  RecyclerView mRecipeRecyclerView;
  RecipeRecyclerViewAdapter mRecipeRecyclerViewAdapter;

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
    return new RecipeLoader(this);
  }

  @Override
  public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> data) {
    mRecipeList = data;
    mRecipeRecyclerViewAdapter = new RecipeRecyclerViewAdapter(mRecipeList);
    mRecipeRecyclerView.setAdapter(mRecipeRecyclerViewAdapter);
    Util.saveInSharedPref(this, RECIPE_LIST_KEY, mRecipeList);
  }

  @Override
  public void onLoaderReset(Loader<List<Recipe>> loader) {

  }

  static class RecipeLoader extends AsyncTaskLoader<List<Recipe>> {

    private static final String URL = BuildConfig.API_URL;

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

  static class RecipeRecyclerViewAdapter extends
      RecyclerView.Adapter<RecipeRecyclerViewAdapter.ViewHolder> {

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        int recipeId = (int) view.getTag();
        Intent intent = new Intent(view.getContext(), RecipeStepListActivity.class);
        intent.putExtra(RecipeStepListActivity.EXTRA_RECIPE_ID_KEY, recipeId);
        view.getContext().startActivity(intent);
      }
    };
    List<Recipe> mRecipeList;

    public RecipeRecyclerViewAdapter(List<Recipe> recipeList) {
      mRecipeList = new ArrayList<>();
      mRecipeList = recipeList;
    }

    @Override
    public RecipeRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.recipe_recycler_view_content, parent, false);
      return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeRecyclerViewAdapter.ViewHolder holder, int position) {
      holder.mRecipeNameTextView.setText(mRecipeList.get(position).getName());
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

      public ViewHolder(View itemView) {
        super(itemView);
        mRecipeNameTextView = itemView.findViewById(R.id.text_view_recipe_name);
      }
    }
  }
}
