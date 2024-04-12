package com.example.cra2go

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.cra2go.ui.theme.CRA2goTheme
import java.security.AccessController.getContext


class MainActivity : ComponentActivity() {

    private val TAG = "Main"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        val ctx : Context = this
        val url = "https://httpbin.org/get"
        var queue = Volley.newRequestQueue(ctx)
        val stringRequest = StringRequest(Request.Method.GET, url,
            { response ->
                Log.i(TAG, response.toString())
            },
            { error ->
                Log.i(TAG, error.toString())
            })




        queue.add(stringRequest)

        setContent {

            CRA2goTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                }
            }


        }
    }





}




