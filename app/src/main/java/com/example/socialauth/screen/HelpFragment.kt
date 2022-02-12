package com.example.socialauth.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.socialauth.R
import com.example.socialauth.databinding.FragmentHelpBinding

class HelpFragment : Fragment() {
    private lateinit var binding: FragmentHelpBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHelpBinding.inflate(inflater)
        
        return binding.root
    }
}