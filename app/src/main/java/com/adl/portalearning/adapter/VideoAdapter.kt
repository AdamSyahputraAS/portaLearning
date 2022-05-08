package com.adl.portalearning.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adl.portalearning.MainActivity
import com.adl.portalearning.R
import com.adl.portalearning.model.ModelVideo

class VideoAdapter(data1: MainActivity, val data: ArrayList<ModelVideo>): RecyclerView.Adapter<VideoViewHolder>() {
    lateinit var parent: ViewGroup
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        this.parent = parent

        return VideoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_main_activity,parent,false))
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bindData(this@VideoAdapter,position)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}