package com.kkw.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.kkw.bakingapp.data.Recipe;

import butterknife.ButterKnife;

public class StepListAdapter extends RecyclerView.Adapter<StepListAdapter.RecyclerViewHolder> {
    Recipe recipe;

    public CardView mCardView;
    public TextView mTextView;

    // Define a new interface OnStepClickListener that triggers a callback in the host activity
    OnStepClickListener mCallback;

    // OnStepClickListener interface, calls a method in the host activity named onStepSelected
    public interface OnStepClickListener {
        void onStepSelected(int position);
    }

   public StepListAdapter(Recipe selectedRecipe, OnStepClickListener listener) {
        this.recipe = selectedRecipe;
        mCallback= listener;
    }
        class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
        }


        public RecyclerViewHolder(final LayoutInflater inflater, ViewGroup container) {
            super(inflater.inflate(R.layout.step_info,container,false));
            ButterKnife.bind(this, itemView);

            mCardView = itemView.findViewById(R.id.recipe_info_card_container);
            mTextView = itemView.findViewById(R.id.step_textView);

            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    mCallback.onStepSelected(position);
                }
            });

            //itemView.setOnClickListener(this);

//          mTextView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(v.getContext(), StepDetailedActivity.class);
//                    Bundle bundle = new Bundle();
//
//                    bundle.putParcelable("recipe", recipe);
//                    bundle.putParcelable("steps", recipe.steps().get(getAdapterPosition()));
//
//                    intent.putExtras(bundle);
//                    v.getContext().startActivity(intent);
//               }
//            });


        }


    }

    @NonNull
    @Override
    public StepListAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        return new StepListAdapter.RecyclerViewHolder(inflater,parent);
    }


    @Override
    public void onBindViewHolder(@NonNull StepListAdapter.RecyclerViewHolder holder, int position) {
        mTextView.setText(recipe.steps().get(position).shortDescription());

    }

    @Override
    public int getItemCount() {
        if(recipe.steps().size() == 0){
            return 0;
        } else {
            return recipe.steps().size() ;
        }
    }
}

