package com.example.tikkel

import android.app.Application
import android.util.Log
import com.kakao.sdk.common.KakaoSdk



class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Kakao SDK 초기화
        KakaoSdk.init(this, "")
        //${BuildConfig.kakao_api_key}
    }
}