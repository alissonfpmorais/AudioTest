package br.com.dl.audiotest.main.runtime.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import br.com.dl.audiotest.R
import br.com.dl.audiotest.R.id.playBt
import br.com.dl.audiotest.main.business.effect.MainEffect
import br.com.dl.audiotest.main.business.event.MainEvent
import br.com.dl.audiotest.main.business.event.PlayButtonClicked
import br.com.dl.audiotest.main.business.model.MainModel
import br.com.dl.audiotest.main.business.mainUpdate
import br.com.dl.audiotest.main.runtime.handler.mainHandler
import com.jakewharton.rxbinding2.view.RxView
import com.spotify.mobius.Mobius
import com.spotify.mobius.MobiusLoop
import com.spotify.mobius.rx2.RxConnectables
import com.spotify.mobius.rx2.RxMobius
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private val musicLocation = ""

    private val loop: MobiusLoop.Builder<MainModel, MainEvent, MainEffect> =
            RxMobius.loop(::mainUpdate, ::mainHandler)
    private val controller: MobiusLoop.Controller<MainModel, MainEvent> =
            Mobius.controller(loop, MainModel())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        controller.connect(RxConnectables.fromTransformer(this::mainUIHandler))
    }

    override fun onResume() {
        super.onResume()
        controller.start()
    }

    override fun onPause() {
        super.onPause()
        controller.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        controller.disconnect()
    }

    fun mainUIHandler(modelStream: Observable<MainModel>): Observable<MainEvent> {
        val disposable = modelStream
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {

                }

        val obsvPlayBt = RxView.clicks(playBt)
                .debounce(500, TimeUnit.MILLISECONDS)
                .map { PlayButtonClicked(musicLocation) }

        return Observable
                .merge(listOf())
                .doOnDispose(disposable::dispose)
    }
}
