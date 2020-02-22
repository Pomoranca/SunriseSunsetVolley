package com.example.restapiexample

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException


class MainActivity : AppCompatActivity() {
    private var mQueue: RequestQueue? = null
    private var mJSONURLString = ""
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Start location service
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        getLastKnownLocation()

        //Queue request
        mQueue = Volley.newRequestQueue(this)


        //Click to parse
        button_parse.setOnClickListener {
            jsonParse()

        }
    }

    //Get sunrise and sunset from API
    private fun jsonParse() {
        val jsonArrayRequest = JsonObjectRequest(
                Request.Method.GET,
                mJSONURLString,
                null,
                Response.Listener { response ->
                    try {
                        //Create result object
                        val result = response.getJSONObject("results")

                        //Create vals sunrise and sunset
                        val sunrise = result.getString("sunrise")
                        val sunset = result.getString("sunset")

                        //Edit textView
                        api_text.text = "Sunrise: $sunrise \n Sunset: $sunset"

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { }
        )
        //add API Request
        mQueue!!.add(jsonArrayRequest)
    }

    //Get last known location
    private fun getLastKnownLocation() {
        fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        //get latitudu
                        val latitude = location.latitude.toString()

                        //get longitudu
                        val longitude = location.longitude.toString()

                        //String for API get request
                        mJSONURLString = "https://api.sunrise-sunset.org/json?lat=$latitude&lng=$longitude"

                    }
                }
    }

}
