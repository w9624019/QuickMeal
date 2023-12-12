package com.example.QuickMeal.Ordering_Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.QuickMeal.databinding.FragmentProfileBinding
import com.example.QuickMeal.R
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment:Fragment(R.layout.fragment_profile) {
    private lateinit var binding:FragmentProfileBinding
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.constraintProfile.setOnClickListener{
            findNavController().navigate(R.id.action_profileFragment_to_userAccountEditFragment)
        }
        binding.linearTrackOrder.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_addressShower)
        }

        binding.linearRegional.setOnClickListener {
            firebaseAuth.signOut()
        }

        binding.linearOrders.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_orderFragment)
        }
    }
}