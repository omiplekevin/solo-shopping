package com.project.soloshoppingcart.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.project.soloshoppingcart.databinding.FragmentCartBinding
import com.project.soloshoppingcart.databinding.FragmentCheckoutBinding
import com.project.soloshoppingcart.datamodel.Products
import com.project.soloshoppingcart.ui.adapter.CartRVAdapter
import com.project.soloshoppingcart.ui.callback.CartEventCallback
import com.project.soloshoppingcart.viewmodel.CartViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckoutFragment(private val eventCallback: CartEventCallback) : Fragment() {

    companion object {
        fun newInstance(callback: CartEventCallback) = CheckoutFragment(callback)
    }

    private val viewModel by viewModels<CartViewModel>()
    private lateinit var binding: FragmentCheckoutBinding
    private val adapter = CartRVAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        adapter.onItemClick = {
            viewModel.removeProductToCart(it.uId)
        }
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
    }

}