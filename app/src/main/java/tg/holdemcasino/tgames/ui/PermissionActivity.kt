package tg.holdemcasino.tgames.ui


import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.webkit.WebViewClient
import dagger.hilt.android.AndroidEntryPoint
import tg.holdemcasino.tgames.BaseApp
import tg.holdemcasino.tgames.R
import tg.holdemcasino.tgames.common.BaseActivity
import tg.holdemcasino.tgames.databinding.ActivityPermissionBinding
import tg.holdemcasino.tgames.utils.Constant
import tg.holdemcasino.tgames.utils.PermissionHelper


@AndroidEntryPoint
class PermissionActivity :
    BaseActivity<ActivityPermissionBinding>(
        ActivityPermissionBinding::inflate
    ) {

    private lateinit var permissionHelper: PermissionHelper
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        displayDataPolicy()

        permissionHelper = PermissionHelper(this)
        sharedPreferences = getSharedPreferences(BaseApp.appCode, Context.MODE_PRIVATE)

        // Check and request permissions when needed
        if (!permissionHelper.checkPermissions()) {
            permissionHelper.requestPermissions()
        }
    }

    private fun displayDataPolicy(){
        binding.wvLink.webViewClient = WebViewClient()
        binding.wvLink.loadUrl(BaseApp.policyURL)

        binding.btnAccept.setOnClickListener {
            if (!permissionHelper.checkPermissions()) {
                permissionHelper.requestPermissions()
            }
            else {
                userDataConsent()
            }
        }
        binding.btnDecline.setOnClickListener {
            val editSharedPref = sharedPreferences.edit()
            editSharedPref.putBoolean("runOnce", false)
            editSharedPref.apply()
            editSharedPref.commit()
            finishAffinity()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == Constant.PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                updateSharedPref(
                    location = true,
                    camera = true,
                    media = true,
                    firstRun = true
                )
            } else {
                // Permissions not granted
                updateSharedPref(
                    location = false,
                    camera = false,
                    media = false,
                    firstRun = false
                )
            }
        }
        userDataConsent()
    }

    private fun updateSharedPref(
        location: Boolean,
        camera: Boolean,
        media: Boolean,
        firstRun: Boolean
    ) {
        val sharedPreferences = getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)
        val editSharedPref = sharedPreferences.edit()

        editSharedPref.putBoolean("locationGranted", location)
        editSharedPref.putBoolean("cameraGranted", camera)
        editSharedPref.putBoolean("mediaGranted", media)
        editSharedPref.putBoolean("runOnce", firstRun)
        editSharedPref.apply()
        userDataConsent()
    }

    private fun userDataConsent() {
        val editSharedPref = sharedPreferences.edit()
        val permitSendData = sharedPreferences.getBoolean("permitSendData", false)
        if (!permitSendData) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("User Data Consent")
                .setMessage(getString(R.string.data_consent))
                .setPositiveButton(
                    "Accept"
                ) { allowDialog, _ ->
                    editSharedPref.putBoolean("permitSendData", true)
                    editSharedPref.apply()
                    allowDialog.dismiss()
                    jumpToActivity()
                }
                .setNegativeButton(
                    "Don't send Data"
                ) { dialog, _ ->
                    editSharedPref.putBoolean("permitSendData", false)
                    editSharedPref.apply()
                    dialog.dismiss()
                }
            builder.show()
        } else {
            jumpToActivity()
        }
    }

    private fun jumpToActivity() {
        val jumpIntent = Intent(this,MainActivity::class.java)
        jumpIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(jumpIntent)
        finish()
    }
}





