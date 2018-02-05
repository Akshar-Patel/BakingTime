package com.example.ab.bakingtime.model;

import android.os.Parcel;
import com.squareup.moshi.Json;


public class Ingredient implements android.os.Parcelable {

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
  @Json(name = "mQuantity")
  private float mQuantity;
  @Json(name = "mMeasure")
  private String mMeasure;
  @Json(name = "ingredient")
  private String mName;

  @Json(name = "ingredient")

  public Ingredient() {
  }

  protected Ingredient(Parcel in) {
    this.mQuantity = in.readFloat();
    this.mMeasure = in.readString();
    this.mName = in.readString();
  }

  public float getQuantity() {
    return mQuantity;
  }

  public String getMeasure() {
    return mMeasure;
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
    dest.writeFloat(this.mQuantity);
    dest.writeString(this.mMeasure);
    dest.writeString(this.mName);
  }
}
