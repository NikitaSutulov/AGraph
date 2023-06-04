package com.nickytoolchick.agraph

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isNotChecked
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nickytoolchick.agraph.ui.ChartOptionsActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ChartOptionsCollectingTest {
    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setup() {
        Intents.init()
    }

    @Test
    fun testChartOptionsCollecting() {
        onView(withId(R.id.configureChartButton)).perform(click())
        Intents.intended(hasComponent(ChartOptionsActivity::class.java.name))

        onView(withId(R.id.horizontalStepEditText)).check(matches(withText("0.0")))
        onView(withId(R.id.horizontalStepEditText)).perform(replaceText("5.0"))
        onView(withId(R.id.horizontalStepEditText)).check(matches(withText("5.0")))

        onView(withId(R.id.verticalStepEditText)).check(matches(withText("0.0")))
        onView(withId(R.id.verticalStepEditText)).perform(replaceText("10.3"))
        onView(withId(R.id.verticalStepEditText)).check(matches(withText("10.3")))

        onView(withId(R.id.xMinEditText)).check(matches(withText("0.0")))
        onView(withId(R.id.xMinEditText)).perform(replaceText("2.8"))
        onView(withId(R.id.xMinEditText)).check(matches(withText("2.8")))

        onView(withId(R.id.xMaxEditText)).check(matches(withText("0.0")))
        onView(withId(R.id.xMaxEditText)).perform(replaceText("3.4"))
        onView(withId(R.id.xMaxEditText)).check(matches(withText("3.4")))

        onView(withId(R.id.yMinEditText)).check(matches(withText("0.0")))
        onView(withId(R.id.yMinEditText)).perform(replaceText("6.9"))
        onView(withId(R.id.yMinEditText)).check(matches(withText("6.9")))

        onView(withId(R.id.yMaxEditText)).check(matches(withText("0.0")))
        onView(withId(R.id.yMaxEditText)).perform(replaceText("4.2"))
        onView(withId(R.id.yMaxEditText)).check(matches(withText("4.2")))

        onView(withId(R.id.logScaleXCheckedTV)).check(matches(isNotChecked()))
        onView(withId(R.id.logScaleXCheckedTV)).perform(click())
        onView(withId(R.id.logScaleXCheckedTV)).check(matches(isChecked()))

        onView(withId(R.id.logScaleYCheckedTV)).check(matches(isNotChecked()))
        onView(withId(R.id.logScaleYCheckedTV)).perform(click())
        onView(withId(R.id.logScaleYCheckedTV)).check(matches(isChecked()))

        onView(withId(R.id.showHorizontalLinesCheckedTV)).check(matches(isNotChecked()))
        onView(withId(R.id.showHorizontalLinesCheckedTV)).perform(click())
        onView(withId(R.id.showHorizontalLinesCheckedTV)).check(matches(isChecked()))

        onView(withId(R.id.showVerticalLinesCheckedTV)).check(matches(isNotChecked()))
        onView(withId(R.id.showVerticalLinesCheckedTV)).perform(click())
        onView(withId(R.id.showVerticalLinesCheckedTV)).check(matches(isChecked()))

        onView(withId(R.id.submitChartOptionsButton)).perform(click())
        onView(withId(R.id.configureChartButton)).perform(click())

        onView(withId(R.id.horizontalStepEditText)).check(matches(withText("5.0")))
        onView(withId(R.id.verticalStepEditText)).check(matches(withText("10.3")))
        onView(withId(R.id.xMinEditText)).check(matches(withText("2.8")))
        onView(withId(R.id.xMaxEditText)).check(matches(withText("3.4")))
        onView(withId(R.id.yMinEditText)).check(matches(withText("6.9")))
        onView(withId(R.id.yMaxEditText)).check(matches(withText("4.2")))

        onView(withId(R.id.logScaleXCheckedTV)).check(matches(isChecked()))
        onView(withId(R.id.logScaleYCheckedTV)).check(matches(isChecked()))
        onView(withId(R.id.showHorizontalLinesCheckedTV)).check(matches(isChecked()))
        onView(withId(R.id.showVerticalLinesCheckedTV)).check(matches(isChecked()))
    }

    @After
    fun tearDown() {
        Intents.release()
    }
}