package com.example.tikkel.ui.home

import android.app.ActivityManager
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.tikkel.R
import com.example.tikkel.databinding.FragmentHomeBinding
import kotlin.concurrent.thread
import com.example.tikkel.MainActivity


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var dialog:AlertDialog
    private lateinit var btnMin: ImageButton

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("#########################","HomeFragment onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("#########################","HomeFragment onCreateView")
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        btnMin = binding.MinButton

        //val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
         //   textView.text = it
        }
        if(!isServiceRunning()){
            binding.textOnOff.text="ON"
        }
        else{
            binding.textOnOff.text="OFF"
        }

        btnMin.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(context)) {
                    requestPermission()
                }else{
                    if(CheckOverlayPermission()){
                        if(!isServiceRunning()){
                            requireActivity().startForegroundService(Intent(requireActivity(), FloatingWindowApp::class.java))
                            binding.textOnOff.text = "OFF"

                        }
                        else{
                            //서비스에 안전 종료 메시지 보내기

                            requireActivity().stopService(Intent(requireActivity(), FloatingWindowApp::class.java))
                            binding.textOnOff.text = "ON"
                        }
                    }
                }
            }

        }
        binding.nextScore.setOnClickListener {
            val dlg= CustomScore()
            dlg.show(requireActivity().supportFragmentManager,"CustomScore")
        }
        binding.textView.setOnClickListener {
            val dlg= CustomScore()
            dlg.show(requireActivity().supportFragmentManager,"CustomScore")
        }

        /*
        requestPermission()
        checkAndroidVersion()
         */
        return root
    }


    override fun onPause() {
        super.onPause()
        Log.d("#########################","HomeFragment onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("#########################","HomeFragment onStop")
    }

    private fun isServiceRunning(): Boolean {
        val manager = requireActivity().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for( service in manager.getRunningServices(Int.MAX_VALUE)){
            if(FloatingWindowApp::class.java.name == service.service.className){
                return true
            }
        }
        return false
    }

    private fun requestPermission() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setCancelable(true)
        builder.setTitle("Screen Overlay Permission needed")
        builder.setMessage("Enable 'Display over the App' from settings'")
        builder.setPositiveButton("Open Settings", DialogInterface.OnClickListener { dialog, which ->
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${requireActivity().packageName}")
            )
            startActivity(intent)
        })
        dialog = builder.create()
        dialog.show()
    }

    private fun CheckOverlayPermission(): Boolean{
        return if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            Settings.canDrawOverlays(requireActivity())
        }
        else return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("#########################","HomeFragment onDestroyView")
        _binding = null
    }
}