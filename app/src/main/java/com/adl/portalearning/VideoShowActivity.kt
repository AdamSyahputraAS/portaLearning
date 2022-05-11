package com.adl.portalearning

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.MediaController
import com.adl.portalearning.model.ModelVideo
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
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

            videoShowThumbnail?.let {
                Glide.with(this@VideoShowActivity)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .load(data.imageUri)
                    .into(it)


            }
            txtJudul.setText(data.title)
        }
        videoShowThumbnail.setOnClickListener{
            val intent = Intent(this@VideoShowActivity, webViewPlay::class.java)
            intent.putExtra("Videos", data)
            startActivity(intent)
        }
        txtRating.setOnClickListener {
            var dialog = RatingDialogFragment()
            dialog.show(supportFragmentManager,"ratingDialog")
        }
    }


}