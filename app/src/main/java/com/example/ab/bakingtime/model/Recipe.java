package com.example.ab.bakingtime.model;

import android.os.Parcel;
import com.squareup.moshi.Json;
import java.util.List;

/**
 * @json annotations are for moshi library.
 */
public class Recipe implements android.os.Parcelable {

  public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
    @Override
    public Recipe createFromParcel(Parcel source) {
      return new Recipe(source);
    }

    @Override
    public Recipe[] newArray(int size) {
      return new Recipe[size];
    }
  };
  @Json(name = "id")
  private int mId;
  @Json(name = "name")
  private String mName;
  @Json(name = "image")
  private String mImage;
  @Json(name = "servings")
  private int mServing;
  @Json(name = "ingredients")
  private List<Ingredient> mIngredientList;
  @Json(name = "steps")
  private List<Step> mStepsList;
  public Recipe() {
  }
  protected Recipe(Parcel in) {
    this.mId = in.readInt();
    this.mName = in.readString();
    this.mImage = in.readString();
    this.mServing = in.readInt();
    this.mIngredientList = in.createTypedArrayList(Ingredient.CREATOR);
    this.mStepsList = in.createTypedArrayList(Step.CREATOR);
  }

  public String getImage() {
    return mImage;
  }

  public void setImage(String image) {
    mImage = image;
  }

  public int getServing() {
    return mServing;
  }

  public void setServing(int serving) {
    mServing = serving;
  }

  public List<Step> getStepsList() {
    return mStepsList;
  }

  public void setStepsList(List<Step> stepsList) {
    mStepsList = stepsList;
  }

  public String getName() {
    return mName;
  }

  public List<Ingredient> getIngredientList() {
    return mIngredientList;
  }

  public int getId() {
    return mId;
  }

  public void setId(int id) {
    mId = id;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.mId);
    dest.writeString(this.mName);
    dest.writeString(this.mImage);
    dest.writeInt(this.mServing);
    dest.writeTypedList(this.mIngredientList);
    dest.writeTypedList(this.mStepsList);
  }
}
