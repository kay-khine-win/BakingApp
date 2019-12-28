package com.kkw.bakingapp.data;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

import java.io.Serializable;

@AutoValue
public abstract class Step implements Parcelable {
    public abstract int id();
    public abstract String shortDescription();
    public abstract String description();
    public abstract String videoURL();
    public abstract String thumbnailURL();

    public static Builder builder() {
        return new AutoValue_Step.Builder();
    }



    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(int id);
        public abstract Builder shortDescription(String shortDescription);
        public abstract Builder description(String description);
        public abstract Builder videoURL(String videoURL);
        public abstract Builder thumbnailURL(String thumbnailURL);

        public abstract Step build();
    }
}
