package com.kkw.bakingapp.data;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;


@AutoValue
public abstract class Ingredient implements Parcelable {
    public abstract String quantity();
    public abstract String measure();
    public abstract String ingredient();

    public static Builder builder() {
        return new AutoValue_Ingredient.Builder();
    }


    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder quantity(String quantity);
        public abstract Builder measure(String measure);
        public abstract Builder ingredient(String ingredient);

        public abstract Ingredient build();
    }
}