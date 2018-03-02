package br.com.dl.audiotest.main.business.effect

import android.content.Context
import br.com.dl.audiotest.main.business.model.AudioStatus

sealed class MainEffect {
    data class AttemptToPlay(val musicLocation: String, val status: AudioStatus, val context: Context): MainEffect()
}

typealias AttemptToPlay = MainEffect.AttemptToPlay