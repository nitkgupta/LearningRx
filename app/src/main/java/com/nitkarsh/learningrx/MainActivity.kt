package com.nitkarsh.learningrx

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.nitkarsh.learningrx.restservices.Model
import com.nitkarsh.learningrx.restservices.RestClient
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import java.util.concurrent.Callable

class MainActivity : AppCompatActivity() {

    lateinit var call1: Call<List<Model>>
    lateinit var call2: Call<List<Model>>
    var list = arrayListOf<Model>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getSomeWorkDone()
        call1 = RestClient.getApiService().getRepos(userName = "nitkgupta")
        call2 = RestClient.getApiService().getRepos(userName = "nitkgupta")
        getSomeWorkDoneCallable()
        getSomeWorkDoneAgain()
    }

    fun getObservable(): Observable<String> {
        return Observable.just("Nitkarsh", "Gupta")
    }

    /**
     * getting simple just for emitting single value at a time
     */
    private fun getObserver(): Observer<String> {
        return object : Observer<String> {

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(value: String) {
                Log.e("VALUE_OBSERVER", value)
            }

            override fun onError(e: Throwable) {

            }

            override fun onComplete() {
                println("onComplete")
            }
        }
    }

    private fun getSomeWorkDone() {
        getObservable().subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(getObserver())
    }

    /**
     * getting observable with callbale type
     */
    private fun getObservableCallable(): Observable<List<Model>>? {
        return Observable.fromCallable(object : Callable<List<Model>> {
            override fun call(): List<Model> {
                if (call1.isExecuted || call1.isCanceled) {
                    return call1.clone().execute().body()!!
                } else {
                    return call1.execute().body()!!
                }
            }
        })
    }

    private fun getObserverCallable(): Observer<List<Model>> {
        return object : Observer<List<Model>> {
            override fun onComplete() {
                Log.e("OnComplete Callable", "You are finished callable")
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: List<Model>) {
                Log.e("Callable Complete", t.toString())
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    /**
     * this will call the observable with mapping
     */
    private fun getSomeWorkDoneCallable() {
        getObservableCallable()?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.map {
            list.addAll(it)
            Log.e("List_size", list.size.toString())
            list
        }?.subscribe(getObserverCallable())
    }

    /**
     * Observable fir fetching user info
     */
     fun getObservable3WithApi(): Observable<List<Model>> {
        return Observable.fromCallable(object: Callable<List<Model>> {
            override fun call(): List<Model> {
                return if (call2.isExecuted || call2.isCanceled) call2.clone().execute().body()!! else call2.execute().body()!!
            }
        })
    }

    /**
     * Zip operator. It combines the result from two and combines to perform some operation and return the result
     */
    private fun getSomeWorkDoneAgain() {
        Observable.zip(getObservableCallable(),getObservable3WithApi(),object: BiFunction<List<Model>,List<Model>,List<Model>> {
            override fun apply(t1: List<Model>, t2: List<Model>): List<Model> {
                return arrayListOf<Model>().apply {
                    addAll(t1)
                    addAll(t2)
                }
            }
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getObserverCallable())
    }
}
