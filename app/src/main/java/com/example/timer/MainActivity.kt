package com.example.timer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var startButton: Button
    private lateinit var timeTextView: TextView
    private lateinit var texts: TextView
    private lateinit var endButton: Button
    private lateinit var addButton: Button
    private lateinit var deleteButton: Button
    private lateinit var listView: ListView
    private lateinit var timerHandler: Handler
    private lateinit var timerRunnable: Runnable

    private var timerRunning = false
    private var secondsElapsed = 0
    private val tools: MutableList<String> = mutableListOf()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startButton = findViewById(R.id.startButton)
        timeTextView = findViewById(R.id.timeTextView)
        texts = findViewById(R.id.endButton)
        endButton = findViewById(R.id.resetButton)
        addButton = findViewById(R.id.ButtonAdd)
        deleteButton = findViewById(R.id.ButtonEnd)
        listView = findViewById(R.id.Abv)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_activated_1, tools)
        listView.adapter = adapter

        startButton.setOnClickListener {
            if (!timerRunning) {
                startTimer()
            }
        }

        timerHandler = Handler(Looper.getMainLooper())
        timerRunnable = object : Runnable {
            override fun run() {
                if (timerRunning) {
                    secondsElapsed++
                    updateTimeUI()
                    timerHandler.postDelayed(this, 1000)
                }
            }
        }

        endButton.setOnClickListener {
            if (timerRunning) {
                timerRunning = false
                timerHandler.removeCallbacks(timerRunnable)
                texts.text = timeTextView.text
            }
        }

        deleteButton.setOnClickListener {
            texts.text = "00:00:00"
        }

        addButton.setOnClickListener {
            if (!timerRunning) {
                val textswe = texts.text.toString()
                adapter.insert(textswe, 0)
            }
        }

        listView.setOnItemClickListener { parent, view, position, id ->
            val item = adapter.getItem(position)
            adapter.remove(item)
        }

        // Відновлення стану
        if (savedInstanceState != null) {
            timerRunning = savedInstanceState.getBoolean("timerRunning")
            secondsElapsed = savedInstanceState.getInt("secondsElapsed")
            updateTimeUI()
            if (timerRunning) {
                startTimer()
            }
            val savedList = savedInstanceState.getStringArrayList("tools")
            if (savedList != null) {
                tools.clear()
                tools.addAll(savedList)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun startTimer() {
        timerRunning = true
        timerHandler.postDelayed(timerRunnable, 0)
    }

    private fun updateTimeUI() {
        val hours = secondsElapsed / 3600
        val minutes = (secondsElapsed % 3600) / 60
        val seconds = secondsElapsed % 60
        timeTextView.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("timerRunning", timerRunning)
        outState.putInt("secondsElapsed", secondsElapsed)
        outState.putStringArrayList("tools", ArrayList(tools))
    }
}
