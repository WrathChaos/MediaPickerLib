package com.coursion.freakycoder.mediapicker.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.coursion.freakycoder.mediapicker.adapters.BucketsAdapter
import com.coursion.freakycoder.mediapicker.galleries.OpenGallery
import com.coursion.mediapickerlib.R
import kotlinx.android.synthetic.main.fragment_image.*

import java.io.File
import java.util.ArrayList
import java.util.HashSet

class VideoFragment : Fragment() {

    companion object {
        var videosList: MutableList<String> = ArrayList()
        var selected: MutableList<Boolean> = ArrayList()
    }
    private lateinit var recyclerView: RecyclerView
    private var mAdapter: BucketsAdapter? = null
    private var bucketNames: MutableList<String> = ArrayList()
    private val bitmapList = ArrayList<String>()
    private val projection = arrayOf(MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media.DATA)
    private val projection2 = arrayOf(MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.DATA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Bucket names reloaded
        bucketNames.clear()
        bitmapList.clear()
        videosList.clear()
        getVideoBuckets()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_image, container, false)
        recyclerView = v.findViewById(R.id.recyclerView)
        populateRecyclerView()
        return v
    }

    private fun populateRecyclerView() {
        mAdapter = BucketsAdapter(bucketNames, bitmapList, context!!)
        val mLayoutManager = GridLayoutManager(context, 3)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = mAdapter
        recyclerView.addOnItemTouchListener(RecyclerTouchListener(context!!, recyclerView,
                object : ClickListener {
            override fun onClick(view: View, position: Int) {
                getVideos(bucketNames[position])
                val intent = Intent(context, OpenGallery::class.java)
                intent.putExtra("FROM", "Videos")
                startActivity(intent)
            }

            override fun onLongClick(view: View?, position: Int) {

            }
        }))
        mAdapter!!.notifyDataSetChanged()
    }

    fun getVideos(bucket: String) {
        selected.clear()
        val cursor = context!!.contentResolver
                .query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection2,
                        MediaStore.Video.Media.BUCKET_DISPLAY_NAME + " =?",
                        arrayOf(bucket), MediaStore.Video.Media.DATE_ADDED)
        var imagesTEMP: ArrayList<String>? = ArrayList(cursor!!.count)
        val albumSet = HashSet<String>()
        var file: File
        if (cursor.moveToLast()) {
            do {
                if (Thread.interrupted()) {
                    return
                }
                val path = cursor.getString(cursor.getColumnIndex(projection2[1]))
                file = File(path)
                if (file.exists() && !albumSet.contains(path)) {
                    imagesTEMP!!.add(path)
                    albumSet.add(path)
                    selected.add(false)
                }
            } while (cursor.moveToPrevious())
        }
        cursor.close()
        if (imagesTEMP == null) {

            imagesTEMP = ArrayList()
        }
        videosList.clear()
        videosList.addAll(imagesTEMP)
    }

    private fun getVideoBuckets() {
        val cursor = context!!.contentResolver
                .query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection,
                        null, null,
                        MediaStore.Video.Media.DATE_ADDED)
        val bucketNamesTEMP = ArrayList<String>(cursor!!.count)
        val bitmapListTEMP = ArrayList<String>(cursor.count)
        val albumSet = HashSet<String>()
        var file: File
        if (cursor.moveToLast()) {
            do {
                if (Thread.interrupted()) {
                    return
                }
                val album = cursor.getString(cursor.getColumnIndex(projection[0]))
                val image = cursor.getString(cursor.getColumnIndex(projection[1]))
                file = File(image)
                if (file.exists() && !albumSet.contains(album)) {
                    bucketNamesTEMP.add(album)
                    bitmapListTEMP.add(image)
                    albumSet.add(album)
                }
            } while (cursor.moveToPrevious())
        }
        cursor.close()
        if (bucketNamesTEMP.isNotEmpty()) {
            bucketNames = ArrayList()
        }
        bucketNames.clear()
        bitmapList.clear()
        bucketNames.addAll(bucketNamesTEMP)
        bitmapList.addAll(bitmapListTEMP)
    }

    interface ClickListener {
        fun onClick(view: View, position: Int)
        fun onLongClick(view: View?, position: Int)
    }

    class RecyclerTouchListener(context: Context, recyclerView: RecyclerView,
                                private val clickListener: VideoFragment.ClickListener?) :
            RecyclerView.OnItemTouchListener {
        private val gestureDetector: GestureDetector

        init {
            gestureDetector = GestureDetector(context, object :
                    GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    return true
                }

                override fun onLongPress(e: MotionEvent) {
                    val child = recyclerView.findChildViewUnder(e.x, e.y)
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child))
                    }
                }
            })
        }

        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            val child = rv.findChildViewUnder(e.x, e.y)
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child))
            }
            return false
        }

        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
    }

}