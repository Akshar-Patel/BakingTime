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
