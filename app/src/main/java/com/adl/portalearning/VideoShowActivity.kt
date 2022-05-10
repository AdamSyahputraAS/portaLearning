package com.adl.portalearning

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.MediaController
import com.adl.portalearning.model.ModelVideo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_add_content.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_video_show.*

class VideoShowActivity : AppCompatActivity() {

    private lateinit var data: ModelVideo


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_show)
        if(intent.hasExtra("Videos")) {
            data = intent.getParcelableExtra("Videos")!!
            //isUpdate = true
            //setUIWithModel(data)
            videoDescription.setText(data.description)
            playVideo.setText(data.videoUri)
            txtJudul.setText(data.title)

        }
        playVideo.setOnClickListener{
            val intent = Intent(this@VideoShowActivity, webViewPlay::class.java)
            intent.putExtra("Videos", data)
            startActivity(intent)
        }
    }


}