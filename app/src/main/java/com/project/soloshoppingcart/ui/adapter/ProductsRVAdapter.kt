package com.project.soloshoppingcart.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.project.soloshoppingcart.databinding.ListItemProductViewBinding
import com.project.soloshoppingcart.datamodel.Products
import com.project.soloshoppingcart.datamodel.ProductsItem

class ProductsRVAdapter : RecyclerView.Adapter<ProductsRVAdapter.ProductViewHolder>() {

    var onItemClick: ((ProductsItem) -> Unit)? = null
    var products = mutableListOf<ProductsItem?>()

    @SuppressLint("NotifyDataSetChanged")
    fun setProducts(products: Products) {
        this.products = products.products!!.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder =
        ProductViewHolder(
            ListItemProductViewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.apply {
            bind(products[position])
        }
    }

    companion object {
        @SuppressLint("DiscouragedApi")
        @JvmStatic
        @BindingAdapter("customImage")
        fun useCustomImage(productView: ImageView, asset: String) {
            val drawable = productView.context.resources.getIdentifier(asset, "drawable", productView.context.packageName)
            productView.setImageDrawable(AppCompatResources.getDrawable(productView.context, drawable))
        }

        @JvmStatic
        @BindingAdapter("customBackground")
        fun useCustomBackground(mcv: MaterialCardView, color: String) {
            mcv.setCardBackgroundColor(Color.parseColor(color))
        }

        @JvmStatic
        @BindingAdapter("formattedPrice")
        fun useFormattedPriceLabel(priceTextView: TextView, price: String) {
            priceTextView.text = String.format("$%s", price)
        }
    }

    inner class ProductViewHolder(val binding: ListItemProductViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductsItem?) {
            binding.product = product
            binding.addLayoutLl.setOnClickListener {
                onItemClick?.invoke(product!!)
            }
        }
    }
}
