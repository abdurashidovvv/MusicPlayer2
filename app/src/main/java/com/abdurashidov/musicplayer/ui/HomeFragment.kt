package com.abdurashidov.musicplayer.ui

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.abdurashidov.musicplayer.R
import com.abdurashidov.musicplayer.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private var REQUEST_PERMISSSION: Int = 99

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentHomeBinding.inflate(layoutInflater)

        val handler=Handler(Looper.getMainLooper())
        val runnable= Runnable {

            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_PERMISSSION
                )

            }else{
                val handler=Handler(Looper.getMainLooper())
                val runnable= Runnable {
                    findNavController().navigate(R.id.recycleMusicFragment2)
                }
                handler.postDelayed(runnable, 5000)
            }

        }
        handler.postDelayed(runnable, 500)
        return binding.root
    }
}