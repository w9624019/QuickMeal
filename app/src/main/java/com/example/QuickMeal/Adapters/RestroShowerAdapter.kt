package com.example.QuickMeal.Adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.QuickMeal.DataModels.Firebase.Dish
import com.example.QuickMeal.DataModels.Firebase.Restaurant
import com.example.QuickMeal.Ordering_Fragments.Dish_Details
import com.example.QuickMeal.databinding.BestProductViewBinding
import com.example.QuickMeal.databinding.RestaurantShowerBinding
import kotlin.random.Random

class RestroShowerAdapter: RecyclerView.Adapter<RestroShowerAdapter.RestroViewHolder>(){
    inner class RestroViewHolder (private val binding: RestaurantShowerBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(product: Restaurant){
            binding.apply {
                Glide.with(itemView).load(product.img).into(imageView8)
                Restroname.text=product.name.toString()
                Minutes.text="${Random.nextInt(1, 60)} minutes away"
            }


        }
    }

    private val diffCallback=object : DiffUtil.ItemCallback<Restaurant>(){
        override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
            return oldItem==newItem
        }
    }

    val differ= AsyncListDiffer(this,diffCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestroViewHolder {
        return RestroViewHolder(
            RestaurantShowerBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: RestroViewHolder, position: Int) {
        val product=differ.currentList[position]
        holder.bind(product)

        holder.itemView.setOnClickListener {
            onClick?.invoke(product)
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }




    private fun navigate(product: Dish, view: View) {

        val intent= Intent(view.context, Dish_Details::class.java).apply {
            putExtra("id",product.id)

            putExtra("Name",product.name)
            putExtra("img",product.img)

            putExtra("prices",product.price.toString())
            putExtra("category",product.category)
            putExtra("restaurant_id",product.restaurant_id.toString())

        }
        view.context.startActivity(intent)

    }
    var onClick:((Restaurant)->Unit)?=null
}