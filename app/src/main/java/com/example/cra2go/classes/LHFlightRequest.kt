package com.example.cra2go.classes

import android.graphics.ColorSpace.Model
import android.util.Log
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest

class LHFlightRequest (
    tag:String = "LHFlightRequest",
    url:String,

) {

    val stringRequest = object : StringRequest(Request.Method.GET, url,
        Response.Listener<String> { response ->
            Log.d(tag, "Response is sucessfull! ")

        },
        Response.ErrorListener { error ->
            Log.d(tag, error.toString())
        }) {
        override fun getHeaders(): MutableMap<String, String> {
            val headers = HashMap<String, String>()
            headers["Authorization"] = "Bearer 2bby9anhg533fgaxc8k3q4rj"
            headers["X-Originating-IP"] = "87.101.182.99"
            headers["Accept"] = "application/json"
            return headers
        }


    }
}