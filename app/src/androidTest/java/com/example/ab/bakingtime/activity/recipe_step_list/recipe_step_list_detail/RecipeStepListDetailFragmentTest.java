package com.example.ab.bakingtime.activity.recipe_step_list.recipe_step_list_detail;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

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

@RunWith(AndroidJUnit4.class)
public class RecipeStepListDetailFragmentTest {

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
  public void clickRecipeIngredients_checkIngredientList() {
    onView(withId(R.id.recycler_view_recipe_list)).perform(
        RecyclerViewActions.actionOnItemAtPosition(0, click()));
    onView(withId(R.id.recycler_view_recipe_step_list)).check(matches(isDisplayed()));
    onView(allOf(withId(R.id.text_view_ingredient), withText("Ingredients"))).perform(click());
    onView(withId(R.id.recycler_view_ingredients)).check(matches(isDisplayed()));
    onView(allOf(withId(R.id.text_view_ingredient_name),
        withText("6. Nutella Or Other Chocolate-hazelnut Spread ")))
        .check(matches(isDisplayed()));
  }

  @Test
  public void clickRecipeStep_checkPlayer() {
    onView(withId(R.id.recycler_view_recipe_list)).perform(
        RecyclerViewActions.actionOnItemAtPosition(0, click()));
    onView(withId(R.id.recycler_view_recipe_step_list)).check(matches(isDisplayed()));
    onView(withId(R.id.recycler_view_recipe_step_list)).perform(
        RecyclerViewActions.actionOnItemAtPosition(0, click()));
    onView(allOf(withId(R.id.exo_play), withContentDescription("Play"), isDisplayed()));
  }

  @Test
  public void clickRecipeStep_checkDesc() {
    onView(withId(R.id.recycler_view_recipe_list)).perform(
        RecyclerViewActions.actionOnItemAtPosition(3, click()));
    onView(withId(R.id.recycler_view_recipe_step_list)).check(matches(isDisplayed()));
    onView(withId(R.id.recycler_view_recipe_step_list)).perform(
        RecyclerViewActions.actionOnItemAtPosition(5, click()));
    onView(allOf(withId(R.id.text_view_recipe_step_short_desc),
        withText("Mix cream cheese and dry ingredients.")))
        .check(matches(isDisplayed()));
  }

  @Test
  public void clickRecipeStep_checkPrevButton() {
    onView(withId(R.id.recycler_view_recipe_list)).perform(
        RecyclerViewActions.actionOnItemAtPosition(2, click()));
    onView(withId(R.id.recycler_view_recipe_step_list)).check(matches(isDisplayed()));
    onView(withId(R.id.recycler_view_recipe_step_list)).perform(
        RecyclerViewActions.actionOnItemAtPosition(6, click()));
    onView(allOf(withId(R.id.text_view_recipe_step_short_desc),
        withText("Pour batter into pans.")))
        .check(matches(isDisplayed()));
    onView(allOf(withId(R.id.button_recipe_step_prev), withText("previous"))).perform(click());
    onView(allOf(withId(R.id.text_view_recipe_step_short_desc),
        withText("Add egg mixture to batter.")))
        .check(matches(isDisplayed()));
  }

  @Test
  public void clickRecipeStep_checkNextButton() {
    onView(withId(R.id.recycler_view_recipe_list)).perform(
        RecyclerViewActions.actionOnItemAtPosition(2, click()));
    onView(withId(R.id.recycler_view_recipe_step_list)).check(matches(isDisplayed()));
    onView(withId(R.id.recycler_view_recipe_step_list)).perform(
        RecyclerViewActions.actionOnItemAtPosition(6, click()));
    onView(allOf(withId(R.id.text_view_recipe_step_short_desc),
        withText("Pour batter into pans.")))
        .check(matches(isDisplayed()));
    onView(allOf(withId(R.id.button_recipe_step_next), withText("next"))).perform(click());
    onView(allOf(withId(R.id.text_view_recipe_step_short_desc),
        withText("Begin making buttercream.")))
        .check(matches(isDisplayed()));
  }

  @After
  public void unRegisterIdlingResource() {
    if (mSimpleIdlingResource != null) {
      IdlingRegistry.getInstance().unregister(mSimpleIdlingResource);
    }
  }
}
