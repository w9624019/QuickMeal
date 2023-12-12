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
import com.example.QuickMeal.DataModels.CartProduct
import com.example.QuickMeal.databinding.BillingProductsRvItemBinding

class BillingRecyclerView : RecyclerView.Adapter<BillingRecyclerView.BillingProductViewHolder>(){
    inner class BillingProductViewHolder (private val binding: BillingProductsRvItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(product: CartProduct){
binding.apply {
    Glide.with(itemView).load(product.product.img).into(imageCartProduct)
    tvProductCartName.text=product.product.name
    tvProductCartPrice.text=(product.quantity.toInt()*product.product.price.toLong()).toString()
    tvBillingProductQuantity.text=product.quantity.toString()
}


        }
    }

    private val diffCallback=object : DiffUtil.ItemCallback<CartProduct>(){
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.product.id==newItem.product.id
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem==newItem
        }
    }

    val differ= AsyncListDiffer(this,diffCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingProductViewHolder {
        return BillingProductViewHolder(
            BillingProductsRvItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: BillingProductViewHolder, position: Int) {
        val product=differ.currentList[position]
        holder.bind(product)



    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


//    private fun navigate(product: Product, view: View) {
//
//        val intent= Intent(view.context, Product_Details::class.java).apply {
//            putExtra("id",product.id)
//            putExtra("category",product.category)
//            putExtra("Name",product.product_name)
//            putExtra("img",product.img)
//            putExtra("Des",product.description)
//            putExtra("prices",product.prices.toString())
//            putExtra("what",product.what)
//
//        }
//        view.context.startActivity(intent)
//
//    }
}