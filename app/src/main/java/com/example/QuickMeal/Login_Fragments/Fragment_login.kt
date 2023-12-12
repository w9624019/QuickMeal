package com.example.QuickMeal.Login_Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.QuickMeal.AfterLogin
import com.example.QuickMeal.R
import com.example.QuickMeal.Util.Rsource
import com.example.QuickMeal.ViewModel.LoginViewModel
import com.example.QuickMeal.databinding.FragmentLoginBinding
import com.example.QuickMeal.dialog.setupBottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest



@AndroidEntryPoint
class Fragment_login : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.register.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_login_to_fragment_register)
        }



            binding.apply {
                login.setOnClickListener {


                    if (emailforlogin.text.isEmpty() || passwordforlogin.text.isEmpty()) {
                        Toast.makeText(
                            requireContext(),
                            "Please Fill email and Password",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setOnClickListener
                    }

                    viewModel.loginUser(emailforlogin.text.toString(),passwordforlogin.text.toString())
                }


            }



        binding.ForgotPass.setOnClickListener {
            setupBottomSheetDialog { email->
                viewModel.resetPassword(email)
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.resetpass.collect {
                when (it) {
                    is Rsource.Loading -> {

                    }
                    is Rsource.Success -> {
                        Snackbar.make(requireView(),"Reset Link sent to the given mail", Snackbar.LENGTH_LONG).show()
                    }
                    is Rsource.Error -> {
                        Snackbar.make(requireView(),"Error: ${it.message.toString()}", Snackbar.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }



        lifecycleScope.launchWhenStarted {
            viewModel.login.collectLatest {
                when(it){
                    is Rsource.Error -> {
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                        binding.login.revertAnimation()
                    }
                    is Rsource.Loading -> {
                        binding.login.startAnimation()
                    }
                    is Rsource.Success -> {
                        binding.login.revertAnimation()
                       val intent=Intent(requireActivity(),AfterLogin::class.java)
                        startActivity(intent)
                    }
                   else -> Unit
                }
            }
        }

    }


}