package com.project.soloshoppingcart.ui.fragments

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.project.soloshoppingcart.R
import com.project.soloshoppingcart.databinding.FragmentOrderConfirmationBinding
import com.project.soloshoppingcart.databinding.LayoutPromptBinding
import com.project.soloshoppingcart.datamodel.Products
import com.project.soloshoppingcart.ui.callback.CartEventCallback
import com.project.soloshoppingcart.viewmodel.CheckoutViewModel
import com.project.soloshoppingcart.viewmodel.OrdConfirmationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdConfirmationFragment(
    private val eventCallback: CartEventCallback,
    private val orderReceiptRef: String
) : Fragment() {

    companion object {
        fun newInstance(callback: CartEventCallback, orderReceiptRef: String) =
            OrdConfirmationFragment(callback, orderReceiptRef)
    }

    private val viewModel by viewModels<OrdConfirmationViewModel>()
    private lateinit var binding: FragmentOrderConfirmationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderConfirmationBinding.inflate(inflater, container, false)
        binding.returnToProductsBtn.setOnClickListener {
            onReturnToProducts()
        }
        binding.orderIdTv.text = String.format(getString(R.string.label_order_id, orderReceiptRef))
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

    private fun onReturnToProducts() {
        //basic validation
        eventCallback.onReturnToProducts()
    }

    private fun showPromptFieldCheck(message: String) {
        val snackbar = Snackbar.make(requireView(), "", Snackbar.LENGTH_LONG)
        val sbViewBinding = LayoutPromptBinding.inflate(layoutInflater)
        ViewCompat.setBackgroundTintList(
            sbViewBinding.promptTv,
            ColorStateList.valueOf(Color.parseColor("#F95A2C"))
        )
        ViewCompat.setBackgroundTintMode(
            sbViewBinding.promptTv,
            PorterDuff.Mode.MULTIPLY
        )
        val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout
        snackbarLayout.setPadding(0, 0, 0, 0)
        snackbarLayout.setBackgroundColor(Color.TRANSPARENT)

        sbViewBinding.promptTv.setTextColor(Color.parseColor("#FFFFFF"))
        sbViewBinding.promptTv.text = message

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