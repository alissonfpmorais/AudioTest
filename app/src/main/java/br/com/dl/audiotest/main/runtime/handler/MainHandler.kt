package br.com.dl.audiotest.main.runtime.handler

import br.com.dl.audiotest.main.business.effect.MainEffect
import br.com.dl.audiotest.main.business.event.MainEvent
import io.reactivex.Observable

fun mainHandler(effectStream: Observable<MainEffect>): Observable<MainEvent> {
    TODO()
}