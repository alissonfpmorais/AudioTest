package br.com.dl.audiotest.main.business

import br.com.dl.audiotest.main.business.effect.AttemptToPlay
import br.com.dl.audiotest.main.business.effect.MainEffect
import br.com.dl.audiotest.main.business.event.MainEvent
import br.com.dl.audiotest.main.business.event.PlayButtonClicked
import br.com.dl.audiotest.main.business.event.PlayFailed
import br.com.dl.audiotest.main.business.event.PlaySuccessful
import br.com.dl.audiotest.main.business.model.Error
import br.com.dl.audiotest.main.business.model.MainModel
import br.com.dl.audiotest.main.business.model.Playing
import com.spotify.mobius.Effects.effects
import com.spotify.mobius.Next

fun mainUpdate(model: MainModel, event: MainEvent): Next<MainModel, MainEffect> {
    return when (event) {
        is PlayButtonClicked ->
            onPlayButtonClicked(model.copy(musicLocation = event.musicLocation))
        is PlaySuccessful -> onPlaySuccessful(model.copy(status = Playing))
        is PlayFailed -> onPlayFailed(model.copy(status = Error(event.msg)))
    }
}

fun onPlayButtonClicked(model: MainModel): Next<MainModel, MainEffect> = Next.next(model,
        effects(AttemptToPlay(model.musicLocation, model.status)))

fun onPlaySuccessful(model: MainModel): Next<MainModel, MainEffect> = Next.next(model)

fun onPlayFailed(model: MainModel): Next<MainModel, MainEffect> = Next.next(model)