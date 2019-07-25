package org.sairaa.androidtest;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class RecyclerViewTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<MainActivity>(MainActivity.class);

    @Test
    public void scrollToItemBelowFold_checkItsText() {
        // First, scroll to the position that needs to be matched and click on it.
        onView(withId(R.id.test_rv))
                .perform(RecyclerViewActions.actionOnItemAtPosition(5,
                        click()));

        // Match the text in an item below the fold and check that it's displayed.
//        String itemElementText = activityScenarioRule.getScenario().getResult()
//                .getString(R.string.item_element_text)
//                + String.valueOf(ITEM_BELOW_THE_FOLD);
        onView(withText("5")).check(matches(isDisplayed()));
    }

    @Test
    public void scroll_rv_test(){
        onView(withId(R.id.test_rv))
                .perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));

        onView(withRecyclerView(R.id.test_rv).atPosition(3))
                .check(matches(hasDescendant(withText("3"))));


    }

    @Test
    public void scroll_test2(){
        onView(withId(R.id.test_rv)).perform(scrollToPosition(90));
        onView(withText("90")).check(matches(isCompletelyDisplayed()));
    }

    public static void tapRecyclerViewItem(int recyclerViewId, int position) {
        onView(withId(recyclerViewId)).perform(scrollToPosition(position));
        onView(withRecyclerView(recyclerViewId).atPosition(position)).perform(click());
    }

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }
}
