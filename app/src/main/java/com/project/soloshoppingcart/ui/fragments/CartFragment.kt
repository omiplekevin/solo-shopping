package com.project.soloshoppingcart.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.project.soloshoppingcart.databinding.FragmentCartBinding
import com.project.soloshoppingcart.datamodel.Products
import com.project.soloshoppingcart.ui.adapter.CartRVAdapter
import com.project.soloshoppingcart.ui.callback.CartEventCallback
import com.project.soloshoppingcart.viewmodel.CartViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment(private val eventCallback: CartEventCallback) : Fragment() {

    companion object {
        fun newInstance(callback: CartEventCallback) = CartFragment(callback)
    }

    private val viewModel by viewModels<CartViewModel>()
    private lateinit var binding: FragmentCartBinding
    private val adapter = CartRVAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        adapter.onItemClick = {
            viewModel.removeProductToCart(it.uId)
        }
        binding.buyBtn.setOnClickListener {
            eventCallback.onProceedToCheckout()
        }
        binding.inCartRv.adapter = adapter
        viewModel.productsInCart.observe(viewLifecycleOwner) {
            eventCallback.onCartUpdated()
        }
        viewModel.products.observe(viewLifecycleOwner) {
            adapter.setProducts(it)
            updateTotals(it)
        }
        viewModel.getData()
        return binding.root
    }

    private fun updateTotals(products: Products) {
        var total = 0F
        if (products.products != null) {
            for (item in products.products!!) {
                if (item != null) {
                    total += item.price!!.toFloat()
                }
            }
        }
        binding.inCartTotalTv.text = "$%.0f".format(total)
    }

}