package com.gabez.todo_list_simple.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gabez.todo_list_simple.R
import com.gabez.todo_list_simple.backgroundService.ObserveChangesService
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import org.koin.core.KoinComponent

class MainActivity : AppCompatActivity(), KoinComponent {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ObserveChangesService.startService(this@MainActivity)
    }

    override fun onDestroy() {
        super.onDestroy()
        ObserveChangesService.stopService(this@MainActivity)
        finishAndRemoveTask()
    }
}