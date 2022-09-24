package com.udacity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*


class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val actionBar: ActionBar? = supportActionBar

        // showing the back button in action bar

        // showing the back button in action bar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        Log.d("Details", "${intent.getStringExtra("FileStatus")}")

        fileName.text = intent.getStringExtra("FileTitle") ?: ""
        statusText.text = intent.getStringExtra("FileStatus") ?: ""


        Handler(Looper.getMainLooper()).postDelayed({
            motion_layout.setTransition(R.id.start, R.id.end)
            motion_layout.setTransitionDuration(2000)
            motion_layout.transitionToEnd()
        }, 1000)

        okBtn?.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        finish()
    }
}
