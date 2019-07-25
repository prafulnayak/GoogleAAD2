package org.sairaa.androidtest;

import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.core.internal.deps.dagger.internal.Preconditions.checkNotNull;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class AdapterTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<MainActivity>(MainActivity.class);

    @Test
    public void testAdapter(){
        onView(withText("99")).check(doesNotExist());
    }

    @Test
    public void list_Scrolls() {
        onView(withText("1")).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void listTest(){
                onView(ViewMatchers.withId(R.id.test_rv))
                .perform(RecyclerViewActions.actionOnItemAtPosition(10,
                        click()));
//        onData(allOf(is(instanceOf(Map.class)), hasEntry(equalTo("STR"), is("1011"))));
//                .perform(click());
//        onData(withItemContent("10"))
//                .perform(click());
//
//        onView(withId(R.id.textView2))
//                .check(matches(withText("10")));
    }
//    @Test
//    public void scrollToItemBelowFold_checkItsText() {
//        // First, scroll to the position that needs to be matched and click on it.
//        onView(ViewMatchers.withId(R.id.test_rv))
//                .perform(RecyclerViewActions.actionOnItemAtPosition(10,
//                        click()));
//
//        // Match the text in an item below the fold and check that it's displayed.
//        String itemElementText = activityRule.getActivity().getResources()
//                .getString(R.string.item_element_text)
//                + String.valueOf(ITEM_BELOW_THE_FOLD);
//        onView(withText(itemElementText)).check(matches(isDisplayed()));
//    }

    public static Matcher<Object> withItemContent(String expectedText) {
        checkNotNull(expectedText);
        return withItemContent(String.valueOf(equalTo(expectedText)));
    }
//    private static DataInteraction onRow(String str) {
//        return onData(hasEntry(atPosition()));
//    }

    private static Matcher<View> withAdaptedData(final Matcher<Object> dataMatcher) {
        return new TypeSafeMatcher<View>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("with class name: ");
                dataMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof AdapterView)) {
                    return false;
                }
                @SuppressWarnings("rawtypes")
                Adapter adapter = ((AdapterView) view).getAdapter();
                for (int i = 0; i < adapter.getCount(); i++) {
                    if (dataMatcher.matches(adapter.getItem(i))) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    @SuppressWarnings("rawtypes")
    public static Matcher<Object> withItemContent(final Matcher<String> itemTextMatcher) {
        // use preconditions to fail fast when a test is creating an invalid matcher.
        checkNotNull(itemTextMatcher);
        return new BoundedMatcher<Object, Map>(Map.class) {
            @Override
            public boolean matchesSafely(Map map) {
                return hasEntry(equalTo("STR"), itemTextMatcher).matches(map);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with item content: ");
                itemTextMatcher.describeTo(description);
            }
        };
    }
}
