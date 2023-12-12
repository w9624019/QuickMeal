package com.example.QuickMeal.Ordering_Fragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.QuickMeal.Adapters.BestProductAdapter
import com.example.QuickMeal.DataModels.CartProduct
import com.example.QuickMeal.DataModels.Firebase.Dish
import com.example.QuickMeal.R
import com.example.QuickMeal.Util.Rsource
import com.example.QuickMeal.ViewModel.CartViewModel
import com.example.QuickMeal.ViewModel.RestaurantDataViewModel
import com.example.QuickMeal.ViewModel.addToCartViewModel
import com.example.QuickMeal.databinding.FragmentDishDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


private val prodct_id = "id"

private val restaurant_id="restaurant_id"
private val product_img="img"
private val product_name="Name"
private val product_price="prices"
private val TAG = "Product_details"
private val product_category="category"
private val product_location="location"
@AndroidEntryPoint
class Dish_Details : AppCompatActivity() {
    private lateinit var binding:FragmentDishDetailsBinding
        private val bestProductAdapter by lazy {BestProductAdapter() }
    private val viewModel by viewModels<RestaurantDataViewModel>()
    private val viewModel2 by viewModels<addToCartViewModel>()
    private val viewModelfroDelete by viewModels<CartViewModel>()
    private lateinit var id: String
    private lateinit var restrauntID: String
    private var extras: Bundle? = null
    private lateinit var name:String
    private lateinit var location:String
    private var i=1
    private lateinit var img:String
    private lateinit var prices:String
    private lateinit var kate:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= FragmentDishDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        extras = intent.extras

        if (extras != null) {
            id = (extras?.getInt(prodct_id, 0) ?: "").toString()
            name=extras?.getString(product_name, "") ?: ""
            img=extras?.getString(product_img, "") ?: ""
            prices=extras?.getString(product_price, "") ?: ""
            kate=extras?.getString(product_category, "") ?: ""
            restrauntID=extras?.getString(restaurant_id, "") ?: ""
            location=extras?.getString(product_location, "") ?: ""
            Log.d(TAG, "$location sdc $restrauntID")
        } else {
            finish()
        }


        binding.apply {
            DishName.text=name
            DishPrice.text="Price- " +"$"+prices
            Glide.with(this@Dish_Details).load(img).into(imageView2)
        }
        setUpRestroRV()

        viewModel.getRestoFoods(restrauntID,location)

        lifecycleScope.launchWhenStarted {
            viewModel.restroData.collectLatest {
                when(it){
                    is Rsource.Error -> {
                        binding.progressBar2.visibility= View.GONE
                        Toast.makeText(this@Dish_Details, it.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                    is Rsource.Loading -> {
                        binding.progressBar2.visibility= View.VISIBLE
                    }
                    is Rsource.Success -> {
                        binding.progressBar2.visibility= View.GONE
                        bestProductAdapter.differ.submitList(it.data)
                    }
                   else -> Unit
                }
            }
        }



        binding.addtoCart.setOnClickListener {
            if(i % 2 != 0){
                viewModel2.addUpdateProductInCart(CartProduct(1, Dish(id.toInt(),name,kate,prices.toLong(),available = true,img,restrauntID.toLong())))

            } else {
                viewModelfroDelete.deleteCart(CartProduct(1, Dish(id.toInt(),name,kate,prices.toLong(),available = true,img,restrauntID.toLong())))
            }

        }


        lifecycleScope.launchWhenStarted {
            viewModel2.addToCart.collectLatest {
                when(it){
                    is Rsource.Loading->{
                        binding.addtoCart.startAnimation()
                        Log.d(TAG, "Loading")
                    }
                    is Rsource.Success->{
                        binding.addtoCart.revertAnimation()
                        binding.addtoCart.text="Remove from cart"
                        Toast.makeText(
                            this@Dish_Details,
                            "Product Added To cart",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d(TAG, "added")
                    }
                    is Rsource.Error->{
                        binding.addtoCart.stopAnimation()
                        Toast.makeText(
                            this@Dish_Details,
                            "Something went wrong",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else-> Unit
                }
            }
        }



    }

    private fun setUpRestroRV() {
        binding.moreItemRV.apply {
            adapter=bestProductAdapter
            layoutManager=GridLayoutManager(this@Dish_Details,2,GridLayoutManager.VERTICAL,false)
        }
    }
}