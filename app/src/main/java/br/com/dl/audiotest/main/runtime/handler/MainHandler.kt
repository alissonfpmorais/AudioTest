package br.com.dl.audiotest.main.runtime.handler

import android.content.Context
import android.content.Intent
import br.com.dl.audiotest.audio.runtime.AudioService
import br.com.dl.audiotest.main.business.effect.AttemptToPlay
import br.com.dl.audiotest.main.business.effect.MainEffect
import br.com.dl.audiotest.main.business.event.MainEvent
import br.com.dl.audiotest.main.business.model.AudioStatus
import io.reactivex.Observable
import android.os.IBinder
import android.content.ComponentName
import android.content.ServiceConnection
import br.com.dl.audiotest.main.business.event.PlaySuccessful
import io.reactivex.schedulers.Schedulers

lateinit var audioService: AudioService

fun mainHandler(effectStream: Observable<MainEffect>): Observable<MainEvent> {
    return effectStream.flatMap {
        when (it) {
            is AttemptToPlay -> onAttemptToPlay(it.musicLocation, it.status, it.context)
        }
    }
}

private fun onAttemptToPlay(musicLocation: String, status: AudioStatus, context: Context): Observable<MainEvent> {
    val audioIntent = Intent(context, AudioService::class.java)
    context.startService(audioIntent)
    context.bindService(audioIntent, serviceConnection, Context.BIND_AUTO_CREATE)

    return Observable.just(1)
            .observeOn(Schedulers.io())
            .map {
                Observable.empty<Int>()
                        .repeatUntil { ::audioService.isInitialized }
                        .subscribe {  }

                PlaySuccessful(audioService.startAudio(musicLocation))
            }
}

private val serviceConnection = object : ServiceConnection {
    override fun onServiceDisconnected(name: ComponentName) {
//        mServiceBound = false
    }

    override fun onServiceConnected(name: ComponentName, service: IBinder) {
        val audioBinder = service as AudioService.AudioBinder
        audioService = audioBinder.getService()
//        mServiceBound = true
    }
}