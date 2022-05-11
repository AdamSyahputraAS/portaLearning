package com.adl.portalearning.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adl.portalearning.MainActivity
import com.adl.portalearning.R
import com.adl.portalearning.model.ModelVideo

class SearchItemAdapter(data1: ArrayList<ModelVideo>, val data: ArrayList<ModelVideo>): RecyclerView.Adapter<SearchItemViewHolder>() {
    lateinit var parent: ViewGroup
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemViewHolder {
        this.parent = parent

        return SearchItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_search,parent,false))
    }

    override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) {
        holder.bindData(this@SearchItemAdapter,position)
    }

    override fun getItemCount(): Int {
        return data.size
    }


}