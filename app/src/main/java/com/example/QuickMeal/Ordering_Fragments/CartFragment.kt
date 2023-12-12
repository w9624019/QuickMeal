package com.example.QuickMeal.Ordering_Fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.QuickMeal.Adapters.CartProductAdapter
import com.example.QuickMeal.Firebase.FirebaseCommon
import com.example.QuickMeal.R
import com.example.QuickMeal.Util.Rsource
import com.example.QuickMeal.Util.VerticalItemDecoration
import com.example.QuickMeal.ViewModel.CartViewModel
import com.example.QuickMeal.databinding.CartFragmentBinding
import kotlinx.coroutines.flow.collectLatest

class CartFragment :Fragment(R.layout.cart_fragment){
    private lateinit var binding:CartFragmentBinding
    private val CartProductAdapter by lazy { CartProductAdapter() }
    private val viewModel by activityViewModels<CartViewModel> ()
    private var total:Float=0f
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=CartFragmentBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpCartRV()


        lifecycleScope.launchWhenStarted {
            viewModel.productPrice.collectLatest {
                it?.let { price->
                    total=price
                    binding.tvTotalPrice.text="$"+price.toString()

                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.deleteDialog.collectLatest {
                val alert= AlertDialog.Builder(requireContext()).apply {
                    setTitle("Delete item from cart")
                    setMessage(("Do you want to delete this item from cart"))
                    setNegativeButton("Cancel"){dialog,_->
                        dialog.dismiss()

                    }
                    setPositiveButton("Yes"){ dialog,_->

                        viewModel.deleteCart(it)
                        dialog.dismiss()
                    }
                }
                alert.create()
                alert.show()
            }
        }

        CartProductAdapter.onPlusClick={
            viewModel.changeQuantity(it, FirebaseCommon.QuantityChanging.INCREASE)
        }
        CartProductAdapter.onMinusClick={
            viewModel.changeQuantity(it,FirebaseCommon.QuantityChanging.DECREASE)
        }




        lifecycleScope.launchWhenStarted {
            viewModel.cartProducts.collectLatest {
                when(it){
                    is Rsource.Loading->{
                        binding.progressbarCart.visibility=View.VISIBLE
                    }
                    is Rsource.Error -> {
                        binding.progressbarCart.visibility=View.GONE
                        showEmptyCart()
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                    is Rsource.Success -> {
                        binding.progressbarCart.visibility=View.GONE
                        if(it.data!!.isEmpty()){
                            showEmptyCart()
                            hideOtherView()
                        } else {
                            hideEmptyCart()
                            showOtherView()
                            CartProductAdapter.differ.submitList(it.data)
                        }
                    }
                    else ->Unit
                }
            }
        }
        binding.buttonCheckout.setOnClickListener {
            val action=CartFragmentDirections.actionCartFragmentToBillingFragment(CartProductAdapter.differ.currentList.toTypedArray(),total)
            findNavController().navigate(action)
        }



    }

    private fun setUpCartRV() {
        binding.rvCart.apply {
            adapter=CartProductAdapter
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            addItemDecoration(VerticalItemDecoration())
         }
    }

    private fun showOtherView() {
        binding.apply {
            rvCart.visibility=View.VISIBLE
            totalBoxContainer.visibility=View.VISIBLE
            buttonCheckout.visibility=View.VISIBLE
        }
    }

    private fun hideOtherView() {
        binding.apply {
            rvCart.visibility=View.GONE
            totalBoxContainer.visibility=View.GONE
            buttonCheckout.visibility=View.GONE
        }
    }

    private fun hideEmptyCart() {
        binding.apply {
            layoutCarEmpty.visibility=View.GONE
        }
    }

    private fun showEmptyCart() {
        binding.apply {
            layoutCarEmpty.visibility=View.VISIBLE
        }
    }
}