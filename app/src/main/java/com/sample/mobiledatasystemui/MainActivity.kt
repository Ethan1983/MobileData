package com.sample.mobiledatasystemui

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var button : Button

    private val callback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            connectivityManager.bindProcessToNetwork(network)
            button.text = "Bound to Mobile Data"
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        button = findViewById<Button>(R.id.button1).also {
            it.setOnClickListener {
                connectivityManager.requestNetwork(networkRequest, callback)
                button.text = "In Progress"
                button.isEnabled = false
            }
        }

        with( findViewById<WebView>(R.id.webView)) {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            loadUrl("https://www.youtube.com")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        connectivityManager.unregisterNetworkCallback(callback)
    }
}
