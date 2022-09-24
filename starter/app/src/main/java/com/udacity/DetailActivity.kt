package com.udacity

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.udacity.notification.StatusViewModel
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*


class DetailActivity : AppCompatActivity() {

    // val viewModel: StatusViewModel by activityViewModels()
    private val viewModel: StatusViewModel by viewModels()

    //    private val viewModel: StatusViewModel by lazy {
//        val activity = requireNotNull(this) {
//            "You can only access the viewModel after onViewCreated()"
//        }
//        //The ViewModelProviders (plural) is deprecated.
//       ViewModelProvider.of(this, DevByteViewModel.Factory(activity.application)).get(DevByteViewModel::class.java)
//
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val actionBar: ActionBar? = supportActionBar

        // showing the back button in action bar

        // showing the back button in action bar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        Log.d("Details", "${intent.getStringExtra("FileStatus")}")

        fileName.text = intent.getStringExtra("FileTitle")?:""
        statusText.text = intent.getStringExtra("FileStatus")?:""


        viewModel.state.observe(this, Observer {
            // blank observe here
            Log.d("Details", "observe${it.name}")

        })
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
