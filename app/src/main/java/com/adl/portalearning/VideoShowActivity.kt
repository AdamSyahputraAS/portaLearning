package com.adl.portalearning

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

    private var videoUri: Uri? = null
    private lateinit var videoArrayList: ArrayList<ModelVideo>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_show)
//        playVideo.setOnClickListener({
//            val intent = Intent(this@VideoShowActivity, webViewPlay::class.java)
//            intent.putExtra("Videos",data)
//            startActivity(intent)
//        })
        setVideoToView()
    }



    fun setVideoToView() {
        //set the picked video to video view

        val videoUrl:String? = data.videoUri


        //video play controls
        val mediaController = MediaController(this)
        mediaController.setAnchorView(playVideo)
        val videoUri = Uri.parse(videoUrl)

        //set media controller
        playVideo.setMediaController(mediaController)
        //set video uri
        playVideo.setVideoURI(videoUri)
        playVideo.requestFocus()
        playVideo.setOnPreparedListener({
            //when video is ready, by default dont play automaticly
            playVideo.start()
        })
    }

}