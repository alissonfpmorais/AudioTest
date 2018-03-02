package br.com.dl.audiotest.main.business.effect

import br.com.dl.audiotest.main.business.model.AudioStatus

sealed class MainEffect {
    data class AttemptToPlay(val musicLocation: String, val status: AudioStatus): MainEffect()
}

typealias AttemptToPlay = MainEffect.AttemptToPlay