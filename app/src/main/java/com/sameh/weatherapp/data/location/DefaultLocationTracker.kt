package com.sameh.weatherapp.data.location

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.sameh.weatherapp.domain.location.ILocationTracker
import com.sameh.weatherapp.utils.toLogD
import com.sameh.weatherapp.utils.toLogE
import com.sameh.weatherapp.utils.toLogW
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class DefaultLocationTracker @Inject constructor(
    private val locationClient: FusedLocationProviderClient,
    private val application: Application
) : ILocationTracker {

    override suspend fun getCurrentLocation(): Location? {
        val hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(
            application,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val hasAccessCoarseLocationPermission = ContextCompat.checkSelfPermission(
            application,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val locationManager =
            application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (!hasAccessCoarseLocationPermission || !hasAccessFineLocationPermission || !isGpsEnabled) {
            "return null".toLogW()
            return null
        }

        return suspendCancellableCoroutine { cont ->
            locationClient.lastLocation.apply {
                if (isComplete) {
                    "isComplete".toLogD()
                    if (isSuccessful) {
                        "isSuccessful: $result".toLogD()
                        cont.resume(result)
                    } else {
                        "notSuccessful".toLogW()
                        cont.resume(null)
                    }
                    return@suspendCancellableCoroutine
                }
                addOnSuccessListener {
                    "addOnSuccessListener: $it".toLogD()
                    cont.resume(it)
                }
                addOnFailureListener {
                    "addOnFailureListener: $it".toLogE()
                    cont.resume(null)
                }
                addOnCanceledListener {
                    "addOnCanceledListener: ".toLogW()
                    cont.cancel()
                }
            }
        }
    }
}