package com.example.tikkel.ui.home

import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.*
import android.util.Log
import android.view.*
import android.widget.Button
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.startActivity
import com.example.tikkel.MainActivity
import com.example.tikkel.R
import kotlin.concurrent.thread


class FloatingWindowApp : Service(){
    private lateinit var  floatView: ViewGroup
    private lateinit var  floatWindowLayoutParams: WindowManager.LayoutParams
    private var LAYOUT_TYPE: Int? = null
    private lateinit var windowMannager: WindowManager
    // private lateinit var btnMax: Button
    private var safeStop: Boolean = false   // 추후에 이 변수가 사라지고 디비로부터 직접 서비스 러닝 중인지 체크할 데이터 다룰 것이다.
    private var wantPlay: Boolean = true
    private lateinit var handler_main : Handler
    private val CHANEL_ID = "TKL327"
    private val NOTI_ID = 327



    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        handler_main = Handler()
        Log.d("#########################","Service onCreate")

        val metrics = applicationContext.resources.displayMetrics
        val width : Int by lazy {
            if(MainActivity().db_display_mode_read().equals("banner")){
                metrics.widthPixels
            }
            else{
                metrics.widthPixels/3
            }
        }
        val height : Int by lazy {
            if(MainActivity().db_display_mode_read().equals("banner")){
                250
            }
            else{
                (width*0.55f).toInt()
            }
        }



        windowMannager = getSystemService(WINDOW_SERVICE) as WindowManager

        val inflater = baseContext.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater

        floatView = inflater.inflate(R.layout.floating_layout,null) as ViewGroup
        // btnMax = floatView.findViewById(R.id.MaxButton)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            LAYOUT_TYPE = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        }
        else LAYOUT_TYPE = WindowManager.LayoutParams.TYPE_TOAST

        floatWindowLayoutParams = WindowManager.LayoutParams(
            (width * 0.9f).toInt(),
            (height).toInt(),
            LAYOUT_TYPE!!,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        floatWindowLayoutParams.gravity = Gravity.TOP
        floatWindowLayoutParams.x = 0
        floatWindowLayoutParams.y = 0

        windowMannager.addView(floatView,floatWindowLayoutParams)

        // 추후 테스트 광고 달때 아래 코드 자세히 튜닝하겠음.
        /*btnMax.setOnClickListener {
            stopSelf()
            windowMannager.removeView(floatView)
            val back = Intent(this@FloatingWindowApp, MainActivity::class.java)
            back.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

            startActivity(back)
        }
        */

        // 이벤트 처리 함수는 따로 지정해야 minimize 됐을 때 조작가능
        floatView.setOnTouchListener(object: View.OnTouchListener{
            val updatedFloatWindowLayoutParams = floatWindowLayoutParams
            var x = 0.0
            var y = 0.0
            var px = 0.0
            var py = 0.0

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                when(event!!.action) {
                    MotionEvent.ACTION_DOWN -> {
                        x = updatedFloatWindowLayoutParams.x.toDouble()
                        y = updatedFloatWindowLayoutParams.y.toDouble()

                        px = event.rawX.toDouble()
                        py = event.rawY.toDouble()
                    }
                    MotionEvent.ACTION_MOVE -> {
                        updatedFloatWindowLayoutParams.x = (x + event.rawX - px).toInt()
                        updatedFloatWindowLayoutParams.y = (y + event.rawY - py).toInt()
                        windowMannager.updateViewLayout(floatView, updatedFloatWindowLayoutParams)
                    }
                    /*MotionEvent.ACTION_UP -> {
                        stopSelf()
                        windowMannager.removeView(floatView)
                        val back = Intent(this@FloatingWindowApp, MainActivity::class.java)
                        back.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

                        startActivity(back)
                        // 추후 흰화면이 되는 이벤트 때 성공을 알리는 암호화된 토큰 서버로 보내기
                    }*/
                }
                return false
            }
        })
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("#########################","Service onStartCommand")
        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        val notification = NotificationCompat.Builder(this, CHANEL_ID)
            .setContentTitle("TIKKEL")
            .setContentText("TIKKEL이 실행중입니다.")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(NOTI_ID,notification)

        runBackground()

        return START_STICKY
    }

    fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val serviceChannel = NotificationChannel(CHANEL_ID, "TKL_FOREGROUND",NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }
    // 광고 api 선정 및 광고 게제와 출금 알고리즘 상의 후 백그라운드 함수 Body 제작할 것임.
    fun runBackground(){

    }

    fun use_handler_main_message(msg: Message){
        handler_main.sendMessage(msg)
    }

    fun use_handler_main_runnable(){

    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d("#########################","Service onDestroy")
        stopSelf()
        windowMannager.removeView(floatView)
    }

    inner class BackgroundThread : Thread(){
        private lateinit var handler_back : Handler
        override fun run() {
            super.run()
            handler_back = Handler()
            for(i: Int in 1..100){
                sleep(2000)
            }
            /*while(wantPlay){
                if (!safeStop){

                }
                else{
                    //서비스 재부팅 코드 runnable 로 메인스레드에 전달
                }
            }*/
            //안전하게 스레드 바로 종료시킴.

        }
        fun use_handler_back_message(msg:Message){
            handler_back.sendMessage(msg)
        }

        fun use_handler_back_runnable(){

        }
    }
}