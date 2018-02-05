package com.example.ab.bakingtime.activity.recipe_step_list;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.example.ab.bakingtime.R;
import com.example.ab.bakingtime.activity.main.MainActivity;
import com.example.ab.bakingtime.activity.main.SimpleIdlingResource;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by ab on 5/2/18.
 */
@RunWith(AndroidJUnit4.class)
public class RecipeStepListActivityTest {

  @Rule
  public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(
      MainActivity.class);


  SimpleIdlingResource mSimpleIdlingResource;

  @Before
  public void registerIdlingResource() {
    mSimpleIdlingResource = mActivityTestRule.getActivity().getSimpleIdlingResource();
    IdlingRegistry.getInstance().register(mSimpleIdlingResource);
  }

  @Test
  public void checkRecipeStepList() {
    onView(withId(R.id.recycler_view_recipe_list)).perform(
        RecyclerViewActions.actionOnItemAtPosition(0, click()));
    onView(withId(R.id.recycler_view_recipe_step_list)).check(matches(isDisplayed()));
    onView(allOf(withId(R.id.text_view_recipe_step_name),
        withText("Press the crust into baking form.")))
        .check(matches(isDisplayed()));
    onView(allOf(withId(R.id.text_view_recipe_step_name), withText("Finishing Steps")))
        .check(matches(isDisplayed()));
  }

  @After
  public void unRegisterIdlingResource() {
    if (mSimpleIdlingResource != null) {
      IdlingRegistry.getInstance().unregister(mSimpleIdlingResource);
    }
  }
}
