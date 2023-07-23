package com.example.tikkel

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tikkel.databinding.ActivityMainBinding
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import android.util.Base64
import com.example.tikkel.miniDB.DatabaseHelper
import com.example.tikkel.ui.home.FloatingWindowApp


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    //lateinit var dbHelper : DatabaseHelper
    private val dbHelper: DatabaseHelper by lazy{
        DatabaseHelper.getInstance(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //dbHelper = DatabaseHelper.getInstance(this)
        Log.d("#########################","MainActivity onCreate")

        binding = ActivityMainBinding.inflate(layoutInflater)
        //getHashKey()

        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_withdraw,
                R.id.navigation_display,
                R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        //for checking now's display mode with logging
        db_display_mode_read()

    }

    // DB 관련 함수는 보안을 위해 아래와 같이 앞에 DB_ 를 붙이고 이후로 테이블명, CRUD 기능을 붙여 함수명을 만들자.
    // dbHelper 객체가 타 클래스에 노출됨을 막고, 인스턴스가 중복 생성되는걸 막자.

    // display_mode 테이블을 위한 함수들
    fun db_display_mode_read(): String{
        return dbHelper.display_mode_read()
    }

    fun db_display_mode_update( mode: String){
        dbHelper.display_mode_update("1",mode)
    }


    override fun onStart() {
        super.onStart()
        Log.d("#########################","MainActivity onStart")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("#########################","MainActivity onRestart")
    }

    override fun onPause() {
        super.onPause()
        Log.d("#########################","MainActivity onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("#########################","MainActivity onStop")
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
        Log.d("#########################","MainActivity onDestroy")
    }

    private fun getHashKey() {
        var packageInfo: PackageInfo = PackageInfo()
        try {
            packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        for (signature: android.content.pm.Signature in packageInfo.signatures) {
            try {
                var md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.e("KEY_HASH", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            } catch (e: NoSuchAlgorithmException) {
                Log.e("KEY_HASH", "Unable to get MessageDigest. signature = " + signature, e)
            }
        }
    }



}