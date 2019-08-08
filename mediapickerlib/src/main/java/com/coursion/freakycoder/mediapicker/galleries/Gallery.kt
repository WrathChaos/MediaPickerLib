package com.coursion.freakycoder.mediapicker.galleries

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import com.coursion.freakycoder.mediapicker.fragments.ImageFragment
import com.coursion.freakycoder.mediapicker.fragments.VideoFragment
import com.coursion.freakycoder.mediapicker.helper.Util
import com.coursion.mediapickerlib.R
import kotlinx.android.synthetic.main.activity_gallery.*
import java.util.ArrayList

class Gallery : AppCompatActivity() {
    
    companion object {
        var selectionTitle: Int = 0
        var title: String? = null
        var maxSelection: Int = 0
        var mode: Int = 0
        var tabBarHidden = false
    }

    open lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_gallery)
        fab = findViewById(R.id.fab)
        // Set the toolbar
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.arrow_back)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        val util = Util()
        util.setButtonTint(fab, ContextCompat.getColorStateList(applicationContext, R.color.fabColor)!!)
        fab.setOnClickListener { returnResult() }

        title = intent.extras!!.getString("title")
        maxSelection = intent.extras!!.getInt("maxSelection")
        if (maxSelection == 0) maxSelection = Integer.MAX_VALUE
        mode = intent.extras!!.getInt("mode")
        tabBarHidden = intent.extras!!.getBoolean("tabBarHidden")
        title = title
        selectionTitle = 0
        // Set the ViewPager and TabLayout
        setupViewPager(viewPager)
        tabLayout!!.setupWithViewPager(viewPager)

        OpenGallery.selected.clear()
        OpenGallery.imagesSelected.clear()

    }

    override fun onPostResume() {
        super.onPostResume()
        if (selectionTitle > 0) {
            title = selectionTitle.toString()
        }
    }

    //This method set up the tab view for images and videos
    private fun setupViewPager(viewPager: androidx.viewpager.widget.ViewPager?) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        if (mode == 1 || mode == 2) {
            adapter.addFragment(ImageFragment(), "Images")
        }
        if (mode == 1 || mode == 3)
            adapter.addFragment(VideoFragment(), "Videos")
        viewPager!!.adapter = adapter

        if (tabBarHidden) {
            tabLayout.visibility = View.GONE
        } else {
            tabLayout.visibility = View.VISIBLE
        }
    }

    internal inner class ViewPagerAdapter(manager: androidx.fragment.app.FragmentManager) : androidx.fragment.app.FragmentPagerAdapter(manager) {
        private val mFragmentList = ArrayList<androidx.fragment.app.Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): androidx.fragment.app.Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: androidx.fragment.app.Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList[position]
        }
    }

    private fun returnResult() {
        val returnIntent = Intent()
        returnIntent.putStringArrayListExtra("result", OpenGallery.imagesSelected)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }
}
