package com.example.tikkel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.postDelayed
import com.example.tikkel.signup.SignUpActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause.*
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User

class SplashActivity : AppCompatActivity() {
    private val splashDuration = 2000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // blink 애니메이션
        val anim = AnimationUtils.loadAnimation(this, R.anim.blink_animation)
        // 애니메이션 재생
        val animTong = AnimationUtils.loadAnimation(this, R.anim.anim_splash_imageview)

        val image: TextView = findViewById(R.id.splash_text)
        val image2: ImageView = findViewById(R.id.splash_img)
        image.startAnimation(anim)
        image2.startAnimation(animTong)

        // Handler()를 통해서 UI 쓰레드를 컨트롤 한다.
        // Handler().postDelayed(딜레이 시간){딜레이 이후 동작}
        //      postDelayed()를 통해 일정 시간(딜레이 시간)동안 쓰레드 작업을 멈춘다.
        //      {딜레이 이후 동작}을 통해 딜레이 시간 이후, 동작을 정의해준다.


        Handler().postDelayed(splashDuration) {


            UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                if (error != null) {
                    val intent = Intent(this, SignUpActivity::class.java)
                    startActivity(intent)
                    finish()
                } else if (tokenInfo != null) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }


        }
    }






}


