package com.kkw.bakingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kkw.bakingapp.data.Recipe;

public class IngredientListAdapter extends RecyclerView.Adapter<IngredientListAdapter.RecyclerViewHolder> {
    Recipe recipe;
    public TextView iTextView;
    // LiveData<Recipe> recipe;

    public IngredientListAdapter(Recipe selectedRecipe) {

        this.recipe = selectedRecipe;
    }
    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public RecyclerViewHolder(final LayoutInflater inflater, ViewGroup container) {
            super(inflater.inflate(R.layout.ingredient_info,container,false));
            iTextView= itemView.findViewById(R.id.ingredients_info);
        }
    }

    @NonNull
    @Override
    public IngredientListAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        return new IngredientListAdapter.RecyclerViewHolder(inflater,parent);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientListAdapter.RecyclerViewHolder holder, int position) {
        iTextView.setText(recipe.ingredients().get(position).ingredient()
                + " " +recipe.ingredients().get(position).quantity()+" "
                +recipe.ingredients().get(position).measure());


    }

    @Override
    public int getItemCount() {
        return recipe.ingredients().size();
    }

}
