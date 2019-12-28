package com.kkw.bakingapp;


import android.app.Activity;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;


import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;

import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.kkw.bakingapp.data.Recipe;
import com.kkw.bakingapp.data.Step;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoFragment extends Fragment {
    public static final String LOG_TAG = VideoFragment.class.getSimpleName();

    Recipe selectedRecipe;
    Step selectedSteps;

    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";
    String video_url;
    private SimpleExoPlayer simpleExoPlayer;
    @BindView(R.id.movie_exo_player) PlayerView playerView;
    @BindView(R.id.recipe_step_instruction) TextView recipe_step_instruction;
   // @BindView(R.id.description_container) FrameLayout frameLayout;

    public VideoFragment(){
    }

    public VideoFragment(Recipe selectedRecipe,Step selectedSteps){
        this.selectedRecipe = selectedRecipe;
        this.selectedSteps = selectedSteps;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_fragment, container,false);
        ButterKnife.bind(this, view);

            if(savedInstanceState != null){
            selectedRecipe = savedInstanceState.getParcelable("recipe");
            selectedSteps = savedInstanceState.getParcelable("steps");
        }
        recipe_step_instruction.setText(selectedSteps.description());

        if (!TextUtils.isEmpty(selectedSteps.videoURL())) {
            video_url = selectedSteps.videoURL();
            initExoPlayer(video_url);

        }else{
            playerView.setVisibility(View.GONE);
            Toast.makeText(getActivity(),"There is no Video for this step",Toast.LENGTH_SHORT).show();
        }

        return view;

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recipe_step_instruction.setVisibility(View.GONE);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            recipe_step_instruction.setVisibility(View.VISIBLE);
        }

    }

    private void initExoPlayer(String url) {

        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext());
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
        playerView.setPlayer(simpleExoPlayer);
        DataSource.Factory dataSourceFactory= new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(),"BakingApp"));
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(url));
        simpleExoPlayer.prepare(videoSource);
        simpleExoPlayer.setPlayWhenReady(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        simpleExoPlayer.release();

    }



    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("recipe",selectedRecipe);
        outState.putParcelable("steps",selectedSteps);


    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState != null)
        {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
        }
    }
}

