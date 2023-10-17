package tg.holdemcasino.tgames.ui

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.widget.Toast
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import tg.holdemcasino.tgames.BaseApp
import tg.holdemcasino.tgames.databinding.ActivityMainBinding
import tg.holdemcasino.tgames.ui.viewmodel.GameViewModels
import tg.holdemcasino.tgames.utils.Constant


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val gameViewModels: GameViewModels by viewModels()
    var appSharedPref: SharedPreferences? = null
    private var bckExit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        appSharedPref = getSharedPreferences(BaseApp.appCode, MODE_PRIVATE)
        showWebviewContent()
        setContentView(binding.root)
    }
    @SuppressLint("SetJavaScriptEnabled")
    private fun showWebviewContent(){
        gameViewModels.fetchClientData()

        val webviewSettings = binding.wvApiContent
        webviewSettings.webChromeClient = WebChromeClient()
        webviewSettings.settings.loadsImagesAutomatically = true
        webviewSettings.settings.javaScriptEnabled = true
        webviewSettings.settings.domStorageEnabled = true
        webviewSettings.settings.allowFileAccess = true
        webviewSettings.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        webviewSettings.settings.allowFileAccess = true
        webviewSettings.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        webviewSettings.settings.setSupportMultipleWindows(true)
        webviewSettings.loadUrl(BaseApp.gameURL)
    }

    override fun onBackPressed() {
        if (bckExit) {
            super.finishAffinity()
            return
        }
        this.bckExit = true
        Toast.makeText(this, "Press back again to exit.", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ bckExit = false }, 2000)
    }
}