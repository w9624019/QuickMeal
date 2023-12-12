package com.example.QuickMeal.Ordering_Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.QuickMeal.Adapters.addressShowerAdapter
import com.example.QuickMeal.R
import com.example.QuickMeal.Util.Rsource
import com.example.QuickMeal.ViewModel.BillingViewModel
import com.example.QuickMeal.databinding.AddressRvItemBinding
import com.example.QuickMeal.databinding.AddressShowerFragmentBinding
import com.example.QuickMeal.databinding.AddressShowerRvBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
@AndroidEntryPoint
class AddressShower:Fragment(R.layout.address_shower_fragment) {
    private lateinit var binding:AddressShowerFragmentBinding
    private val viewModel by viewModels<BillingViewModel>()
    private val addressAdapter by lazy { addressShowerAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= AddressShowerFragmentBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.close.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.recyclerView2.apply {
            adapter=addressAdapter
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        }
        binding.addAddress.setOnClickListener {
            findNavController().navigate(R.id.action_addressShower_to_addAddressFragment)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.address.collectLatest {
                when(it){
                    is Rsource.Error -> {
                        binding.progressBar4.visibility=View.GONE
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                    is Rsource.Loading -> {
                        binding.progressBar4.visibility=View.VISIBLE
                    }
                    is Rsource.Success -> {
                        binding.progressBar4.visibility=View.GONE
                        addressAdapter.differ.submitList(it.data)
                    }
                    else -> Unit
                }
            }
        }

    }
}