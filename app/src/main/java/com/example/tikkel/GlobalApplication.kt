package com.example.tikkel

import android.app.Application
import android.util.Log
import com.example.tikkel.BuildConfig.kakao_api_key
import com.kakao.sdk.common.KakaoSdk



class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Kakao SDK 초기화
        KakaoSdk.init(this, "${kakao_api_key}")
        Log.d("key","kakao${kakao_api_key}")
    }
}