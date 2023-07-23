package com.example.tikkel.ui.display

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ImageButton
import android.widget.Switch
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.tikkel.MainActivity
import com.example.tikkel.databinding.FragmentDisplayBinding
import com.example.tikkel.ui.home.FloatingWindowApp
import com.example.tikkel.ui.home.HomeFragment

class DisplayFragment : Fragment() {

    private var _binding: FragmentDisplayBinding? = null
    private val binding get() = _binding!!
    private lateinit var switchBanner: Switch
    private lateinit var switchWindow: Switch

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val displayViewModel =
            ViewModelProvider(this).get(DisplayViewModel::class.java)

        _binding = FragmentDisplayBinding.inflate(inflater, container, false)
        val root: View = binding.root

        switchBanner = binding.switchBanner
        switchWindow = binding.switchWindow

        if(MainActivity().db_display_mode_read().equals("banner")){
            switchBanner.isChecked = true
            switchWindow.isChecked = false
        }
        else{
            switchBanner.isChecked = false
            switchWindow.isChecked = true
        }

        switchBanner.setOnCheckedChangeListener{CompoundButton, onSwitch ->
            if(onSwitch){
                switchWindow.isChecked = false
                MainActivity().db_display_mode_update("banner")
                if(isServiceRunning()){
                    requireActivity().stopService(Intent(requireActivity(), FloatingWindowApp::class.java))
                    requireActivity().startForegroundService(Intent(requireActivity(), FloatingWindowApp::class.java))
                }


            }
            else{
                switchWindow.isChecked = true
                MainActivity().db_display_mode_update("window")
                Log.d("#########################","순재")
                /*if(isServiceRunning()){
                    requireActivity().stopService(Intent(requireActivity(), FloatingWindowApp::class.java))
                    requireActivity().startForegroundService(Intent(requireActivity(), FloatingWindowApp::class.java))
                }*/
            }
        }


        switchWindow.setOnCheckedChangeListener{CompoundButton, onSwitch ->
            if(onSwitch){
                switchBanner.isChecked = false
                MainActivity().db_display_mode_update("window")
                Log.d("#########################","야동")
                //lateinit var e: java.lang.Exception
                if(isServiceRunning()){
                    requireActivity().stopService(Intent(requireActivity(), FloatingWindowApp::class.java))
                    requireActivity().startForegroundService(Intent(requireActivity(), FloatingWindowApp::class.java))
                }
            }
            else{
                switchBanner.isChecked = true
                MainActivity().db_display_mode_update("banner")
                /*if(isServiceRunning()){
                    requireActivity().stopService(Intent(requireActivity(), FloatingWindowApp::class.java))
                    requireActivity().startForegroundService(Intent(requireActivity(), FloatingWindowApp::class.java))
                }*/
            }
        }


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



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}