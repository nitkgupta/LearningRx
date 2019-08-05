package com.nitkarsh.learningrx.restservices

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Model constructor(@SerializedName("name") @Expose var name: String = "")