package com.coursion.freakycoder.mediapicker.adapters


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.Glide
import com.coursion.freakycoder.mediapicker.helper.SquareLayout
import com.coursion.mediapickerlib.R

class BucketsAdapter(private val bucketNames: List<String>,
                     private val bitmapList: List<String>,
                     private val context: Context) :
        RecyclerView.Adapter<BucketsAdapter.MyViewHolder>() {

    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = inflater.inflate(R.layout.album_item,
                parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        bucketNames[position]
        holder.title.text = bucketNames[position]
        // Calculate and get the needed area for album title
        var mainHeight: Int
        var neededHeight: Int
        holder.sl.post {
            mainHeight = holder.sl.height
            holder.title.post {
                neededHeight = holder.title.height
                holder.thumbnail.layoutParams.height = mainHeight - neededHeight * 2
            }
       }

        Glide.with(context)
                .load("file://" + bitmapList[position])
                .apply(RequestOptions().centerCrop())
                .into(holder.thumbnail)
    }

    override fun getItemCount(): Int {
        return bucketNames.size
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView
        var thumbnail: ImageView
        var sl: SquareLayout

        init {
            title = view.findViewById(R.id.title)
            thumbnail = view.findViewById(R.id.image)
            sl = view.findViewById(R.id.sl)
        }
    }
}

