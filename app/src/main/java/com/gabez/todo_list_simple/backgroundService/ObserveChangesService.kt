package com.gabez.todo_list_simple.backgroundService

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.gabez.todo_list_simple.R
import com.gabez.todo_list_simple.app.MainActivity
import com.gabez.todo_list_simple.data.dataSources.RemoteDataChangeProvider
import com.gabez.todo_list_simple.data.firestore.RemoteDatabaseItemsProvider
import com.gabez.todo_list_simple.data.firestore.responses.ApiResponseStatus
import com.gabez.todo_list_simple.data.pagination.PagingLastIndexProvider
import com.gabez.todo_list_simple.domain.entities.ItemTODO
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import org.jetbrains.anko.runOnUiThread
import org.koin.core.KoinComponent
import org.koin.core.inject

class ObserveChangesService : Service(), KoinComponent {
    private val CHANNEL_ID = "com.gabez.todo_list_simple.backgroundService.CHANNEL_ID"

    val lastIndexProvider: PagingLastIndexProvider by inject()
    val remoteDataProvider: RemoteDatabaseItemsProvider by inject()
    val dataChanges: RemoteDataChangeProvider by inject()

    var lastIndex: LiveData<Int> = lastIndexProvider.lastIndex
    private lateinit var repeatFunJob: Job

    companion object {
        fun startService(context: Context) {
            val startIntent = Intent(context, ObserveChangesService::class.java)
            ContextCompat.startForegroundService(context, startIntent)
        }

        fun stopService(context: Context) {
            val stopIntent = Intent(context, ObserveChangesService::class.java)
            context.stopService(stopIntent)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        GlobalScope.launch(Dispatchers.IO) {
            repeatFunJob = repeatFun()
            repeatFunJob.start()
        }

        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("TODO list app")
            .setContentText("Listening for changes...")
            .setSmallIcon(R.drawable.app_icon_plain)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        GlobalScope.launch { repeatFunJob.cancel() }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID, "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }

    private suspend fun loadRemoteData() {
        remoteDataProvider.getLast(lastIndex.value!!.toLong()).collect {
            response ->

            when(response.status){
                ApiResponseStatus.SUCCESS -> {
                    Log.v("DB", "load remote data - success")
                    dataChanges.saveRemoteDataAndCompare(response.data as List<ItemTODO>)
                }
                ApiResponseStatus.FAILED -> Log.v("DB", "load remote data - failed")
            }
        }
    }

    private suspend fun repeatFun(): Job {
        while (true) {
            loadRemoteData()
            delay(1000 * 60)
        }
    }
}