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
import com.example.QuickMeal.DataModels.Address
import com.example.QuickMeal.R
import com.example.QuickMeal.Util.Rsource
import com.example.QuickMeal.ViewModel.AddressViewModel
import com.example.QuickMeal.databinding.AddAddresBottomsheetBinding
import com.example.QuickMeal.databinding.AddAddressFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
@AndroidEntryPoint
class AddAddressFragment: Fragment(R.layout.add_address_fragment) {
    private lateinit var binding:AddAddresBottomsheetBinding
    val viewModel by viewModels<AddressViewModel> ()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=AddAddresBottomsheetBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        lifecycleScope.launchWhenStarted {
            viewModel.addNewAddress.collectLatest {
                when(it){
                    is Rsource.Loading->{
                        binding.progressbarAddress.visibility=View.VISIBLE

                    }
                    is Rsource.Error -> {
                        binding.progressbarAddress.visibility=View.INVISIBLE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    is Rsource.Success -> {
                        binding.progressbarAddress.visibility=View.INVISIBLE
                        findNavController().navigateUp()
                        binding.apply {
                            edAddressTitle.text.clear()
                            edCity.text.clear()
                            edPhone.text.clear()
                            edState.text.clear()
                            edStreet.text.clear()
                            edFullName.text.clear()

                        }
                        Toast.makeText(requireContext(), "Data successFully added", Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.error.collectLatest {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }


        binding.apply {
            buttonSave.setOnClickListener{
                val addressTitile=edAddressTitle.text.toString()
                val name=edFullName.text.toString()
                val street=edStreet.text.toString()
                val phone=edPhone.text.toString()
                val city=edCity.text.toString()
                val state=edState.text.toString()
                val address= Address(addressTitile,city,name,state,street,phone)
                viewModel.addAddress(address)
            }
        }

    }
}