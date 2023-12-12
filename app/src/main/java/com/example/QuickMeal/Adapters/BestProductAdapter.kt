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
import com.example.QuickMeal.DataModels.Firebase.AvailableLocation
import com.example.QuickMeal.DataModels.Firebase.Dish
import com.example.QuickMeal.Ordering_Fragments.Dish_Details
import com.example.QuickMeal.databinding.BestProductViewBinding


class BestProductAdapter: RecyclerView.Adapter<BestProductAdapter.BestProductViewHolder>(){
    inner class BestProductViewHolder (private val binding:BestProductViewBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(product: Dish){
            binding.apply {
                Glide.with(itemView).load(product.img).into(imgProduct)

                tvName.text=product.name


                tvPrice.text= "Price- "+ "$"+product.price.toString()
                Log.d("bestProductAdapter", "bind: " +product.price)
            }


        }
    }

    private val diffCallback=object : DiffUtil.ItemCallback<Dish>(){
        override fun areItemsTheSame(oldItem: Dish, newItem: Dish): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: Dish, newItem: Dish): Boolean {
            return oldItem==newItem
        }
    }

    val differ= AsyncListDiffer(this,diffCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestProductViewHolder {
        return BestProductViewHolder(
            BestProductViewBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: BestProductViewHolder, position: Int) {
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
    var onClick:((Dish)->Unit)?=null
}