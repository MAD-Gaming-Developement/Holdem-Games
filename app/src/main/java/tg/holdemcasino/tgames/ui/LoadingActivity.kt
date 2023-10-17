package tg.holdemcasino.tgames.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import dagger.hilt.android.AndroidEntryPoint
import tg.holdemcasino.tgames.BaseApp
import tg.holdemcasino.tgames.R
import tg.holdemcasino.tgames.common.BaseActivity
import tg.holdemcasino.tgames.databinding.ActivityPermissionBinding
import tg.holdemcasino.tgames.utils.Constant

@AndroidEntryPoint
class LoadingActivity :  BaseActivity<ActivityPermissionBinding>(
    ActivityPermissionBinding::inflate
) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpFirebaseConfig()
    }

    private fun setUpFirebaseConfig() {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(3600)
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.default_config)
        remoteConfig.fetchAndActivate().addOnCompleteListener(this) { task: Task<Boolean> ->
            if (task.isSuccessful) {
                val updated = task.result
                Toast.makeText(this, "Able to get the config", Toast.LENGTH_SHORT).show()
                Log.d(
                    "FirebaseConfig",
                    "Remote Config updated:$updated"
                )
            } else {
                Toast.makeText(this, "Unable to get the config", Toast.LENGTH_SHORT).show()
                Log.d(
                    "FirebaseConfig",
                    "Unable to get Config succesfully"
                )
            }
            fetchConfig()
        }

        remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                fetchConfig()
                Log.d(
                    "FirebaseConfig",
                    "Updated Config:$configUpdate"
                )
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                fetchConfig()
                error.localizedMessage?.let {
                    Log.d(
                        "FirebaseConfig",
                        it
                    )
                }
            }
        })
    }

    private fun fetchConfig() {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        BaseApp.apiURL = remoteConfig.getString("apiURL")
        BaseApp.gameURL = remoteConfig.getString("gameURL")
        BaseApp.jsInterface = remoteConfig.getString("jsInterface")
        BaseApp.policyURL = remoteConfig.getString("policyURL")
        jumpToActivity()
    }

    private fun jumpToActivity() {
        val sharedPref = getSharedPreferences(BaseApp.appCode, MODE_PRIVATE)
        if (sharedPref.getBoolean("runOnce", false)) {
            val webApp = Intent(this, MainActivity::class.java)
            webApp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(webApp)
            finish()

        } else {
            val dataPolicy = Intent(this, PermissionActivity::class.java)
            dataPolicy.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(dataPolicy)
            finish()
        }
    }
}