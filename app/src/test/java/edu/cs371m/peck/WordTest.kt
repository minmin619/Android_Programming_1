package edu.cs371m.peck

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@RunWith(
    RobolectricTestRunner::class
)
class WordTest {

    private fun testWords(start: Int, numWords: Int, expectedList: List<String>) {
        val wordList = PickWords.pick(PrideAndPrejudice, start, numWords)
        //print(wordList)
        assertEquals(expectedList, wordList)
    }

    // Here is a very basic test to get you started.
    @Test
    fun start0() {
        testWords(
            0, 8,
            listOf(
                "PRIDE", "AND", "PREJUDICE", "By", "Jane", "Austen", "Chapter", "1"
            )
        )
    }
}

