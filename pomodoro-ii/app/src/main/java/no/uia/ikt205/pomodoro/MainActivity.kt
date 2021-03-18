package no.uia.ikt205.pomodoro

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import no.uia.ikt205.pomodoro.util.millisecondsToDescriptiveTime
import no.uia.ikt205.pomodoro.util.minutesToMilliSeconds
import java.lang.NumberFormatException

class MainActivity : AppCompatActivity() {

    lateinit var timer: CountDownTimer
    lateinit var startButton: Button

    lateinit var barWorkTimeDuration: SeekBar
    lateinit var barPauseTimeDuration: SeekBar

    lateinit var editSelectRepetitions: EditText

    lateinit var workTimeDurationScreen: TextView
    lateinit var pauseTimeDurationScreen: TextView
    lateinit var workMayPause: TextView
    lateinit var coutdownDisplay: TextView
    lateinit var quantityRepetitionsDisplay: TextView

    var timeToCountDownInMsStart = 0L
    val minute = 60000L
    var workTime = 15 * minute
    var pauseTime = 0L
    var quantityRepetitions = 1
    var startButtunFunksjon = false
    var backToWork = true
    val timeTicks = 1000L
    var counter = 0
    var repetitionsDefault = 0

    // private var stopTimer:Boolean = false //Vil stoppe timeren

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        barWorkTimeDuration = findViewById<SeekBar>(R.id.BarWorkTimeDuration)
        barPauseTimeDuration = findViewById<SeekBar>(R.id.BarPauseTimeDuration)

        startButton = findViewById<Button>(R.id.startCountdownButton)

        editSelectRepetitions = findViewById<EditText>(R.id.editSelectRepetitions)

        workMayPause = findViewById<TextView>(R.id.workMayPause)
        workTimeDurationScreen = findViewById<TextView>(R.id.workTimeDuration)
        pauseTimeDurationScreen = findViewById<TextView>(R.id.pauseTimeDuration)
        quantityRepetitionsDisplay = findViewById<TextView>(R.id.quantityRepetitionsDisplay)
        coutdownDisplay = findViewById<TextView>(R.id.countDownView)

        editSelectRepetitions.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                //TODO("Not yet implemented")
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                quantityRepetitions = try {
                    editSelectRepetitions.text.toString().toInt()
                } catch (nfe: NumberFormatException) {
                    android.widget.Toast.makeText(this@MainActivity, "No integer", android.widget.Toast.LENGTH_SHORT).show()
                    0
                }
            }
        })

        //Work time duration
        barWorkTimeDuration.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(a0: SeekBar?, a1: Int, a2: Boolean) {
                workTime = barWorkTimeDuration.progress.toLong() * minute
                workTimeDurationScreen.text = millisecondsToDescriptiveTime(workTime)
            }

            override fun onStartTrackingTouch(a0: SeekBar?) {

            }

            override fun onStopTrackingTouch(a0: SeekBar?) {
                Toast.makeText(this@MainActivity, "Current work time! ${barWorkTimeDuration.progress}", Toast.LENGTH_SHORT).show()
            }
        })

        //Pause time duration
        barPauseTimeDuration.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(a0: SeekBar?, a1: Int, a2: Boolean) {
                pauseTime = barPauseTimeDuration.progress.toLong() * minute
                pauseTimeDurationScreen.text = millisecondsToDescriptiveTime(pauseTime)
            }

            override fun onStartTrackingTouch(a0: SeekBar?) {

            }

            override fun onStopTrackingTouch(a0: SeekBar?) {
                Toast.makeText(this@MainActivity, "Current Pause time! ${barPauseTimeDuration.progress}", Toast.LENGTH_SHORT).show()
            }
        })

        startButton.setOnClickListener() {
            if (!startButtunFunksjon) {
                startButtunFunksjon = true
                backToWork = true
                quantityRepetitionsDisplay.text = "Quantity Repetitions: " + (quantityRepetitions)
                counter = 1
                timeToCountDownInMsStart = workTime
                workMayPause.text = "Work"
                startButton.text = "Stop"

                startCountDown()
            } else {
                timer.cancel()
                startButtunFunksjon = false
                startButton.text = "Start"
                timeToCountDownInMsStart = 0L
                updateDisplay(timeToCountDownInMsStart)
                quantityRepetitionsDisplay.text = ""

            }
        }

        coutdownDisplay = findViewById<TextView>(R.id.countDownView)
    }

    fun startCountDown() {
        if (quantityRepetitions == 0) {
            startButtunFunksjon = false
            counter = 0
            Toast.makeText(this@MainActivity, "Wrong input", Toast.LENGTH_SHORT).show()
            workMayPause.text = ""
        }
        else {
            timer = object : CountDownTimer(timeToCountDownInMsStart, timeTicks) {
                @SuppressLint("SetTextI18n")
                override fun onFinish() {

                    if (counter == quantityRepetitions * 2 - 1) {
                        startButtunFunksjon = false
                        counter = 0
                        Toast.makeText(this@MainActivity,"The last job is done man!", Toast.LENGTH_SHORT).show()
                        workMayPause.text = ""
                        quantityRepetitionsDisplay.text = ""
                    }
                    else if (backToWork) {
                        Toast.makeText(this@MainActivity,"The job is done man!", Toast.LENGTH_SHORT).show()
                        timeToCountDownInMsStart = pauseTime
                        backToWork = false
                        counter += 1
                        workMayPause.text = "Rest"
                        startCountDown()
                    }
                    else {
                        Toast.makeText(this@MainActivity, "Get back to work", Toast.LENGTH_SHORT).show()
                        timeToCountDownInMsStart = workTime
                        backToWork = true
                        counter += 1
                        repetitionsDefault += 1
                        workMayPause.text = "Working"
                        quantityRepetitionsDisplay.text = "Repetitions: " + (quantityRepetitions - repetitionsDefault)
                        startCountDown()
                    }
                }
                override fun onTick(millisUntilFinished: Long) {
                    updateDisplay(millisUntilFinished)
                }
            }
            timer.start()
        }
    }
    fun updateDisplay(timemillisec: Long) {
        coutdownDisplay.text = millisecondsToDescriptiveTime(timemillisec)
    }
}