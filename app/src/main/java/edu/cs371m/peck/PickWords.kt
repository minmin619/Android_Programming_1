package edu.cs371m.peck

import android.util.Log

class PickWords {
    companion object {
        private val punctSpaceStr = " \t\n._,:;“”?!-"
        fun pick(input: String, start: Int, numWords: Int): List<String> {
            // XXX Write me
            val words = mutableListOf<String>()
            val seenWords = mutableMapOf<String, Int>()
            var currentIndex = start

            while (words.size < numWords && currentIndex < input.length) {

                while (currentIndex < input.length && punctSpaceStr.contains(input[currentIndex])) {
                    currentIndex++
                }


                val wordStart = currentIndex
                while (currentIndex < input.length && !punctSpaceStr.contains(input[currentIndex])) {
                    currentIndex++
                }


                if (wordStart < currentIndex) {
                    val word = input.substring(wordStart, currentIndex)


                    val wordWithCount = if (seenWords.containsKey(word)) {
                        val count = seenWords[word]!! + 1
                        seenWords[word] = count
                        "$word($count)"
                    } else {
                        seenWords[word] = 0
                        word
                    }

                    words.add(wordWithCount)
                }
            }

            return words
        }
    }
}