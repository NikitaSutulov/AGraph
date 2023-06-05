package com.nickytoolchick.agraph

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nickytoolchick.agraph.ui.DatasetOptionsActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class FileIOTest {
    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setup() {
        Intents.init()
    }

    @Test
    fun testFileIO() {
        onView(withId(R.id.configureDatasetButton)).perform(click())
        Intents.intended(hasComponent(DatasetOptionsActivity::class.java.name))

        onView(withId(R.id.strokeSizeEditText)).perform(replaceText("4.4"))
        onView(withId(R.id.pointRadiusEditText)).perform(replaceText("8.3"))
        onView(withId(R.id.lineSmoothCheckedTV)).perform(click())

        onView(withId(R.id.newPointXEditText)).perform(replaceText("24.9"))
        onView(withId(R.id.newPointYEditText)).perform(replaceText("36.7"))
        onView(withId(R.id.addPointButton)).perform(click())
        onView(withId(R.id.pointsTV)).check(matches(withText("24.9 36.7\n")))

        onView(withId(R.id.submitDatasetOptionsButton)).perform(click())
        onView(withId(R.id.saveButton)).perform(click())
        onView(withId(R.id.configureDatasetButton)).perform(click())

        onView(withId(R.id.strokeSizeEditText)).perform(replaceText("4.9"))
        onView(withId(R.id.pointRadiusEditText)).perform(replaceText("8.5"))
        onView(withId(R.id.lineSmoothCheckedTV)).perform(click())
        onView(withId(R.id.newPointXEditText)).perform(replaceText("24.9"))
        onView(withId(R.id.newPointYEditText)).perform(replaceText("36.7"))
        onView(withId(R.id.deletePointButton)).perform(click())
        onView(withId(R.id.submitDatasetOptionsButton)).perform(click())

        onView(withId(R.id.loadButton)).perform(click())
        onView(withId(R.id.configureDatasetButton)).perform(click())

        onView(withId(R.id.strokeSizeEditText)).check(matches(withText("4.4")))
        onView(withId(R.id.pointRadiusEditText)).check(matches(withText("8.3")))
        onView(withId(R.id.lineSmoothCheckedTV)).check(matches(isChecked()))
        onView(withId(R.id.pointsTV)).check(matches(withText("24.9 36.7\n")))
    }

    @After
    fun tearDown() {
        Intents.release()
    }
}