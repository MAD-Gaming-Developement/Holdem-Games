package tg.holdemcasino.tgames

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class BaseApp : Application() {

    companion object {
        const val appCode = "TG12102"
        var apiURL = ""
        var policyURL = ""
        var gameURL = ""
        var jsInterface = ""
    }
    lateinit var sharedPref: SharedPreferences
    override fun onCreate() {
        super.onCreate()
        sharedPref = getSharedPreferences(appCode, Context.MODE_PRIVATE)
    }
}