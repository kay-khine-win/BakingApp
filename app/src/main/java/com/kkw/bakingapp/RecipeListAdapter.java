package com.kkw.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.kkw.bakingapp.data.Recipe;
import java.util.ArrayList;

public class RecipeListAdapter extends
        RecyclerView.Adapter<RecipeListAdapter.RecipeListViewHolder> {

    private ArrayList<Recipe> recipesList;
    private LayoutInflater mInflater;

    public RecipeListAdapter(Context context, ArrayList<Recipe> recipesList) {
        this.recipesList = recipesList;
        this.mInflater = LayoutInflater.from(context);;
    }

    public class RecipeListViewHolder extends RecyclerView.ViewHolder {

        RecipeListAdapter mAdapter;
        private TextView recipeTitle;

        public RecipeListViewHolder(@NonNull View itemView, RecipeListAdapter mAdapter) {
            super(itemView);
            recipeTitle = itemView.findViewById(R.id.recipeTitle);
            this.mAdapter = mAdapter;


            recipeTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DetailActivity.class);
                    Bundle bundle = new Bundle();

                    bundle.putParcelable("recipe", recipesList.get(getAdapterPosition()));
                    bundle.putParcelable("steps", recipesList.get(getAdapterPosition()).steps().get(0));

                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);
                }
            });
        }



    }


    @NonNull
    @Override
    public RecipeListAdapter.RecipeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View mItemView = mInflater.inflate(R.layout.recipe_list_main, parent, false);
        return new RecipeListViewHolder(mItemView, this);
    }



    @Override
    public void onBindViewHolder(@NonNull RecipeListAdapter.RecipeListViewHolder holder, int position) {

        Recipe recipefromNW = recipesList.get(position);
        holder.recipeTitle.setText(recipefromNW.name());

    }

    @Override
    public int getItemCount() {

        return recipesList.size();
    }


}
