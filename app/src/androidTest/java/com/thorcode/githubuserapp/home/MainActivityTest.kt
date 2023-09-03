package com.thorcode.githubuserapp.home

import android.view.KeyEvent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.thorcode.githubuserapp.R
import dev.thorcode.core.ui.ListUsersAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest{

    @Before
    fun setup() {
        ActivityScenario.launch(MainActivity::class.java)
    }


    @Test
    fun assertUserList() {
        onView(withId(R.id.rvUsers))
            .check(matches(isDisplayed()))
    }

    @Test
    fun assertSearchView(): Unit = runBlocking {
        onView(withId(R.id.search))
            .check(matches(isDisplayed()))
        onView(withId(R.id.search))
            .perform(click())
        onView(withId(androidx.appcompat.R.id.search_src_text))
            .perform(typeText("haha"))
            .perform(pressKey(KeyEvent.KEYCODE_ENTER))
        delay(2000L)
        onView(withId(R.id.rvUsers))
            .perform(RecyclerViewActions.scrollTo<ListUsersAdapter.ListViewHolder>(hasDescendant(
                withText("haha")
            )))
    }

    @Test
    fun assertHomeToActivity() {
        onView(withId(R.id.ic_favorite))
            .check(matches(isDisplayed()))
            .perform(click())
        onView(withId(R.id.rvUsersFav))
            .check(matches(isDisplayed()))
    }

    @Test
    fun assertToSwitchTheme() {
        onView(withId(R.id.ic_switch_theme))
            .check(matches(isDisplayed()))
            .perform(click())
        onView(withId(R.id.switch_theme))
            .check(matches(isDisplayed()))
    }
}