package com.adl.portalearning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import com.adl.portalearning.model.ModelVideo

class webViewPlay : AppCompatActivity() {
    lateinit var data: ModelVideo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view_play)
        val myWebView: WebView = findViewById(R.id.webview)
        if (intent.hasExtra("data")) {
            data = intent.getParcelableExtra("data")!!
            myWebView.loadUrl(data.videoUri.toString())
        }
    }
}