package tg.holdemcasino.tgames.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionHelper (private val activity: Activity) {

    fun checkPermissions(): Boolean {
        val locationPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
        val cameraPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
        val mediaPermission = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        return locationPermission == PackageManager.PERMISSION_GRANTED
                && cameraPermission == PackageManager.PERMISSION_GRANTED
                && mediaPermission == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermissions() {
        val permissions = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_MEDIA_IMAGES
            )
        } else {
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
        ActivityCompat.requestPermissions(activity, permissions, Constant.PERMISSION_CODE)
    }

}