package com.example.broadcasttest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.OutcomeReceiver
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.broadcasttest.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    lateinit var timeChangeReceiver: TimeChangeReceiver
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //当系统时间发生改变时，就会发送一条值为“android.intent.action.TIME_TICK”的标准广播
        val intentFilter = IntentFilter()
        intentFilter.addAction("android.intent.action.TIME_TICK")
        timeChangeReceiver = TimeChangeReceiver()
        registerReceiver(timeChangeReceiver,intentFilter)

        //设置按钮发送广播
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener({
            val intent = Intent("com.example.broadcasttest.MY_BROADCAST")
            //android8.0开始，静态注册的BroadcastReceiver无法接收隐式广播
            //所以需要setPackage将该广播设置为显示广播
            intent.setPackage(packageName)

            //发送标准广播
//            sendBroadcast(intent)

            //发送有序广播
            sendOrderedBroadcast(intent, null)
        })

        binding.forceOffLine.setOnClickListener({
            val intent = Intent("com.example.broadcasttest.FORCE_OFFLINE")
            sendBroadcast(intent)
            Log.d("forceOffline button", "Broadcast has been sent")
        })
    }

    override fun onDestroy(){
        //动态注册的BroadcastReceiver必须要取消注册
        super.onDestroy()
        unregisterReceiver(timeChangeReceiver)
    }

    inner class TimeChangeReceiver : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            Toast.makeText(context, "Time has changed", Toast.LENGTH_SHORT).show()
        }
    }
}