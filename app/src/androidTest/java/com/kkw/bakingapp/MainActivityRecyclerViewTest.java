package com.kkw.bakingapp;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class MainActivityRecyclerViewTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void recyclerViewClickTest(){
        //Wait to get data from network
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.mRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(1,click()));
        String recipeName = "Brownies";
        onView(withText(recipeName)).check(matches(isDisplayed()));

    }
    @Test
    public void idlingRes(){
        //Wait to get data from network
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.registerIdlingResources(mActivityTestRule.getActivity().countingIdlingResource);
        String recipeName = "Yellow Cake";
        onView(withText(recipeName)).check(matches(isDisplayed()));
    }

    @Test
    public void recyclerViewScrollTest(){
        RecyclerView recyclerView = mActivityTestRule.getActivity().findViewById(R.id.mRecyclerView);
        int i = recyclerView.getAdapter().getItemCount();
        onView(withId(R.id.mRecyclerView)).perform(RecyclerViewActions.scrollToPosition(i-1));

    }

}
