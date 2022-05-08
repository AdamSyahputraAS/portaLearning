package com.adl.portalearning

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.adl.portalearning.model.ModelVideo
import com.adl.ujiancrud.adapter.VideoAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //arraylist for videolist
    private lateinit var videoArrayList: ArrayList<ModelVideo>
    //adapter
    private lateinit var adapterVideo:VideoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //actionbar title
        title = "Videos"

        //function call to load videos from firebase
        loadVideosFromFirebase()

        //handle click
        btnAddNewVideoButton.setOnClickListener({
            startActivity(Intent(this, Add_Content_Activity::class.java))
        })

    }

    private fun loadVideosFromFirebase() {
        //init arraylist before adding data into it
        videoArrayList = ArrayList()

        //reference of firebase db
        val ref = FirebaseDatabase.getInstance().getReference("Videos")
        ref.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //clear list before adding data into it
                videoArrayList.clear()
                for (ds in snapshot.children){
                    //get data as model
                    val modelVideo = ds.getValue(ModelVideo::class.java)
                    //add to array list
                    videoArrayList.add(modelVideo!!)
                }
                //setup adapter
                adapterVideo = VideoAdapter(this@MainActivity, videoArrayList)
                //set adapter to recyclerview
                mainVideoRecyclerView.adapter = adapterVideo
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }

        )
    }


}