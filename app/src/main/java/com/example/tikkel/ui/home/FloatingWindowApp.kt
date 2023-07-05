package com.example.tikkel.ui.home

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.*
import android.widget.Button
import androidx.core.content.ContextCompat.startActivity
import com.example.tikkel.MainActivity
import com.example.tikkel.R


class FloatingWindowApp : Service(){
    private lateinit var  floatView: ViewGroup
    private lateinit var  floatWindowLayoutParams: WindowManager.LayoutParams
    private var LAYOUT_TYPE: Int? = null
    private lateinit var windowMannager: WindowManager
    // private lateinit var btnMax: Button

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        val metrics = applicationContext.resources.displayMetrics
        val width = metrics.widthPixels
        val height = 250

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
                    MotionEvent.ACTION_UP -> {
                        stopSelf()
                        windowMannager.removeView(floatView)
                        val back = Intent(this@FloatingWindowApp, MainActivity::class.java)
                        back.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

                        startActivity(back)
                        // 추후 흰화면이 되는 이벤트 때 성공을 알리는 암호화된 토큰 서버로 보내기
                    }
                }
                return false
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSelf()
        windowMannager.removeView(floatView)
    }
}