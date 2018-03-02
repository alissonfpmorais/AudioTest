package br.com.dl.audiotest

import br.com.dl.audiotest.main.business.effect.AttemptToPlay
import br.com.dl.audiotest.main.business.effect.MainEffect
import br.com.dl.audiotest.main.business.event.PlayButtonClicked
import br.com.dl.audiotest.main.business.event.PlayFailed
import br.com.dl.audiotest.main.business.event.PlaySuccessful
import br.com.dl.audiotest.main.business.mainUpdate
import br.com.dl.audiotest.main.business.model.MainModel
import br.com.dl.audiotest.main.business.model.None
import br.com.dl.audiotest.main.business.model.Playing
import br.com.dl.audiotest.main.business.model.Error
import com.spotify.mobius.test.NextMatchers.*
import com.spotify.mobius.test.UpdateSpec
import com.spotify.mobius.test.UpdateSpec.assertThatNext
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class MainUnitTest {
    val spec = UpdateSpec(::mainUpdate)

    @Test
    fun checkAttemptToPlayStartWhenPlayButtonPressed() {
        val musicLocation = "path_to_audio_file"
        val model = MainModel()
        val effect = AttemptToPlay(musicLocation, None)

        spec.given(model)
                .`when`(PlayButtonClicked(musicLocation))
                .then(assertThatNext(
                        hasModel(model.copy(musicLocation = musicLocation)),
                        hasEffects(effect as MainEffect)))
    }

    @Test
    fun checkPlaySuccessfulShowInUI() {
        val model = MainModel()

        spec.given(model)
                .`when`(PlaySuccessful)
                .then(assertThatNext(
                        hasModel(model.copy(status = Playing)),
                        hasNoEffects()))
    }

    @Test
    fun checkPlayFailedShowInUI() {
        val model = MainModel()
        val errorMsg = "Audio file does not exist!"

        spec.given(model)
                .`when`(PlayFailed(errorMsg))
                .then(assertThatNext(
                        hasModel(model.copy(status = Error(errorMsg))),
                        hasNoEffects()))
    }
}
