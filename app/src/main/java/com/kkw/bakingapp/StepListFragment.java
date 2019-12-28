package com.kkw.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kkw.bakingapp.data.Recipe;
import com.kkw.bakingapp.data.Step;


public class StepListFragment extends Fragment implements StepListAdapter.OnStepClickListener{

    RecyclerView recyclerView;
    Recipe selectedRecipe;
    Step selectedSteps;

    FragmentManager video_fragManager;
    Fragment video_fragment;

    OnFragStepCLick fcallBack;

    public interface OnFragStepCLick {
        void OnFragStepClicked(int pos);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            fcallBack = (StepListFragment.OnFragStepCLick) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Must implement OnFragStepCLick");
        }
    }
    @Override
    public void onStepSelected(int position) {
        fcallBack.OnFragStepClicked(position);

    }

    private StepListAdapter adapter;
    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";
    public StepListFragment(){
    }


   public StepListFragment(Recipe selectedRecipe){
       this.selectedRecipe = selectedRecipe;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.detail_list_fragment, container,false);
        recyclerView = view.findViewById(R.id.detail_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new StepListAdapter(selectedRecipe, this);

        if(savedInstanceState != null){
            selectedRecipe = savedInstanceState.getParcelable("key");
            showRecyclerView();

        }
        else {
            showRecyclerView();
        }

        return view;

    }


    private void showRecyclerView(){
       // adapter = new StepListAdapter(selectedRecipe,this::onStepSelected);
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



//    @Override
//    public void onStepSelected(int position) {
//        Intent intent = new Intent(getContext(), StepDetailedActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("recipe", selectedRecipe);
//        bundle.putParcelable("steps", selectedRecipe.steps().get(position));
//        intent.putExtras(bundle);
//        getContext().startActivity(intent);
//    }
}
