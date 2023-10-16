package com.project.soloshoppingcart.ui.adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.soloshoppingcart.databinding.ListItemIncartViewBinding
import com.project.soloshoppingcart.datamodel.Products
import com.project.soloshoppingcart.datamodel.ProductsItem

class CartRVAdapter : RecyclerView.Adapter<CartRVAdapter.CartViewHolder>() {

    var onItemClick: ((ProductsItem) -> Unit)? = null
    var products = mutableListOf<ProductsItem?>()

    @SuppressLint("NotifyDataSetChanged")
    fun setProducts(products: Products) {
        this.products = products.products!!.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder =
        CartViewHolder(
            ListItemIncartViewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.apply {
            bind(products[position])
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("formattedPrice")
        fun useFormattedPriceLabel(priceTextView: TextView, price: String) {
            priceTextView.text = String.format("$%.0f", price.toFloat())
        }

        @JvmStatic
        @BindingAdapter("inCartCustomBackground")
        fun useInCartCustomBackground(rootView: ConstraintLayout, color: String) {
            ViewCompat.setBackgroundTintList(
                rootView,
                ColorStateList.valueOf(Color.parseColor(color))
            )
            ViewCompat.setBackgroundTintMode(
                rootView,
                PorterDuff.Mode.MULTIPLY
            )
        }
    }

    inner class CartViewHolder(val binding: ListItemIncartViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductsItem?) {
            binding.product = product
            binding.removeProductIv.setOnClickListener {
                onItemClick?.invoke(product!!)
            }
        }
    }
}
