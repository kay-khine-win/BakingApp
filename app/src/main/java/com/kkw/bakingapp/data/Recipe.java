package com.kkw.bakingapp.data;
import android.os.Parcelable;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class Recipe implements Parcelable {
    public abstract String name();
    public abstract List<Ingredient> ingredients();
    public abstract List<Step> steps();

    public static Builder builder() {

        return new AutoValue_Recipe.Builder();
    }


    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder name(String name);
        public abstract Builder ingredients(List<Ingredient> ingredients);
        public abstract Builder steps(List<Step> steps);
        public abstract Recipe build();
    }
}
