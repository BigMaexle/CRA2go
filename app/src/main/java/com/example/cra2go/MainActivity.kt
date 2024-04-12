package com.example.cra2go

import android.os.Bundle
import android.util.Log
import android.widget.Button
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


class MainActivity : ComponentActivity() {

    private val TAG = "Main"

    private var mRequestQueue: RequestQueue? = null
    private var mStringRequest: StringRequest? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestQueue.add(stringRequest)

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

    val url = "https://api.example.com/data"
    var requestQueue = Volley.newRequestQueue(this.baseContext)

    val stringRequest = StringRequest(Request.Method.GET, url,
        { response ->
            // Display the first 500 characters of the response string.
            Log.i(TAG, "hat geklappt: ")
        },
        { Log.i(TAG, "hat nist geklappt: ") })


}




