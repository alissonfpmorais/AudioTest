package br.com.dl.audiotest.main.business.event

import android.content.Context
import io.reactivex.Observable

sealed class MainEvent {
    data class PlayButtonClicked(val musicLocation: String, val context: Context): MainEvent()
    data class PlaySuccessful(val currentPositionStream: Observable<Int>): MainEvent()
    data class PlayFailed(val msg: String): MainEvent()
}

typealias PlayButtonClicked = MainEvent.PlayButtonClicked
typealias PlaySuccessful = MainEvent.PlaySuccessful
typealias PlayFailed = MainEvent.PlayFailed