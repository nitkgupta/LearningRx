package com.nitkarsh.learningrx.restservices

import com.nitkarsh.learningrx.restservices.Model
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RestServices {

    @GET("/users/{userName}/repos")
    fun getRepos(@Path(value = "userName") userName: String) : Call<Model>

    @GET("/search/users")
    fun searchUsers(@Query("q") text: String, @Query("page") page: Int) : Call<Model>

    @GET("/users/{userName}")
    fun getUserData(@Path(value = "userName", encoded = true) userName: String): Call<Model>

}