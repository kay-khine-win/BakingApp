package com.kkw.bakingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.idling.CountingIdlingResource;

import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.kkw.bakingapp.data.Ingredient;
import com.kkw.bakingapp.data.Recipe;
import com.kkw.bakingapp.data.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecipeListAdapter mAdapter;
    private ArrayList<Recipe> mRecipeList = new ArrayList<>();
    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";

    CountingIdlingResource countingIdlingResource = new CountingIdlingResource("RECIPE_LOADER");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.mRecyclerView);

        if(savedInstanceState != null){
            mRecipeList = savedInstanceState.getParcelableArrayList("key");
            Log.d("RECIPE LIST", mRecipeList.toString());
            showRecyclerView();

        }
        else {
            countingIdlingResource.increment();
            fetchJSON();
            showRecyclerView();
        }

    }

    private void showRecyclerView(){
      //  boolean isTablet = getResources().getBoolean(R.bool.isTablet);
      //  RecyclerView.LayoutManager mLayoutManager;
//        if (isTablet) {
//            mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
//
//        } else {
//            mLayoutManager = new LinearLayoutManager(getApplicationContext());
//        }
//
//        mRecyclerView.setLayoutManager(mLayoutManager);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecipeListAdapter(this, mRecipeList);
        mRecyclerView.setAdapter(mAdapter);
    }

    //Restore Recycler Scroll Position
    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) { //onViewStateRestored is for Fragment
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null)
        {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("key",mRecipeList);
        Log.d("RECIPE LIST onSave", mRecipeList.toString());

        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, mRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    private void fetchJSON() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RecyclerInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        RecyclerInterface api = retrofit.create(RecyclerInterface.class);

        Call<String> call = api.getString();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //Log.d("Responsestring", response.body().toString());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        //Log.d("onSuccess", response.body().toString());
                        String jsonresponse = response.body().toString();
                        writeRecycler(jsonresponse);

                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");
                        Toast.makeText(getApplicationContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    countingIdlingResource.decrement();
    }



    private ArrayList<Recipe> writeRecycler(String response){
       // ArrayList<Recipe> mRecipeList = new ArrayList<>();
        if (TextUtils.isEmpty(response)) {
            return null;
        }

        try {
            JSONArray dataArray = new JSONArray(response);
            for (int i = 0; i < dataArray.length(); i++) {

                JSONObject recipe = (JSONObject) dataArray.get(i);
                String name = recipe.getString("name");
               // Log.d("NAME", name);

                JSONArray ingredientArray = recipe.getJSONArray("ingredients");
                List<Ingredient> ingredientList = new ArrayList<>();
                for (int j = 0; j < ingredientArray.length(); j++) {
                    String q = ingredientArray.getJSONObject(j).getString("quantity");
                    String m = ingredientArray.getJSONObject(j).getString("measure");
                    String ing = ingredientArray.getJSONObject(j).getString("ingredient");

                    Ingredient ingObj = Ingredient.builder()
                            .quantity(q)
                            .measure(m)
                            .ingredient(ing)
                            .build();
                    ingredientList.add(ingObj);
                }

                JSONArray stepArray = recipe.getJSONArray("steps");
                List<Step> stepList = new ArrayList<>();
                for (int j = 0; j < stepArray.length(); j++) {
                    int id = j;
                    String shortDescription = stepArray.getJSONObject(j).getString("shortDescription");
                    String description = stepArray.getJSONObject(j).getString("description");
                    String videoURL = stepArray.getJSONObject(j).getString("videoURL");
                    String thumbnailURL = stepArray.getJSONObject(j).getString("thumbnailURL");

                    Step stepObj = Step.builder()
                            .id(id)
                            .shortDescription(shortDescription)
                            .description(description)
                            .videoURL(videoURL)
                            .thumbnailURL(thumbnailURL)
                            .build();
                    stepList.add(stepObj);
                }

                Recipe r = Recipe.builder()
                        .name(name)
                        .ingredients(ingredientList)
                        .steps(stepList)
                        .build();

              //  Log.d("Recipe", r.name());
               // Log.d("Ingredient", r.ingredients().toString());
              //  Log.d("Steps ", r.steps().toString());

                mRecipeList.add(r);

                mAdapter = new RecipeListAdapter(this, mRecipeList);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager
                        (getApplicationContext(), LinearLayoutManager.VERTICAL, false));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mRecipeList;

    }

}
