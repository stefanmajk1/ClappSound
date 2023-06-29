package com.example.clapapp

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    private lateinit var  seekBar: SeekBar
    private  var mediaPlayer: MediaPlayer? = null
    private lateinit var runnable: Runnable
    private lateinit var handeler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekBar = findViewById(R.id.sbClapping)
        handeler = Handler(Looper.getMainLooper())

       val playBtn = findViewById<FloatingActionButton>(R.id.fabPlay)
       val pauseBtn = findViewById<FloatingActionButton>(R.id.fabPause)
       val stopBtn = findViewById<FloatingActionButton>(R.id.fabStop)


        playBtn.setOnClickListener{
            if(mediaPlayer == null){
                mediaPlayer = MediaPlayer.create(this, R.raw.applauding)
                initializeSeekBar()
            }
            mediaPlayer?.start()
        }
        pauseBtn.setOnClickListener{
            mediaPlayer?.pause()
        }
        stopBtn.setOnClickListener{
            mediaPlayer?.stop()
            mediaPlayer?.reset()
            mediaPlayer?.release()
            mediaPlayer = null
            handeler.removeCallbacks(runnable)
            seekBar.progress = 0
        }
    }

    private fun initializeSeekBar(){
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) mediaPlayer?.seekTo(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
        val tvPlayed = findViewById<TextView>(R.id.tvPlayed)
        val tvDue = findViewById<TextView>(R.id.tvDue)
        seekBar.max = mediaPlayer!!.duration
        runnable = Runnable {
            seekBar.progress = mediaPlayer!!.currentPosition

            val playTime = mediaPlayer!!.currentPosition/1000
            tvPlayed.text = "$playTime sec"

            val duration = mediaPlayer!!.duration/1000
            val dueTime = duration-playTime
            tvDue.text = "$dueTime sec"

            handeler.postDelayed(runnable, 1000)
        }
        handeler.postDelayed(runnable, 1000)
    }
}