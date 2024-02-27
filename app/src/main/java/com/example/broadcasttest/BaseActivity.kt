package com.example.broadcasttest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity: AppCompatActivity() {

    lateinit var receiver: ForceOfflineReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCollector.addActivity(this)
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.example.broadcasttest.FORCE_OFFLINE")
        receiver = ForceOfflineReceiver()
        registerReceiver(receiver, intentFilter)
    }

    override fun onPause(){
        super.onPause()
        unregisterReceiver(receiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }

    inner class ForceOfflineReceiver: BroadcastReceiver(){
        override fun onReceive(context: Context, intent: Intent){
            AlertDialog.Builder(context).apply{
                setTitle("Warning")
                setMessage("You are forced to be offline. Please try to login again.")

                //保证强制退出对话框不会被用户取消
                setCancelable(false)
                setPositiveButton("OK"){_,_->
                    ActivityCollector.finishAll()
                    val intent_Login = Intent(context, LoginActivity::class.java)
                    context.startActivity(intent_Login)
                }
                show()
            }
        }
    }
}