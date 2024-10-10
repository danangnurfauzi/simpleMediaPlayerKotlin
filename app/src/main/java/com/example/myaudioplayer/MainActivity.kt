package com.example.myaudioplayer

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myaudioplayer.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var isPlaying: Boolean = false
    var player: MediaPlayer? = null
    private lateinit var seekBar: SeekBar
    private lateinit var runnable: Runnable
    private var handler = Handler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        seekBar = findViewById(R.id.seekBar)

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    player!!.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Do something when touch starts, if needed
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Do something when touch stops, if needed
            }
        })

        binding.fabPlayOrPause.setOnClickListener {
            if (isPlaying) {
                if (player != null) {
                    player!!.pause()
                    isPlaying = false
                    binding.fabPlayOrPause.setImageResource(R.drawable.ic_play)
                }
            } else {
                if (player == null) {
                    player = MediaPlayer.create(this, R.raw.song)
                }
                player!!.start()
                isPlaying = true

                initializeSeekBar()
                with(binding) {
                    fabPlayOrPause.setImageResource(R.drawable.ic_pause)
                    fabStop.visibility = View.VISIBLE
                }

            }
        }

        binding.fabStop.setOnClickListener {
            seekBar.progress = 0
            stopPlayer()
            binding.fabStop.visibility = View.GONE
        }

        player?.setOnCompletionListener {
            stopPlayer()
            seekBar.progress = 0
        }

    }

    private fun stopPlayer() {
        if (player != null) {
            player!!.release()
            player = null
        }
        isPlaying = false
        seekBar.progress = 0
        binding.fabPlayOrPause.setImageResource(R.drawable.ic_play)
    }

    override fun onStop() {
        super.onStop()
        stopPlayer()
    }

    private fun initializeSeekBar() {
        seekBar.max = player!!.duration

        runnable = Runnable {
            seekBar.progress = player!!.currentPosition

            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
    }
}