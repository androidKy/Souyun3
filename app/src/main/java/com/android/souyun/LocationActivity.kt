package com.android.souyun

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import com.safframework.log.L
import com.utils.common.PermissionUtils
import com.utils.common.ToastUtils
import kotlinx.android.synthetic.main.activity_location.*

class LocationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)


    }


    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            //mPermissionHelper.request(MULTI_PERMISSIONS);
            PermissionUtils.permission(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ).toList()
            )
                .callback(object : PermissionUtils.SimpleCallback {
                    override fun onDenied() {
                        ToastUtils.showToast(this@LocationActivity, "请授予定位权限")
                    }

                    override fun onGranted() {
                        getLocationInfo()
                    }
                })
                .request()
            return
        }
        getLocationInfo()
    }

    fun onLocationClicked(view: View) {
        getLocation()
    }

    private fun getLocationInfo() {
        val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            ToastUtils.showToast(this, "请开启GPS定位")
            return
        }

        val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

        lastLocation?.apply {
            L.d("latitude: $latitude, longitude: $longitude")
            tv_location.text = "$latitude,$longitude"
        }

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            10,
            0f,
            object : LocationListener {
                override fun onLocationChanged(location: Location?) {
                    location?.apply {
                        tv_location.text = "$latitude,$longitude"
                    }
                }

                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

                }

                override fun onProviderEnabled(provider: String?) {

                }

                override fun onProviderDisabled(provider: String?) {

                }
            })
    }
}
