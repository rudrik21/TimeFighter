package com.rudrik.timefighter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.PersistableBundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.core.os.HandlerCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlin.coroutines.suspendCoroutine

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val SCORE_KEY = "score"
    private val TIME_LEFT_KEY = "timeLeft"
    private val GAME_STARTED_KEY = "getStarted"


    private var score = 0
    private var gameStarted = false
    private lateinit var countDownTimer: CountDownTimer
    private var initialCountdown = 30000L
    private var countDownInterval = 1000L
    private var timeLeft = 30

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            savedInstanceState.let {
                score = it.getInt(SCORE_KEY, 0)
                timeLeft = it.getInt(TIME_LEFT_KEY, 30)
                gameStarted = it.getBoolean(GAME_STARTED_KEY, false)
            }
            score.toString().log(this)
            restoreGame()
        } else {
            resetGame()
        }

        bTapMe.setOnClickListener(this) //  to register click listener onTap
    }

    //1
    //  to increment score
    private fun incrementScore() {
        if (gameStarted.not()) {
            startGame()
        }
        score += 1
        val newScore = "${getString(R.string.your_score)} $score"
        tvScore.text = newScore
    }

    //  to reset game
    private fun resetGame() {
        // 1
        score = 0
        // for setting text fields
        val initialScore = getString(R.string.your_score) + score
        tvScore.text = initialScore
        val initialTimeLeft = getString(R.string.time_left) + 30
        tvTimeLeft.text = initialTimeLeft

        // 2
        countDownTimer = object : CountDownTimer(
            initialCountdown,
            countDownInterval
        ) {
            // 3
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished.toInt() / 1000
                val timeLeftString = getString(R.string.time_left) + timeLeft
                tvTimeLeft.text = timeLeftString
            }

            override fun onFinish() {
                endGame()
            }
        }
// 4
        gameStarted = false
    }

    //  to restore game
    private fun restoreGame() {
        // for setting text fields
        val restoredScore = "${getString(R.string.your_score)} $score"
        tvScore.text = restoredScore

        val restoredTimeLeft = "${getString(R.string.time_left)} $timeLeft"
        tvTimeLeft.text = restoredTimeLeft

        countDownTimer = object : CountDownTimer(
            (timeLeft * 1000).toLong(),
            countDownInterval
        ) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished.toInt() / 1000
                val timeLeftStr = "${getString(R.string.time_left)} $timeLeft"
                tvTimeLeft.text = timeLeftStr
            }

            override fun onFinish() {
                endGame()
            }
        }
        countDownTimer.start()
        gameStarted = true
    }

    //  to start game
    private fun startGame() {
        countDownTimer.start()
        gameStarted = true
    }

    //  to end game
    private fun endGame() {
        "${getString(R.string.game_over_message)} $score.".snack(bTapMe)
        resetGame()
    }

    override fun onClick(v: View?) {
        when (v) {

            //  for onTap click action
            bTapMe -> {
                incrementScore()
            }
        }
    }

    //2
    //  for saving session values
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SCORE_KEY, score)
        outState.putInt(TIME_LEFT_KEY, timeLeft)
        outState.putBoolean(GAME_STARTED_KEY, gameStarted)
        countDownTimer.cancel()
        "Losing state: $score".log(this)
    }

    //3
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_about_us, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mbAbout -> {
                showInfo()
            }
        }
        return true
    }

    private fun showInfo() {
        val dialogTitle = getString(R.string.about_title, BuildConfig.VERSION_NAME)
        val dialogMsg = getString(R.string.about_message)
        val dialog = AlertDialog.Builder(this)
            .setTitle(dialogTitle)
            .setMessage(dialogMsg)
            .create()
        dialog.show()
    }

    /*//  for restoring session values
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
*//*
        savedInstanceState.getInt(SCORE_KEY).let {
            score = it
        }
        savedInstanceState.getInt(TIME_LEFT_KEY).let {
            timeLeft = it
        }
        savedInstanceState.getBoolean(GAME_STARTED_KEY).let {
            gameStarted = it
        }
        "Recovering state: $score, $timeLeft".log(this)
        *//*
    }*/


}