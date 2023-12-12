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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.QuickMeal.Adapters.OrderAdapter
import com.example.QuickMeal.R
import com.example.QuickMeal.Util.Rsource
import com.example.QuickMeal.ViewModel.OrderRetrivalViewModel
import com.example.QuickMeal.databinding.OrderDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
@AndroidEntryPoint
class OrderFragment:Fragment(R.layout.order_details) {
    private lateinit var binding:OrderDetailsBinding
    private val viewModel by viewModels<OrderRetrivalViewModel>()
    private val orderAdapter by lazy { OrderAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=OrderDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
binding.close.setOnClickListener {
    findNavController().navigateUp()
}
        binding.orderRV.apply {
            adapter=orderAdapter
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            itemDecorationCount
        }

        lifecycleScope.launchWhenStarted {
            viewModel.allorder.collectLatest {
                when(it){
                    is Rsource.Error ->  {
                        binding.progressBar6.visibility=View.GONE
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                    is Rsource.Loading -> {
                        binding.progressBar6.visibility=View.VISIBLE
                    }
                    is Rsource.Success ->{
                        binding.progressBar6.visibility=View.GONE
                        orderAdapter.differ.submitList(it.data)
                    }
                    else -> Unit
                }
            }
        }


    }
}