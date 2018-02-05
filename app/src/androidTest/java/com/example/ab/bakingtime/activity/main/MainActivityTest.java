package com.example.ab.bakingtime.activity.main;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.example.ab.bakingtime.R;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

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
  public void checkRecipeList() {
    onView(withId(R.id.recycler_view_recipe_list)).check(matches(isDisplayed()));
    onView(allOf(withId(R.id.text_view_recipe_name), withText("Nutella Pie")))
        .check(matches(isDisplayed()));
    onView(allOf(withId(R.id.text_view_recipe_name), withText("Yellow Cake")))
        .check(matches(isDisplayed()));
  }

  @Test
  public void findRecipe_clickRecipe() {
    onView(withId(R.id.recycler_view_recipe_list)).perform(
        RecyclerViewActions.actionOnItemAtPosition(0, click()));
    onView(allOf(withId(R.id.text_view_recipe_step_name),
        withText("Press the crust into baking form.")))
        .check(matches(isDisplayed()));
    onView(withContentDescription("Navigate up")).perform(click());
    onView(withId(R.id.recycler_view_recipe_list))
        .perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));
    onView(
        allOf(withId(R.id.text_view_recipe_step_name),
            withText("Mix cream cheese and dry ingredients.")))
        .check(matches(isDisplayed()));
  }

  @After
  public void unRegisterIdlingResource() {
    if (mSimpleIdlingResource != null) {
      IdlingRegistry.getInstance().unregister(mSimpleIdlingResource);
    }
  }
}
