package com.adl.portalearning

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.adl.portalearning.adapter.SearchItemAdapter
import com.adl.portalearning.adapter.VideoAdapter
import com.adl.portalearning.model.ModelVideo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var searchString:String

    lateinit var lstSearch:ArrayList<ModelVideo>

    lateinit var searchItemAdapter:SearchItemAdapter

    lateinit var database: DatabaseReference

    //arraylist for videolist
    private lateinit var videoArrayList: ArrayList<ModelVideo>
    //adapter
    private lateinit var adapterVideo: VideoAdapter

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lstSearch = ArrayList<ModelVideo>()
        searchItemAdapter = SearchItemAdapter(lstSearch)

        btnSearch.setOnClickListener{
            searchString = txtSearch.text.toString()
            database.get().addOnCompleteListener { task->
                if (task.isSuccessful){
                    val snapshot = task.result
                    videoArrayList.clear()
                    for (data in snapshot.children){
                        val modelVideo = data.getValue(ModelVideo::class.java)
                        //add to array list
                        videoArrayList.add(modelVideo!!)
                    }
                    Search(videoArrayList)
                }else{
                    Log.d("TAG", task.exception!!.message!!)
                }
            }
        }

        auth = FirebaseAuth.getInstance()

        //actionbar title
        title = "Videos"

        //function call to load videos from firebase
        loadVideosFromFirebase()



        //handle click
        btnAddNewVideoButton.setOnClickListener({
            startActivity(Intent(this, Add_Content_Activity::class.java))
        })

        btnLogout.setOnClickListener {
            auth.signOut()
            Intent (this@MainActivity, login_activity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)}
        }

    }


    fun Search(list:ArrayList<ModelVideo>){
        val pattern = searchString.toRegex((RegexOption.IGNORE_CASE))
        lstSearch.clear()
        for (dataItem in list){
            if (pattern.containsMatchIn(dataItem.title!!)){
                println("${dataItem.title}matches")
                val data:String=dataItem.title.toString()
                lstSearch.add(ModelVideo(dataItem.id,dataItem.title,dataItem.description,dataItem.timestamp,dataItem.videoUri,dataItem.imageUri,dataItem.rating,dataItem.uid))
            }
        }
        searchItemAdapter.notifyDataSetChanged()
        mainVideoRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = adapterVideo
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