package br.com.dl.audiotest.main.runtime.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import br.com.dl.audiotest.R
import br.com.dl.audiotest.main.business.effect.MainEffect
import br.com.dl.audiotest.main.business.event.MainEvent
import br.com.dl.audiotest.main.business.event.PlayButtonClicked
import br.com.dl.audiotest.main.business.mainUpdate
import br.com.dl.audiotest.main.business.model.*
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
    lateinit var teste: String

    private val musicLocation = "/storage/emulated/0/Music/music.mp3"

    private val loop: MobiusLoop.Builder<MainModel, MainEvent, MainEffect> =
            RxMobius.loop(::mainUpdate, ::mainHandler)
    private val controller: MobiusLoop.Controller<MainModel, MainEvent> =
            Mobius.controller(loop, MainModel())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        Observable.fromArray(1, 2, 3, 4)
//                .map {
//                    if (it > 2) {
//                        teste = "SIM"
//                        "YES"
//                    } else {
//                        "NO"
//                    }
//                }
//                .repeatUntil { ::teste.isInitialized }
//                .subscribe {
//                    Log.d("Act", "Result: ${it.toString()}")
//                    if (::teste.isInitialized) Log.d("Act", "Teste: $teste")
//                    Log.d("Act", "----------------------")
//                }

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

    private fun mainUIHandler(modelStream: Observable<MainModel>): Observable<MainEvent> {
        val disposable = modelStream
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { render(it) }

        val obsvPlayBt = RxView.clicks(playBt)
                .debounce(500, TimeUnit.MILLISECONDS)
                .map { PlayButtonClicked(musicLocation, this.applicationContext) as MainEvent }

        return Observable
                .merge(listOf(obsvPlayBt))
                .doOnDispose(disposable::dispose)
    }

    private fun render(model: MainModel) {
        val prefix = resources.getString(R.string.status_lbl_prefix)
        val status = model.status

        when (status) {
            is Playing -> status.currentPositionStream
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { updateStatus(prefix + " " +
                            resources.getString(R.string.status_playing) + " " +
                            it.toString())
                    }
            is Recording -> updateStatus(prefix + resources.getString(R.string.status_recording))
            is None -> updateStatus(prefix + resources.getString(R.string.status_none))
            is Error -> updateStatus(prefix + status.msg)
        }
    }

    private fun updateStatus(msg: String) {
        statusLbl.text = msg
    }
}
