
package de.powerradio.app

import android.app.Activity
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {

    private var player: MediaPlayer? = null
    private var playing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(32, 32, 32, 32)
            setBackgroundColor(0xFF17191D.toInt())
        }

        val title = TextView(this).apply {
            text = "POWER RADIO"
            textSize = 32f
            setTextColor(0xFF00C9EF.toInt())
            gravity = Gravity.CENTER
        }

        val status = TextView(this).apply {
            text = "Bereit zum Starten"
            textSize = 18f
            setTextColor(-1)
            gravity = Gravity.CENTER
        }

        val button = Button(this).apply {
            text = "▶ RADIO STARTEN"
        }

        layout.addView(title)
        layout.addView(status)
        layout.addView(button)
        setContentView(layout)

        button.setOnClickListener {
            if (playing) {
                player?.stop()
                player?.release()
                player = null
                playing = false
                button.text = "▶ RADIO STARTEN"
                status.text = "Radio gestoppt"
            } else {
                button.isEnabled = false
                status.text = "Verbinde mit POWER RADIO..."

                player = MediaPlayer().apply {
                    setDataSource("https://stream.laut.fm/powerradio")
                    setOnPreparedListener {
                        it.start()
                        playing = true
                        button.text = "Ⅱ PAUSE"
                        button.isEnabled = true
                        status.text = "POWER RADIO läuft"
                    }
                    setOnErrorListener { _, _, _ ->
                        playing = false
                        status.text = "Streamfehler"
                        button.isEnabled = true
                        button.text = "▶ RADIO STARTEN"
                        true
                    }
                    prepareAsync()
                }
            }
        }
    }

    override fun onDestroy() {
        player?.release()
        player = null
        super.onDestroy()
    }
}
