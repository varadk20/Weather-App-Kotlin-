package com.example.weatherapp

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.karumi.dexter.BuildConfig

object Constants{
    //stored in local.properties and defined in build.gradle
    private val bosskey = com.example.weatherapp.BuildConfig.bosskey


    val APP_ID: String = bosskey
    const val BASE_URL:String = "https://api.openweathermap.org/data/"
    const val METRIC_UNIT:String = "metric"
    const val PREFERENCE_NAME = "WeatherAppPreference"
    const val WEATHER_RESPONSE_DATA = "Weather_response_data"



    fun isNetworkAvailable(context: Context): Boolean{

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as
                ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val network = connectivityManager.activeNetwork?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?:return false

            return when{
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)-> return true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)-> return true
                else -> return false
            }

        }else{ //old android versions
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnectedOrConnecting
        }
    }
}