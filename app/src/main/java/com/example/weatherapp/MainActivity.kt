package com.example.weatherapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.location.LocationRequest
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.getSystemService
import androidx.core.location.LocationRequestCompat
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class MainActivity : ComponentActivity() {

    private lateinit var mFusedLocationClient : FusedLocationProviderClient //to get latitude and longitude


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (!isLocationEnabled()){
            Toast.makeText(
                this,
                "Your location provider is turned off",
                Toast.LENGTH_SHORT
            ).show()

            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }
        else{
            Dexter.withContext(this).withPermissions(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
                .withListener(object : MultiplePermissionsListener{
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if(report!!.areAllPermissionsGranted()){
                            requestLocationData()
                        }

                        if (report.isAnyPermissionPermanentlyDenied){
                            Toast.makeText(
                                this@MainActivity,
                                "You have denied location permission. Please enable for app to work",
                                Toast.LENGTH_SHORT

                            ).show()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<PermissionRequest>?,
                        p1: PermissionToken?
                    ) {
                        showRationalDialogForPermission()
                    }

                }).onSameThread().check()

        }

    }


    @SuppressLint("MissingPermission")
    private fun requestLocationData(){

        val mLocationRequest = com.google.android.gms.location.LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,1000).build()


        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest,mLocationCallback,
            Looper.myLooper()
        )


    }


    private val mLocationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location? = locationResult.lastLocation
            val latitude = mLastLocation?.latitude
            Log.i("Current Latitude", "$latitude")

            val longitude = mLastLocation?.longitude
            Log.i("Current Latitude", "$longitude")
        }
    }





    private fun showRationalDialogForPermission(){
        AlertDialog.Builder(this)
            .setMessage("It looks like you turned off the permissions")
            .setPositiveButton("Go to seetings"
            ){ _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }catch (e: ActivityNotFoundException){
                    e.printStackTrace()
                }
            }

            .setNegativeButton("Cancel"){dialog,
                                        _ ->
                dialog.dismiss()

            }.show()

    }

    

    private fun isLocationEnabled(): Boolean{
        val locationManager: LocationManager=
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherAppTheme {
        Greeting("Android")
    }
}