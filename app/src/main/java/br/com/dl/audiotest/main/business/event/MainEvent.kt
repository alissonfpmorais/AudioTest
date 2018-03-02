package br.com.dl.audiotest.main.business.event

sealed class MainEvent {
    data class PlayButtonClicked(val musicLocation: String): MainEvent()
    object PlaySuccessful: MainEvent()
    data class PlayFailed(val msg: String): MainEvent()
}

typealias PlayButtonClicked = MainEvent.PlayButtonClicked
typealias PlaySuccessful = MainEvent.PlaySuccessful
typealias PlayFailed = MainEvent.PlayFailed