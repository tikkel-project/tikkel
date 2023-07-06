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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.tikkel.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var dialog:AlertDialog
    private lateinit var btnMin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        btnMin = binding.MinButton
        if(isServiceRunning()){
            requireActivity().stopService(Intent(requireActivity(), FloatingWindowApp::class.java))
            requireActivity().finish()
        }

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        btnMin.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(context)) {
                    requestPermission()
                }else{
                    if(CheckOverlayPermission()){
                        requireActivity().startService(Intent(requireActivity(), FloatingWindowApp::class.java))
                        requireActivity().finish()
                    }
                }
            }

        }

        /*
        requestPermission()
        checkAndroidVersion()
         */
        return root
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
        _binding = null
    }
}