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
import com.example.QuickMeal.databinding.SearchRecyclerviewBinding


class SearchAdapter : RecyclerView.Adapter<SearchAdapter.SearchProductViewHolder>(){
    inner class SearchProductViewHolder (private val binding: SearchRecyclerviewBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(product: Dish){
            binding.apply {
                Glide.with(itemView).load(product.img).into(imageViewIcon)
                textViewTitle.text=product.name
                textViewDescription.text="Price - $"+product.price.toString()
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchProductViewHolder {
        return SearchProductViewHolder(
            SearchRecyclerviewBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: SearchProductViewHolder, position: Int) {
        val product=differ.currentList[position]
        holder.bind(product)

        holder.itemView.setOnClickListener {
            onClick?.invoke(product)
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onClick:((Dish)->Unit)?=null

}