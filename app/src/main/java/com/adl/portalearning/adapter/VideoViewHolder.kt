package com.adl.portalearning.adapter

import android.content.Intent
import android.view.View
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.adl.portalearning.Add_Content_Activity
import com.adl.portalearning.VideoShowActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.item_main_activity.*
import kotlinx.android.synthetic.main.item_main_activity.view.*

class VideoViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val judul = view.labelJudulVideo
    val image = view.videoViewField
    val cardview = view.cardView
//    val edit = view.btnEditUser


    fun bindData(adapter:VideoAdapter, position:Int){

        judul.setText(adapter.data.get(position).title)
        image?.let {
            Glide.with(adapter.parent.context)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .load(adapter.data.get(position).imageUri)
                .into(it)


        }
        cardview.setOnClickListener({
            val intent = Intent(adapter.parent.context, VideoShowActivity::class.java)
            intent.putExtra("Videos", adapter.data.get(position))
            adapter.parent.context.startActivity(intent)
        })





//        edit.setOnClickListener({
//            val intent = Intent(adapter.parent.context, AddUser::class.java)
//            intent.putExtra("data",adapter.data.get(position))
//            adapter.parent.context.startActivity(intent)
//            adapter.parent.context
//        })

    }
}