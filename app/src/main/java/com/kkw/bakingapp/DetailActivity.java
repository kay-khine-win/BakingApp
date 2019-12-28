package com.kkw.bakingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;


import com.google.android.material.navigation.NavigationView;
import com.kkw.bakingapp.data.Recipe;
import com.kkw.bakingapp.data.Step;


import butterknife.BindView;
import butterknife.ButterKnife;
import android.util.Log;
import android.view.MenuItem;

public class DetailActivity extends AppCompatActivity implements StepListFragment.OnFragStepCLick,
        SharedPreferences.OnSharedPreferenceChangeListener,
        NavigationView.OnNavigationItemSelectedListener {

    Fragment ingre_fragment;
    Fragment detail_fragment;
    Recipe selectedRecipe;
    Step selectedSteps;

    FragmentManager ingre_fragManager;
    FragmentManager detail_fragManager;
    @BindView(R.id.mtoolbar)
    Toolbar mtoolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    FragmentManager video_fragManager;
    Fragment video_fragment;
    @BindView(R.id.nav_view) NavigationView navigationView;

    private boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Intent intent = getIntent();


        if (findViewById(R.id.main_container) != null) {
            mTwoPane = true;
            if (savedInstanceState != null) {
                selectedRecipe = savedInstanceState.getParcelable("key");
                mtoolbar.setTitle(selectedRecipe.name());
            } else {
                selectedRecipe = intent.getParcelableExtra("recipe");
                selectedSteps = intent.getParcelableExtra("steps");
                mtoolbar.setTitle(selectedRecipe.name());
                showVideo();
            }

        }

        ingre_fragManager = getSupportFragmentManager();
        detail_fragManager = getSupportFragmentManager();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, mtoolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        selectedRecipe = intent.getParcelableExtra("recipe");
        ingre_fragment = new IngredientListFragment(selectedRecipe);
        detail_fragment = new StepListFragment(selectedRecipe);

        showFragment();
        saveData();

    }



    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("FIR", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("ingredients_size", selectedRecipe.ingredients().size());

        for (int i = 0; i < selectedRecipe.ingredients().size(); i++) {
            editor.remove("Ingredient_" + i);
            editor.putString("Ingredient_" + i, selectedRecipe.ingredients().get(i).ingredient());
            editor.putString("quantity_" + i, selectedRecipe.ingredients().get(i).quantity());
            editor.putString("measure_" + i, selectedRecipe.ingredients().get(i).measure());


        }
        editor.apply();
    }

    @Override
    public void OnFragStepClicked(int pos) {
        if (mTwoPane == true) {
            selectedSteps = selectedRecipe.steps().get(pos);
            video_fragment = new VideoFragment(selectedRecipe, selectedSteps);
            video_fragManager.beginTransaction().add(R.id.main_container, video_fragment).commit();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, video_fragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            Intent intent = new Intent(this, StepDetailedActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("recipe", selectedRecipe);
            bundle.putParcelable("steps", selectedRecipe.steps().get(pos));
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    private void showVideo() {
        video_fragManager = getSupportFragmentManager();
        video_fragment = new VideoFragment(selectedRecipe, selectedSteps);
        video_fragManager.beginTransaction().add(R.id.main_container, video_fragment).commit();
    }

    private void showFragment() {
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mtoolbar.setTitle(selectedRecipe.name());
        ingre_fragManager.beginTransaction().add(R.id.ingredientList_Layout, ingre_fragment).commit();
        detail_fragManager.beginTransaction().add(R.id.detailList_Layout, detail_fragment).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.app_bar, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case (R.id.nav_home):
                Intent a = new Intent(DetailActivity.this, MainActivity.class);
                startActivity(a);
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("key", selectedRecipe);
        outState.putParcelable("key1", selectedSteps);

        getSupportFragmentManager().putFragment(outState, "ing", ingre_fragment);
        getSupportFragmentManager().putFragment(outState, "detail", detail_fragment);


    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
        ingre_fragment = getSupportFragmentManager().getFragment(savedInstanceState, "ing");
        detail_fragment = getSupportFragmentManager().getFragment(savedInstanceState, "detail");

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    }
}
