package com.project.soloshoppingcart.ui.fragments

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import com.project.soloshoppingcart.R
import com.project.soloshoppingcart.databinding.ChipFilterBinding
import com.project.soloshoppingcart.databinding.FragmentProductsBinding
import com.project.soloshoppingcart.databinding.LayoutPromptBinding
import com.project.soloshoppingcart.datamodel.Products
import com.project.soloshoppingcart.datamodel.ProductsItem
import com.project.soloshoppingcart.ui.adapter.ProductsRVAdapter
import com.project.soloshoppingcart.ui.callback.CartEventCallback
import com.project.soloshoppingcart.viewmodel.ProductsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProductsFragment(private val eventCallback: CartEventCallback) : Fragment() {

    private val viewModel by viewModels<ProductsViewModel>()
    lateinit var binding: FragmentProductsBinding
    private val adapter = ProductsRVAdapter()

    companion object {
        fun newInstance(callback: CartEventCallback) = ProductsFragment(callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductsBinding.inflate(inflater, container, false)
        adapter.onItemClick = { productItem ->
            val copyProduct = ProductsItem(
                productItem.bgColor,
                productItem.price,
                productItem.name,
                productItem.id,
                productItem.category,
                System.currentTimeMillis())
            showPromptInCart(copyProduct)
            viewModel.addProductToCart(copyProduct)
        }
        binding.productsRv.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        binding.productsRv.adapter = adapter
        viewModel.products.observe(viewLifecycleOwner) {
            createAndSetFilter(it)
            adapter.setProducts(it)
        }
        viewModel.productsInCart.observe(viewLifecycleOwner) {
            eventCallback.onCartUpdated()
        }
        viewModel.getData()
        return binding.root
    }

    private fun createAndSetFilter(products: Products) {
        //pre-filter categories
        val chipGroupParent = binding.chipGroupHolder
        chipGroupParent.removeAllViews() //reset view
        if (products.products != null) {
            var categoryList = mutableListOf<String>()
            categoryList.add("All")
            for (productItem in products.products!!) {
                categoryList.add(productItem?.category!!)
            }
            categoryList = categoryList.distinct().toMutableList()
            for (category in categoryList) {
                val chipFilter = createChipFilter(category)
                chipGroupParent.addView(chipFilter)
            }
        }
    }

    private fun createChipFilter(category: String): Chip {
        val chipFilter = ChipFilterBinding.inflate(layoutInflater).root
        chipFilter.text = category
        return chipFilter
    }

    private fun showPromptInCart(product: ProductsItem) {
        val snackbar = Snackbar.make(requireView(), "", Snackbar.LENGTH_LONG)
        val sbViewBinding = LayoutPromptBinding.inflate(layoutInflater)
        ViewCompat.setBackgroundTintList(
            sbViewBinding.promptTv,
            ColorStateList.valueOf(Color.parseColor(product.bgColor))
        )
        ViewCompat.setBackgroundTintMode(
            sbViewBinding.promptTv,
            PorterDuff.Mode.MULTIPLY
        )
        val snackbarLayout = snackbar.view as SnackbarLayout
        snackbarLayout.setPadding(0, 0, 0, 0)
        snackbarLayout.setBackgroundColor(Color.TRANSPARENT)

        val message = product.name.plus(" ").plus(getString(R.string.prompt_append_string))
        sbViewBinding.promptTv.text =
            setCustomFontTypeSpan(message, product.name!!.length, R.font.montserrat_light)

        sbViewBinding.dismissPromptIv.setOnClickListener { snackbar.dismiss() }
        snackbarLayout.addView(sbViewBinding.root, 0)
        snackbar.show()
    }

    private fun setCustomFontTypeSpan(
        source: String?,
        endIndex: Int,
        font: Int
    ): SpannableString? {
        val spannableString = SpannableString(source)
        val typeface = ResourcesCompat.getFont(requireContext(), font)
        spannableString.setSpan(
            StyleSpan(typeface!!.style),
            0, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannableString
    }

}