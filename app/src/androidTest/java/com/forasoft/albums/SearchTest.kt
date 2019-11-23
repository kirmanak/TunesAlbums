package com.forasoft.albums

import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.forasoft.albums.view.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchTest {
    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun enterSearchRequest() {
        onView(withId(R.id.app_bar_search)).perform(click())
        onView(isAssignableFrom(EditText::class.java)).perform(
            typeText("Eminem"), pressImeActionButton()
        )
        onView(isAssignableFrom(RecyclerView::class.java)).check(matches(isDisplayed()))
        onView(isDescendantOfA(isAssignableFrom(RecyclerView::class.java))).check(
            matches(isDisplayed())
        )
    }
}