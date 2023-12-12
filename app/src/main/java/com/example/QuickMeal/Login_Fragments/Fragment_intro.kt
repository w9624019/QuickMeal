package com.example.QuickMeal.Login_Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.QuickMeal.AfterLogin
import com.example.QuickMeal.R
import com.example.QuickMeal.ViewModel.IntroductionViewModel
import com.example.QuickMeal.ViewModel.IntroductionViewModel.Companion.ACCOUNT_OPTION
import com.example.QuickMeal.ViewModel.IntroductionViewModel.Companion.SHOPPING_ACTIVITY
import com.example.QuickMeal.databinding.FragmentIntroBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Fragment_intro : Fragment(R.layout.fragment_intro) {
    private lateinit var binding: FragmentIntroBinding
    private val viewModel by viewModels<IntroductionViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentIntroBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.Regsiter.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_intro_to_fragment_register)
        }

        binding.Login.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_intro_to_fragment_login)
        }


        lifecycleScope.launchWhenStarted {
            viewModel.navigate.collect{
                when(it){
                    SHOPPING_ACTIVITY->{
                        val i= Intent(requireContext(), AfterLogin::class.java)
                        startActivity(i)
                    }
                    ACCOUNT_OPTION->{
                        findNavController().navigate(R.id.action_fragment_intro_to_fragment_register)
                    }
                    else-> Unit
                }
            }
        }




    }


}