package br.com.dl.audiotest.main.runtime.handler

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.core.getOrElse
import br.com.dl.audiotest.main.business.effect.AttemptToPlay
import br.com.dl.audiotest.main.business.effect.MainEffect
import br.com.dl.audiotest.main.business.event.MainEvent
import br.com.dl.audiotest.main.business.event.PlayFailed
import br.com.dl.audiotest.main.business.event.PlaySuccessful
import br.com.dl.audiotest.main.business.model.AudioStatus
import io.reactivex.Observable
import java.io.File

const val AUDIO_EXTENSION = "mp3"
const val AUDIO_MESSAGE_ERROR = "Unknown error!"

fun mainHandler(effectStream: Observable<MainEffect>): Observable<MainEvent> {
    return effectStream.flatMap {
        when (it) {
            is AttemptToPlay -> onAttemptToPlay(it.musicLocation, it.status, it.context)
        }
    }
}

private fun onAttemptToPlay(musicLocation: String, status: AudioStatus, context: Context): Observable<MainEvent> {
    val musicUri = getMusicFile(musicLocation)

    val result = musicUri.map {
        val player = createSafePlayer(context, it)
        player.start()
        PlaySuccessful
    }

    return Observable.just(result.getOrElse { PlayFailed(AUDIO_MESSAGE_ERROR) })
}

private fun getMusicFile(musicLocation: String): Option<Uri> {
    val file = File(musicLocation)

    return if (file.exists() && file.extension == AUDIO_EXTENSION) {
        val musicUri = Uri.fromFile(file)
        Some(musicUri)
    } else {
        None
    }
}

private fun disablePreviousAudioStreaming(player: MediaPlayer) {
    if (player.isPlaying) { player.stop() }
}

fun createSafePlayer(context: Context, uri: Uri): MediaPlayer {
    val player = MediaPlayer.create(context, uri)
    disablePreviousAudioStreaming(player)

    return player
}