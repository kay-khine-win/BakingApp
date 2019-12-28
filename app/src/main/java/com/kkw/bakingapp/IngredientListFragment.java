package com.kkw.bakingapp;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kkw.bakingapp.data.Recipe;



public class IngredientListFragment extends Fragment {
        public static final String LOG_TAG = IngredientListFragment.class.getSimpleName();

        RecyclerView recyclerView;
        Recipe selectedRecipe;
        private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";
        private RecyclerView.Adapter adapter;

        public IngredientListFragment(){
        }

        public IngredientListFragment(Recipe selectedRecipe){

            this.selectedRecipe = selectedRecipe;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.ingredient_list_fragment, container,false);
            recyclerView = view.findViewById(R.id.ingredient_list_recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

            if(savedInstanceState != null){
                selectedRecipe = savedInstanceState.getParcelable("key");
                Log.d("RECIPE ", selectedRecipe.toString());
                showRecyclerView();

            }
            else {
                showRecyclerView();
            }
            return view;

        }
    private void showRecyclerView(){
        adapter = new IngredientListAdapter(selectedRecipe);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("key",selectedRecipe);

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState != null)
        {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }
}
