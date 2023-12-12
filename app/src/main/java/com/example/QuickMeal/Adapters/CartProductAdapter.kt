package com.example.QuickMeal.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.QuickMeal.DataModels.CartProduct
import com.example.QuickMeal.DataModels.Firebase.Dish
import com.example.QuickMeal.Ordering_Fragments.Dish_Details
import com.example.QuickMeal.R
import com.example.QuickMeal.databinding.CartProductBinding


class CartProductAdapter : RecyclerView.Adapter<CartProductAdapter.CartProductViewholder>() {

    inner class CartProductViewholder(val binding: CartProductBinding)
        : RecyclerView.ViewHolder(binding.root){
        fun bind(cartProduct: CartProduct){
            binding.apply {
                Glide.with(itemView).load(cartProduct.product.img).into(ImageCartProducts)
                ProductCartName.text=cartProduct.product.name.toString()
//                Log.d("recyclerView", "bind: " + product.product_name)
                ProductRestroNAme.text=cartProduct.product.category
                ProductCartPrice.text="Price- $"+cartProduct.product.price.toString()
                quantity.text=cartProduct.quantity.toString()

            }
        }
    }
    val diffCallback =object : DiffUtil.ItemCallback<CartProduct>(){
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.product.id==newItem.product.id
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem==newItem
        }
    }

    val differ = AsyncListDiffer(this,diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductViewholder {
        return CartProductViewholder(
            CartProductBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: CartProductViewholder, position: Int) {
        val product=differ.currentList[position]
        holder.bind(product)

        holder.itemView.findViewById<ImageView>(R.id.ImageCartProducts).setOnClickListener {

            navigate(product.product,it)

        }

        holder.binding.apply {
            plus.setOnClickListener {
                onPlusClick?.invoke(product)
            }
            minus.setOnClickListener {
                onMinusClick?.invoke(product)
            }
        }
    }

    override fun getItemCount(): Int {
        return  differ.currentList.size
    }

    var onClick:((Dish)->Unit)?=null


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
    var onPlusClick:((CartProduct)->Unit)?=null
    var onMinusClick:((CartProduct)->Unit)?=null
}