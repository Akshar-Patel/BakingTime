package com.example.ab.bakingtime.model;

import android.os.Parcel;
import com.squareup.moshi.Json;

public class Step implements android.os.Parcelable {

  public static final Creator<Step> CREATOR = new Creator<Step>() {
    @Override
    public Step createFromParcel(Parcel source) {
      return new Step(source);
    }

    @Override
    public Step[] newArray(int size) {
      return new Step[size];
    }
  };
  @Json(name = "id")
  private int mId;
  @Json(name = "shortDescription")
  private String shortDesc;
  @Json(name = "description")
  private String desc;
  @Json(name = "videoURL")
  private String videoUrl;

  public Step() {
  }

  protected Step(Parcel in) {
    this.mId = in.readInt();
    this.shortDesc = in.readString();
    this.desc = in.readString();
    this.videoUrl = in.readString();
  }

  public int getId() {
    return mId;
  }

  public void setId(int id) {
    mId = id;
  }

  public String getShortDesc() {
    return shortDesc;
  }

  public void setShortDesc(String shortDesc) {
    this.shortDesc = shortDesc;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public String getVideoUrl() {
    return videoUrl;
  }

  public void setVideoUrl(String videoUrl) {
    this.videoUrl = videoUrl;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.mId);
    dest.writeString(this.shortDesc);
    dest.writeString(this.desc);
    dest.writeString(this.videoUrl);
  }
}
