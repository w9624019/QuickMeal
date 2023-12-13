package com.example.QuickMeal.Ordering_Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.QuickMeal.Adapters.ChangeLocationAdapter
import com.example.QuickMeal.DataModels.Firebase.AvailableLocation
import com.example.QuickMeal.R
import com.example.QuickMeal.Util.Rsource
import com.example.QuickMeal.ViewModel.GetLocationViewModel
import com.example.QuickMeal.databinding.ChangeAddressBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
@AndroidEntryPoint
class ChangeLocationFragment:Fragment(R.layout.change_address) {

    private lateinit var binding:ChangeAddressBinding
    private val changeLocationAdapter by lazy{ChangeLocationAdapter()}
    private val viewModel by viewModels<GetLocationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=ChangeAddressBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRV()
        binding.close.setOnClickListener {
            findNavController().navigateUp()
        }

        changeLocationAdapter.onClick={
            val b=Bundle()
            b.putString("Location", it.name)
            findNavController().navigate(R.id.action_changeLocationFragment_to_homeFragment,b)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.location.collectLatest {
                when(it){
                    is Rsource.Error -> {
                        binding.progressBar3.visibility=View.GONE
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                    is Rsource.Loading -> {
                        binding.progressBar3.visibility=View.VISIBLE
                    }
                    is Rsource.Success -> {
                        binding.progressBar3.visibility=View.GONE
                        for (data in it.data!!){
                            Log.d("LocationData", data.name)


                        }
                        changeLocationAdapter.differ.submitList(it.data)
                        Log.d("LocationFrag", it.data!!.size.toString())
                    }
                    else -> Unit

                }
            }
        }

    }

    private fun setUpRV() {
        binding.LocationRV.apply {
            adapter=changeLocationAdapter
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            itemDecorationCount
        }
    }
}