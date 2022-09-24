package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.udacity.LoadingButton.Companion.buttonState
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


//enum class Status{
//    SUCCESS , FAILED
//}
class MainActivity : AppCompatActivity() {
    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager

    // private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private val CHANNEL_ID = "NOTIFICATION_ID"
    private val CHANNEL_NAME = "NOTIFICATION_CHANNEL_NAME"

    // private val notifyIntent = Intent(this, NotificationReceiver::class.java)
    private val REQUEST_CODE = 0
    lateinit var downloadManager: DownloadManager
   // var loadingButton = LoadingButton()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

//        pendingIntent = PendingIntent.getBroadcast(
//            getApplication(),
//            REQUEST_CODE,
//            notifyIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT
//        )

        custom_button.setOnClickListener {
            buttonState = ButtonState.Clicked
            if (radioGroup.checkedRadioButtonId === -1) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_select_text),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Log.d("TAGMAIn", "url $URL")
                download()
            }
        }
        createChannel(CHANNEL_ID, CHANNEL_NAME)
    }


    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            //val status = "DOWNLOADED"
            if (downloadID == id) {
                buttonState = ButtonState.Completed
                Toast.makeText(applicationContext, "Download Completed", Toast.LENGTH_SHORT).show()
                val query = DownloadManager.Query()
                query.setFilterById(downloadID)
                val cursor: Cursor = downloadManager.query(query)

                if (cursor.moveToFirst()) {
                    val columnIndex: Int = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                    val status: Int = cursor.getInt(columnIndex)
                    Log.d("Status", "$status")
                    // val columnReason: Int = cursor.getColumnIndex(DownloadManager.COLUMN_REASON)
                    //val reason: Int = cursor.getInt(columnReason)
                    when (status) {
                        DownloadManager.STATUS_SUCCESSFUL -> {
                            Log.d("Status2", "STATUS_SUCCESSFUL")
                            STATUS = "SUCCESS"
                            title = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE))
                            Log.d("TITLE", "$title")

                        }
                        DownloadManager.STATUS_FAILED -> {
                            Log.d("Status2", "STATUS_FAILED")
                            STATUS = "FAILED"
                        }
//                    DownloadManager.STATUS_PAUSED -> {
//                        Log.d("Status2" , "STATUS_PAUSED")
//
//
//                    }
//                    DownloadManager.STATUS_PENDING -> {
//                        Log.d("Status2" , "STATUS_PENDING")
//
//                    }
//                    DownloadManager.STATUS_RUNNING -> {
//                        Log.d("Status2" , "STATUS_RUNNING")
//                    }
                    }
                }
                val notificationMessage = (getString(R.string.notification_description)).replace(
                    "{{FILE_NAME_REPO}}",
                    TITLE
                )
                notificationManager.sendNotification(
                    notificationMessage!!,
                    context!!,
                    STATUS!!,
                    TITLE
                )


            }


        }
    }

    private fun download() {
        buttonState = ButtonState.Loading
        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(TITLE)
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.firstRadioGlide ->
                    if (checked) {
                        URL = "https://github.com/bumptech/glide"
                        TITLE = "bumptech/glide"
                    }
                R.id.secondRadioApp ->
                    if (checked) {
                        URL =
                            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter"
                        TITLE = "udacity/nd940-c3-advanced-android-programming-project-starter"
                    }
                R.id.thirdRadioRetrofit ->
                    if (checked) {
                        URL = "https://github.com/square/retrofit"
                        TITLE = "square/retrofit"
                    }
            }
        }
    }

    private fun createChannel(channelId: String, channelName: String) {
        Log.d("NOTIFICATION", "CreateChannel Start")
        notificationManager =
            ContextCompat.getSystemService(
                applicationContext,
                NotificationManager::class.java
            ) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
                .apply {
                    setShowBadge(false)
                }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.notification_description)

            val notificationManager = this.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
            Log.d("NOTIFICATION", "CreateChannel End")
        }
    }


    companion object {
        var STATUS: String? = ""
        private var TITLE = ""
        private var URL = ""
    }

}
