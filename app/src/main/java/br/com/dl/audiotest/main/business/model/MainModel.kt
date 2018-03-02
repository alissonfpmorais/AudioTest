package br.com.dl.audiotest.main.business.model

sealed class AudioStatus {
    object Playing: AudioStatus()
    object Recording: AudioStatus()
    object None: AudioStatus()
    data class Error(val msg: String): AudioStatus()
}

typealias Playing = AudioStatus.Playing
typealias Recording = AudioStatus.Playing
typealias None = AudioStatus.Playing
typealias Error = AudioStatus.Error

data class MainModel(val musicLocation: String = "",
                     val status: AudioStatus = None)