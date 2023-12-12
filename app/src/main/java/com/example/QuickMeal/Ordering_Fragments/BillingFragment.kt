package com.example.QuickMeal.Ordering_Fragments

import android.app.AlertDialog
import android.app.NotificationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.QuickMeal.Adapters.BillingRecyclerView
import com.example.QuickMeal.Adapters.addressAdapter
import com.example.QuickMeal.DataModels.Address
import com.example.QuickMeal.DataModels.CartProduct
import com.example.QuickMeal.R
import com.example.QuickMeal.Util.Rsource
import com.example.QuickMeal.ViewModel.BillingViewModel
import com.example.QuickMeal.ViewModel.OrderViewModel
import com.example.QuickMeal.databinding.FragmentBillingBinding
import com.example.agrishop.Data.Order.Order
import com.example.agrishop.Data.Order.OrderStatus
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
const val CHANNEL_ID="channel_id"
@AndroidEntryPoint
class BillingFragment:Fragment(R.layout.fragment_billing) {
    private lateinit var binding:FragmentBillingBinding
    private val addressAdapter by lazy { addressAdapter() }
    private val billingAdapter by lazy { BillingRecyclerView() }
    private val viewModel by viewModels<BillingViewModel>()
    private val args by navArgs<BillingFragmentArgs>()
    private var products= emptyList<CartProduct>()
    private var totalprice=0f
    private val paymentMethod= arrayListOf<String>("Paypal","RazorPay","Cash on Delivery")
    private var selectedAddress: Address?=null
    private val orderViewModel by viewModels<OrderViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentBillingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var builder =NotificationCompat.Builder(requireContext(), CHANNEL_ID)
//        builder.setSmallIcon(R.drawable.playstore)
//            .setContentTitle("Order Placed Successfully")
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//        with(NotificationManagerCompat.from(requireContext()){
//                noti
//        }

        binding.imageAddAddress.setOnClickListener {
            findNavController().navigate(R.id.action_billingFragment_to_addAddressFragment)
        }

        setupBillingProductRv()
        setupAddressRv()
        val arrayAdapter=ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_dropdown_item,paymentMethod)
        binding.spinner2.adapter=arrayAdapter

        binding.spinner2.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Toast.makeText(requireContext(), "Payment will be done via ${paymentMethod[position]}", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                return
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.address.collectLatest {
                when(it){
                    is Rsource.Error ->{
                        binding.progressbarAddress.visibility=View.GONE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    is Rsource.Loading -> {
                        binding.progressbarAddress.visibility=View.VISIBLE
                    }
                    is Rsource.Success -> {
                        addressAdapter.differ.submitList(it.data)
                        binding.progressbarAddress.visibility=View.GONE
                    }
                    else -> Unit
                }
            }
        }


        lifecycleScope.launchWhenStarted {
            orderViewModel.order.collectLatest {
                when(it){
                    is Rsource.Error ->{
                        binding.buttonPlaceOrder.startAnimation()
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    is Rsource.Loading -> {
                        binding.progressbarAddress.visibility=View.VISIBLE
                    }
                    is Rsource.Success -> {
                        binding.buttonPlaceOrder.revertAnimation()
                        findNavController().navigateUp()
                        Snackbar.make(requireView(),"Your Order was Placed", Snackbar.LENGTH_LONG).show()

                    }
                    else -> Unit
                }
            }
        }


        products=args.CartProduct.toList()
        totalprice=args.TotalPrice
        billingAdapter.differ.submitList(products)
        binding.tvTotalPrice.text=totalprice.toString()
        addressAdapter.OnClick={
            selectedAddress=it
        }

        binding.buttonPlaceOrder.setOnClickListener {
            if(selectedAddress==null) {

                Toast.makeText(requireContext(), "Please Select an address", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            showorderConfirmationDialog()
        }



    }

    private fun showorderConfirmationDialog() {
        val alert= AlertDialog.Builder(requireContext()).apply {
            setTitle("Order Items")
            setMessage(("Do you want to order these item from cart"))
            setNegativeButton("Cancel"){dialog,_->
                dialog.dismiss()

            }
            setPositiveButton("Yes"){ dialog,_->
                val order= Order(
                    orderStatus = OrderStatus.Ordered.status,
                    totalPrice = totalprice,
                    products,
                    selectedAddress!!
                )
                orderViewModel.placeOrder(order)
                dialog.dismiss()
            }
        }
        alert.create()
        alert.show()
    }

    private fun setupAddressRv() {
        binding.rvAddress.apply {
            layoutManager= LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL,false)
            adapter=addressAdapter
        }
    }

    private fun setupBillingProductRv() {
        binding.rvProducts.apply {
            layoutManager= LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL,false)
            adapter=billingAdapter
        }
    }
}