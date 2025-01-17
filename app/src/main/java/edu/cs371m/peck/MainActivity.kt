package edu.cs371m.peck

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import edu.cs371m.peck.databinding.ActivityMainBinding
import edu.cs371m.peck.databinding.ContentMainBinding
import kotlinx.coroutines.*
import kotlin.random.Random

class MainActivity :
    AppCompatActivity(),
    CoroutineScope by MainScope()
{
    private lateinit var contentMainBinding: ContentMainBinding
    // A round lasts this many milliseconds
    private val durationMillis = 8000L
    private var score = 0
    private val numWords = 6
    private val seed = 3
    private var random = Random(seed)
    var testing = false

    //
    private lateinit var timer: Timer
    private lateinit var words: Words


    override fun onDestroy() {
        super.onDestroy()
        cancel() // destroy all coroutines
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        setSupportActionBar(activityMainBinding.toolbar)
        contentMainBinding = activityMainBinding.contentMain

        // Initialize Timer and Words
        timer = Timer(contentMainBinding.button)
        words = Words(
            contentMainBinding.sentence,
            contentMainBinding.playArea,
            random
        )

        contentMainBinding.button.setOnClickListener {
            // If user gets impatient and reclicks button, cancel running coroutines
            coroutineContext.cancelChildren()
            newGame()
        }
    }

    private fun doScore(millisLeft: Long) {
        // User won Get one point plus tenths of a sec left
        if( millisLeft > 0 ) {
            if (testing) {
                score++
            } else {
                score += (millisLeft / 100).toInt()
                score++
            }
            contentMainBinding.scoreTV.text = score.toString()
            Log.d(localClassName, "User wins ${score}")
        }
    }

    private fun newGame() {
        // Play a round
        // XXX Create timer and Words

        timer = Timer(contentMainBinding.button)
        words = Words(
            contentMainBinding.sentence,
            contentMainBinding.playArea,
            Random(System.nanoTime().toInt())
        )


        launch {
            val timerJob = async {
                timer.timerCo(durationMillis)
            }
            // Ok, this is an ugly hack.  We want to give the user
            // full credit for winning, even if we pass a constraint that
            // results in a zero-length word list.  But that code executes
            // so quickly the timer might not have even initialized.
            // So delay the words.playRound just slightly to give the
            // timer a chance to initialize
            delay(1)
            words.playRound(numWords) {
                val score = timer.millisLeft()
                doScore(score)
                launch{timerJob.cancelAndJoin()}
            }
        }
    }
}
