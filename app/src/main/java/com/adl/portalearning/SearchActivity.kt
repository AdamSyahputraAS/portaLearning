package com.adl.portalearning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.adl.portalearning.adapter.SearchItemAdapter
import com.adl.portalearning.adapter.VideoAdapter
import com.adl.portalearning.model.ModelVideo
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.btnSearch
import kotlinx.android.synthetic.main.activity_main.txtSearch
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    lateinit var searchString:String

    lateinit var lstSearch:ArrayList<ModelVideo>

    lateinit var searchItemAdapter: SearchItemAdapter

    lateinit var database: DatabaseReference

    private lateinit var videoArrayList: ArrayList<ModelVideo>

    private lateinit var adapterVideo: VideoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        database= FirebaseDatabase.getInstance().reference.child("Videos")

        lstSearch = ArrayList<ModelVideo>()
        searchItemAdapter = SearchItemAdapter(lstSearch)

        btnSearchActivity.setOnClickListener{
            searchString = txtSearchActivity.text.toString()
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
        searchVideoRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = adapterVideo
        }
    }
}