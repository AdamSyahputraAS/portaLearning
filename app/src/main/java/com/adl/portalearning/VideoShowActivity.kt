package com.adl.portalearning

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import com.adl.portalearning.model.ModelVideo
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_video_show.*

class VideoShowActivity : AppCompatActivity() {

    private lateinit var data: ModelVideo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_show)
        playVideo.setOnClickListener({
            val intent = Intent(this@VideoShowActivity, webViewPlay::class.java)
            intent.putExtra("Videos",data)
            startActivity(intent)
        })
    }
}