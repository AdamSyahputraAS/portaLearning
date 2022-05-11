package com.adl.portalearning

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.adl.portalearning.adapter.VideoAdapter
import com.adl.portalearning.model.ModelVideo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //arraylist for videolist
    private lateinit var videoArrayList: ArrayList<ModelVideo>
    //adapter
    private lateinit var adapterVideo: VideoAdapter
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        auth = FirebaseAuth.getInstance()

        //actionbar title
        title = "Videos"

        //function call to load videos from firebase
        loadVideosFromFirebase()

        btnSearch.setOnClickListener({
            startActivity(Intent(this, SearchActivity::class.java))
        })

//        handle click
//        btnAddNewVideoButton.setOnClickListener({
//            startActivity(Intent(this, Add_Content_Activity::class.java))
//        })

        btnLogout.setOnClickListener {
            auth.signOut()
            Intent (this@MainActivity, login_activity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)}
        }

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
//               mainVideoRecyclerView.adapter = adapterVideo
                mainVideoRecyclerView.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity)
                    adapter = adapterVideo
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }

        )
    }


}