package com.coursion.freakycoder.mediapicker.adapters


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.Glide
import com.coursion.mediapickerlib.R

class MediaAdapter(private val bitmapList: List<String>,
                   private val selected: List<Boolean>,
                   private val context: Context):
        RecyclerView.Adapter<MediaAdapter.MyViewHolder>() {

    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(context)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = inflater.inflate(R.layout.media_item,
                parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load("file://" + bitmapList[position])
                .transition(DrawableTransitionOptions().crossFade())
                .apply(RequestOptions()
                        .centerCrop()
                        .dontAnimate()
                        .skipMemoryCache(true))
                .into(holder.thumbnail)
        if (selected[position]) {
            holder.check.visibility = View.VISIBLE
            holder.check.imageAlpha = 150
        } else {
            holder.check.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return bitmapList.size
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var thumbnail: ImageView
        var check: ImageView

        init {
            thumbnail = view.findViewById<View>(R.id.image) as ImageView
            check = view.findViewById<View>(R.id.image2) as ImageView
        }
    }

}

