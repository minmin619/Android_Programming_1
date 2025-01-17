package edu.cs371m.peck


import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.LayoutAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.util.HumanReadables
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.`is`
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)

class ExampleInstrumentedTest {

    /**
     * Instrumented test, which will execute on an Android device.
     *
     * See [testing documentation](http://d.android.com/tools/testing).
     */
    @Test

    fun clickInOrder() {
        // Launch the activity
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        scenario.moveToState(Lifecycle.State.RESUMED)

        // Start a new game by clicking the button
        onView(withId(R.id.button)).perform(ViewActions.click())

        // Wait a moment for the game to initialize (optional)
        Thread.sleep(1000) // Adjust as needed for the UI to update

        // Verify the sentence is displayed
        onView(withId(R.id.sentence))
            .check(matches(isDisplayed()))
            .check(matches(not(withText("")))) // Ensure it's not empty

        // Verify the words are placed in the play area
        onView(withId(R.id.playArea))
            .check(matches(isDisplayed()))

        // Simulate clicks on the words in the correct order
        val words = listOf("Word1", "Word2", "Word3") // Replace with test-specific words
        words.forEachIndexed { index, word ->
            onView(withTagValue(`is`(index.toString()))) // Find TextView by tag
                .perform(ViewActions.click())
            Thread.sleep(500) // Allow time for UI updates
        }

        // Verify the play area is empty after clicking all words in order
        onView(withId(R.id.playArea)).check(matches(hasChildCount(0)))

        // Verify the score is updated
        onView(withId(R.id.scoreTV))
            .check(matches(isDisplayed()))
            .check(matches(not(withText("0")))) // Ensure the score has increased
    }

}
