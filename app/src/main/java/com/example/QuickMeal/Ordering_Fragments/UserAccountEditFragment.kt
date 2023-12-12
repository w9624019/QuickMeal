package com.example.QuickMeal.Ordering_Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.QuickMeal.DataModels.Users
import com.example.QuickMeal.R
import com.example.QuickMeal.Util.Rsource
import com.example.QuickMeal.ViewModel.LoginViewModel
import com.example.QuickMeal.ViewModel.UserAccountViewModel
import com.example.QuickMeal.databinding.FragmentUserAccountBinding
import com.example.QuickMeal.dialog.setupBottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
@AndroidEntryPoint
class UserAccountEditFragment:Fragment(R.layout.fragment_user_account) {
    private lateinit var bining:FragmentUserAccountBinding
    private val viewModel by viewModels<UserAccountViewModel>()
    private val viewModel2: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bining=FragmentUserAccountBinding.inflate(inflater)
        return bining.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
bining.imageCloseUserAccount.setOnClickListener {
    findNavController().navigateUp()
}


        lifecycleScope.launchWhenStarted {
            viewModel.user.collectLatest {
                when(it){
                    is Rsource.Loading->{
                        showUserLoading()
                    }
                    is Rsource.Error -> {
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                    is Rsource.Success -> {

                        hideUserLoading()
                        showuserInfo(it.data!!)
                    }
                    else ->Unit
                }
            }
        }




        lifecycleScope.launchWhenStarted {
            viewModel.editinfo.collectLatest {
                when(it){
                    is Rsource.Loading->{
                        bining.buttonSave.startAnimation()
                    }
                    is Rsource.Error -> {
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                    is Rsource.Success -> {
                        Toast.makeText(
                            requireContext(),
                            "Data successFully changed",
                            Toast.LENGTH_SHORT
                        ).show()
                        bining.buttonSave.revertAnimation()
                        findNavController().navigateUp()
                    }
                    else ->Unit
                }
            }
        }

        bining.tvUpdatePassword.setOnClickListener {
            setupBottomSheetDialog {
                viewModel2.resetPassword(bining.edEmail.text.trim().toString())
            }
        }


        bining.imageCloseUserAccount.setOnClickListener {
            findNavController().navigateUp()
        }


        lifecycleScope.launchWhenStarted {
            viewModel2.resetpass.collect {
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


        bining.buttonSave.setOnClickListener {
            bining.apply {
                val first=edFirstName.text.toString().trim()
                val last=edLastName.text.toString().trim()
                val email=edEmail.text.toString().trim()
                val userData=Users(first,last,email,"")
                viewModel.updateUser(user = userData)
            }

        }
    }

    private fun showuserInfo(data: Users) {
        bining.apply {
//        Glide.with(this@UserAccountFragment).load(data.pfp).into(imageUser)
            edFirstName.setText(data.firstName)
            edLastName.setText(data.lastName)
            edEmail.setText(data.email)
        }
    }

    private fun hideUserLoading() {
        bining.apply {
            progressbarAccount.visibility=View.GONE
//            imageUser.visibility=View.VISIBLE
//            imageEdit.visibility=View.VISIBLE
            edEmail.visibility=View.VISIBLE
            edFirstName.visibility=View.VISIBLE
            edLastName.visibility=View.VISIBLE
            tvUpdatePassword.visibility=View.VISIBLE
            buttonSave.visibility=View.VISIBLE

        }
    }

    private fun showUserLoading() {
        bining.apply {
            progressbarAccount.visibility=View.VISIBLE
//            imageUser.visibility=View.GONE
//            imageEdit.visibility=View.GONE
            edEmail.visibility=View.GONE
            edFirstName.visibility=View.GONE
            edLastName.visibility=View.GONE
            tvUpdatePassword.visibility=View.GONE
            buttonSave.visibility=View.GONE

        }
    }
}