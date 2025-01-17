package edu.cs371m.peck

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.setPadding
import kotlin.random.Random

class Words(private val sentenceTV: TextView,
            private val frame: FrameLayout,
            private val random: Random) {
    private val neutralBgColor = Color.rgb(0xCD, 0xCD, 0xCD)
    private val outOfOrderColor = Color.rgb(200, 0, 0)
    private val textViewHeight by lazy {
        val textView = createTextView("Doesn't matter", 0)
        textView.measure(0, 0)
        textView.measuredHeight
    }

    // XXX You probably want some class variables

    private fun findTVWidth(textView: TextView): Int {
        textView.measure(0, 0)
        return textView.measuredWidth
    }

    private fun createTextView(text: String, index: Int): TextView {
        val tv = TextView(frame.context).apply {
            this.text = text // Set the text for the TextView
            this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f) // Set text size
            this.setPadding(8) // Add padding around text
            this.setBackgroundColor(neutralBgColor) // Set background color
            this.tag = index.toString() // Set tag for testing
            this.layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                width = FrameLayout.LayoutParams.WRAP_CONTENT // Allow width to adjust
                height = FrameLayout.LayoutParams.WRAP_CONTENT // Allow height to adjust
            }
            this.maxLines = 1 // Ensure text is on one line
            this.ellipsize = null // Disable ellipsis, show full text
        }
        return tv
    }



    private fun outOfOrderPick(view: View) {
        val colorToWarn: Animator = ValueAnimator
            .ofObject(ArgbEvaluator(), neutralBgColor, outOfOrderColor)
            .apply { duration = 200 } // milliseconds
            .apply { addUpdateListener { animator -> view.setBackgroundColor(animator.animatedValue as Int) } }
        val colorFromWarn = ValueAnimator
            .ofObject(ArgbEvaluator(), outOfOrderColor, neutralBgColor)
            .apply { duration = 350 }
            .apply { addUpdateListener { animator -> view.setBackgroundColor(animator.animatedValue as Int) } }
        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(
            colorToWarn,
            colorFromWarn
        )
        animatorSet.start()
    }


    fun playRound(numWords: Int, wordsDone: () -> Unit) {

        frame.removeAllViews()


        val pickedWords = PickWords.pick(
            PrideAndPrejudice,
            random.nextInt(0, PrideAndPrejudice.length),
            numWords
        )


        sentenceTV.text = pickedWords.joinToString(" ")


        var currentWordIndex = 0
        val placedPositions = mutableSetOf<Pair<Int, Int>>()


        val maxColumns = frame.width / findTVWidth(createTextView("test", 0))
        val maxRows = frame.height / textViewHeight


        pickedWords.forEachIndexed { index, word ->
            val tv = createTextView(word, index)


            // Set click listeners for the TextViews
            
            tv.setOnClickListener { view ->
                if (index == currentWordIndex) {
                    currentWordIndex++
                    frame.removeView(tv)
                    if (currentWordIndex == pickedWords.size) {
                        wordsDone()
                    }
                } else {
                    outOfOrderPick(view)
                }
            }

            // Calculate unique random position within frame bounds
            val tvWidth = tv.paint.measureText(word).toInt() + tv.paddingLeft + tv.paddingRight
            val maxX = frame.width - tvWidth
            val maxY = frame.height - textViewHeight

            var randomX: Int
            var randomY: Int
            do {
                randomX = random.nextInt(8, maxX - 8)
                randomY = random.nextInt(8, maxY - 8)
            } while (placedPositions.contains(Pair(randomX / tvWidth, randomY / textViewHeight)))

            placedPositions.add(Pair(randomX / tvWidth, randomY / textViewHeight))
            tv.x = randomX.toFloat()
            tv.y = randomY.toFloat()

            frame.addView(tv)
        }
    }

}