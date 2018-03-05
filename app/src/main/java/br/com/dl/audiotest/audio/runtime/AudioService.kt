package br.com.dl.audiotest.audio.runtime

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.util.Log
import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.core.getOrElse
import io.reactivex.Observable
import java.io.File
import java.util.concurrent.TimeUnit

class AudioService: Service() {
    companion object { const val AUDIO_EXTENSION = "mp3" }

    private val timeInterval = 16L

    private val audioService = this
    private var binder = AudioBinder()
    private val player: MediaPlayer = MediaPlayer()

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    fun startAudio(musicLocation: String): Observable<Int> {
        val musicUri = getMusicUri(musicLocation).getOrElse { Uri.fromFile(File("")) }

        disablePreviousAudioStreaming()
        player.setOnCompletionListener { it.reset() }
        player.setDataSource(this, musicUri)
        player.prepare()
        player.start()

        return Observable
                .interval(timeInterval, TimeUnit.MILLISECONDS)
                .map { player.currentPosition / 1000 }
                .repeatUntil { player.currentPosition == player.duration }
    }

    private fun getMusicUri(musicLocation: String): Option<Uri> {
        val file = File(musicLocation)

        return if (file.exists() && file.extension == AUDIO_EXTENSION) {
            val musicUri = Uri.fromFile(file)
            Some(musicUri)
        } else {
            None
        }
    }

    private fun disablePreviousAudioStreaming() {
        if (player.isPlaying) {
            player.stop()
            player.reset()
        }
    }

    inner class AudioBinder: Binder() {
        fun getService() = audioService
    }
}