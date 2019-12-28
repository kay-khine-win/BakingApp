package com.kkw.bakingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.SharedPreferences;
import android.os.Parcelable;
import android.util.Log;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.material.navigation.NavigationView;
import com.kkw.bakingapp.data.Recipe;
import com.kkw.bakingapp.data.Step;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailedActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.mtoolbar) Toolbar mtoolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.prev_btn) Button previous_btn;
    @BindView(R.id.next_btn) Button next_btn;

    @BindView(R.id.main_container) FrameLayout main_container;
    Recipe selectedRecipe;
    Step selectedSteps;

    FragmentManager video_fragManager;
    Fragment video_fragment;
    int current_step;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_step_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        if(savedInstanceState != null){
            selectedRecipe = savedInstanceState.getParcelable("key");
            mtoolbar.setTitle(selectedRecipe.name());
        }
        else {
            selectedRecipe = intent.getParcelableExtra("recipe");
            mtoolbar.setTitle(selectedRecipe.name());
        }

        selectedSteps = intent.getParcelableExtra("steps");
        ActionBar actionBar = this.getSupportActionBar();



       if(actionBar!=null){
           actionBar.setDisplayHomeAsUpEnabled(true);
        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, mtoolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
       drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        current_step = selectedSteps.id();


        previous_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current_step==0){
                   previous_btn.setEnabled(false);
                    Toast.makeText(getApplicationContext(),"This is first step! You can't go back to previous step.",Toast.LENGTH_LONG).show();

                }else
                {
                    int i = current_step - 1;
                    Step a = selectedRecipe.steps().get(i);
                    video_fragManager = getSupportFragmentManager();
                    video_fragment = new VideoFragment(selectedRecipe, a);

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_container, video_fragment)
                            .addToBackStack(null)
                            .commit();
                }
                current_step--;

            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current_step == selectedRecipe.steps().size()-1){
                    Log.d("III",String.valueOf(selectedRecipe.steps().size()));
                    next_btn.setEnabled(false);
                    Toast.makeText(getApplicationContext(),"This is the last step of recipe!",Toast.LENGTH_LONG).show();

                }else
                {
                    int j = current_step + 1;
                    Step a = selectedRecipe.steps().get(j);
                    video_fragManager = getSupportFragmentManager();
                    video_fragment = new VideoFragment(selectedRecipe, a);

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_container, video_fragment)
                            .addToBackStack(null)
                            .commit();

                }
                current_step++;

            }
        });

        video_fragManager = getSupportFragmentManager();
        video_fragment = new VideoFragment(selectedRecipe,selectedSteps);
        showFragment();

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            navigationView.setVisibility(View.GONE);
            mtoolbar.setVisibility(View.GONE);
            previous_btn.setVisibility(View.GONE);
            next_btn.setVisibility(View.GONE);

            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) main_container.getLayoutParams();
            params.width=params.MATCH_PARENT;
            params.height=params.MATCH_PARENT;
            main_container.setLayoutParams(params);

        } else {
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) main_container.getLayoutParams();
            params.width=params.MATCH_PARENT;
            params.height=600;
            main_container.setLayoutParams(params);

            mtoolbar.setVisibility(View.VISIBLE);
            previous_btn.setVisibility(View.VISIBLE);
            next_btn.setVisibility(View.VISIBLE);

        }
    }
    private void showFragment() {
        video_fragManager.beginTransaction().add(R.id.main_container, video_fragment).commit();
    }
//    public boolean isInLandscapeMode( Context context ) {
//        return (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.app_bar,menu);
        return true;
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case (R.id.nav_home):
                Intent a = new Intent(StepDetailedActivity.this, MainActivity.class);
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


    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null)
        {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable("key");
            mtoolbar.setTitle(selectedRecipe.name());

        }

    }
}


