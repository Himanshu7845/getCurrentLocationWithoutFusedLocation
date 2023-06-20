package com.unorg.locationbygpt

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.unorg.locationbygpt.databinding.ActivityMainBinding
import java.io.IOException
import java.util.Locale

/*
 * Created by : Himanshu patel
 * Support :himanshu.patel@unorg.in
 * Last modified 3/25/23, 10:16 PM
 * Copyright (c) Unorg 2023.
 * All rights reserved.
 */

object PermissionUtils {
    /**
     * Function to request permission from the user
     */
    fun requestAccessFineLocationPermission(activity: AppCompatActivity, requestId: Int) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            requestId
        )
    }

    /**
     * Function to check if the location permissions are granted or not
     */
    fun isAccessFineLocationGranted(context: Context): Boolean {
        return ContextCompat
            .checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Function to check if location of the device is enabled or not
     */
    fun isLocationEnabled(context: Context): Boolean {
        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    /**
     * Function to show the "enable GPS" Dialog box
     */
    fun showGPSNotEnabledDialog(context: Context, binding: ActivityMainBinding) {
        binding.latTextView.text =""
        binding.lngTextView.text = ""
        AlertDialog.Builder(context)
            .setTitle("enable_gps")
            .setMessage("required_for_this_app")
            .setCancelable(false)
            .setPositiveButton("enable_now") { _, _ ->
                context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .show()
    }


    fun getAddress(context: Context, lat: Double, lng: Double, binding: ActivityMainBinding){
        val geocoder = Geocoder(context, Locale.getDefault())
        val latitude = lat // Example latitude
        val longitude = lng // Example longitude

        try {
            val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1) as List<Address>
            if (addresses.isNotEmpty()) {
                val address: Address = addresses[0]
                val addressLine: String = address.getAddressLine(0)
                val city: String = address.locality
                val state: String = address.adminArea
                val country: String = address.countryName
                val postalCode: String = address.postalCode
                binding.address.text=" $city, $state, $country,$postalCode"
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}