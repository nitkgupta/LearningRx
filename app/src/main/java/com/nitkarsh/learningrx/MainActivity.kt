package com.nitkarsh.learningrx

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.nitkarsh.learningrx.restservices.Model
import com.nitkarsh.learningrx.restservices.RestClient
import com.nitkarsh.learningrx.restservices.RestServices
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import java.util.concurrent.Callable

class MainActivity : AppCompatActivity() {

    lateinit var call: Call<Model>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getSomeWorkDone()
        call= RestClient.getApiService().getRepos(userName = "nitkgupta")
        getSomeWorkDoneCallable()
    }

    fun getObservable() : Observable<String> {
        return Observable.just("Nitkarsh","Gupta")
    }

    private fun getObserver(): Observer<String> {
        return object : Observer<String> {

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(value: String) {
                Log.e("VALUE_OBSERVER",value)
            }

            override fun onError(e: Throwable) {

            }

            override fun onComplete() {
                println("onComplete")
            }
        }
    }

    private fun getSomeWorkDone() {
        getObservable()?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.subscribe(getObserver())
    }

    private fun getObservableCallable(): Observable<Model>? {
        return Observable.fromCallable(object: Callable<Model> {
            override fun call(): Model {
                if(call.isExecuted || call.isCanceled) {
                    return call.clone().execute().body()!!
                } else {
                    return call.execute().body()!!
                }
            }
        })
    }

    private fun getObserverCallable(): Observer<Model> {
        return object : Observer<Model> {
            override fun onComplete() {
                Log.e("OnComplete Callable","You are finished callable")
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: Model) {
                Log.e("Callable Complete",t.toString())
            }

            override fun onError(e: Throwable) {
            }
        }
    }

    private fun getSomeWorkDoneCallable() {
        getObservableCallable()?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.subscribe(getObserverCallable())
    }
}
