package no.uia.ikt205.pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import no.uia.ikt205.pomodoro.util.millisecondsToDescriptiveTime

class MainActivity : AppCompatActivity() {

    lateinit var timer: CountDownTimer
    lateinit var startButton: Button
    lateinit var timerB30: Button
    lateinit var timerB60: Button
    lateinit var timerB90: Button
    lateinit var timerB120: Button
    lateinit var coutdownDisplay: TextView

    var timeToCountDownInMsStart = 5000L
    var timeToCountDownInMs30 = 1800000L
    var timeToCountDownInMs60 = 3600000L
    var timeToCountDownInMs90 = 5400000L
    var timeToCountDownInMs120 = 7200000L
    val timeTicks = 1000L

    // private var stopTimer:Boolean = false //Vil stoppe timeren

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startButton = findViewById<Button>(R.id.startCountdownButton)
        startButton.setOnClickListener() {
            startCountDown(it)
        }

        coutdownDisplay = findViewById(R.id.countDownView)

        //val Timeduration = listOf<Button>(findViewById<Button>(R.id.button30),

        //Her ifra og videre blir funksjoner til knappene 30,60,90,120 minutter
        timerB30 = findViewById<Button>(R.id.button30)
        timerB60 = findViewById<Button>(R.id.button60)
        timerB90 = findViewById<Button>(R.id.button90)
        timerB120 = findViewById<Button>(R.id.button120)

        timerB30.setOnClickListener() {
            timeToCountDownInMsStart = timeToCountDownInMs30
        }
        timerB60.setOnClickListener() {
            timeToCountDownInMsStart = timeToCountDownInMs60
        }
        timerB90.setOnClickListener() {
            timeToCountDownInMsStart = timeToCountDownInMs90
        }
        timerB120.setOnClickListener() {
            timeToCountDownInMsStart = timeToCountDownInMs120
        }
    }

    fun startCountDown(v: View) {

        timer = object : CountDownTimer(timeToCountDownInMsStart, timeTicks) {
            override fun onFinish() {
                Toast.makeText(this@MainActivity, "Arbeidsøkt er ferdig", Toast.LENGTH_SHORT).show()
            }
            override fun onTick(millisUntilFinished: Long) {
                //updateCountDownDisplay(millisUntilFinished)
                fun updateCountDownDisplay(timeInMs: Long) {
                    coutdownDisplay.text = millisecondsToDescriptiveTime(timeInMs)

                }
            }
        }
    }
}
      //  timer.start()