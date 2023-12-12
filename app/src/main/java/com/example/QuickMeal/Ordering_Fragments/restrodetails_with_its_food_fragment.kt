package com.example.QuickMeal.Ordering_Fragments

import android.content.Intent
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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.QuickMeal.Adapters.BestProductAdapter
import com.example.QuickMeal.DataModels.Firebase.Dish
import com.example.QuickMeal.R
import com.example.QuickMeal.Util.Rsource
import com.example.QuickMeal.ViewModel.BestProductViewModel
import com.example.QuickMeal.databinding.RestrodetailWithItsFoodBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlin.random.Random
@AndroidEntryPoint
class restrodetails_with_its_food_fragment:Fragment(R.layout.restrodetail_with_its_food) {
    private lateinit var binding:RestrodetailWithItsFoodBinding
    private val navArgs by navArgs<restrodetails_with_its_food_fragmentArgs>()
    private val bestProductAdapter by lazy { BestProductAdapter() }
    private val viewModel by viewModels<BestProductViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=RestrodetailWithItsFoodBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data=navArgs.restro
        binding.close.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.textView17.text=data.name
        binding.minutesAway.text="${Random.nextInt(1, 60)} minutes away"
        Log.d("restro", data.name)
        setupRv()

        bestProductAdapter.differ.submitList(data.dishes)
//
//        viewModel.getdata(data.name)

//        lifecycleScope.launchWhenStarted {
//            viewModel.productData.collectLatest {
//                when(it){
//                    is Rsource.Error -> {
//                        binding.progressBar7.visibility=View.VISIBLE
//                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
//                    }
//                    is Rsource.Loading -> {
//                        binding.progressBar7.visibility=View.VISIBLE
//                    }
//                    is Rsource.Success -> {
//                        binding.progressBar7.visibility=View.VISIBLE
//                        bestProductAdapter.differ.submitList(it.data)
//                    }
//                    else -> Unit
//                }
//            }
//        }

        bestProductAdapter.onClick={
            navigate(it,navArgs.location)
        }

    }

    private fun setupRv() {
       binding.Rv.apply {
           adapter=bestProductAdapter
           layoutManager=GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false)
       }
    }

    private fun navigate(product: Dish, location: String) {

        val intent= Intent(requireContext(), Dish_Details::class.java).apply {
            putExtra("id",product.id)
            putExtra("location",location)
            putExtra("Name",product.name)
            putExtra("img",product.img)

            putExtra("prices",product.price.toString())
            putExtra("category",product.category)
            putExtra("restaurant_id",product.restaurant_id.toString())

        }
        startActivity(intent)

    }
}