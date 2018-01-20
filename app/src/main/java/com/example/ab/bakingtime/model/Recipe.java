package com.example.ab.bakingtime.model;

import android.os.Parcel;
import com.squareup.moshi.Json;
import java.util.ArrayList;
import java.util.List;

/**
 * @json annotations are for moshi json library.
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
  @Json(name = "ingredients")
  private List<Ingredient> mIngredientList;

  public Recipe() {
  }

  protected Recipe(Parcel in) {
    this.mId = in.readInt();
    this.mName = in.readString();
    this.mIngredientList = new ArrayList<>();
    in.readList(this.mIngredientList, Ingredient.class.getClassLoader());
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
    dest.writeList(this.mIngredientList);
  }

  static class Ingredient implements android.os.Parcelable {

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
      @Override
      public Ingredient createFromParcel(Parcel source) {
        return new Ingredient(source);
      }

      @Override
      public Ingredient[] newArray(int size) {
        return new Ingredient[size];
      }
    };
    @Json(name = "quantity")
    private float quantity;
    @Json(name = "measure")
    private String measure;
    @Json(name = "ingredient")
    private String mName;

    public Ingredient() {
    }

    protected Ingredient(Parcel in) {
      this.quantity = in.readFloat();
      this.measure = in.readString();
      this.mName = in.readString();
    }

    public float getQuantity() {
      return quantity;
    }

    public String getMeasure() {
      return measure;
    }

    public String getName() {
      return mName;
    }

    @Override
    public int describeContents() {
      return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
      dest.writeFloat(this.quantity);
      dest.writeString(this.measure);
      dest.writeString(this.mName);
    }
  }
}
