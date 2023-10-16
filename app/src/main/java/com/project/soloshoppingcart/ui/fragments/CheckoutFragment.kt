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
import com.project.soloshoppingcart.databinding.FragmentCheckoutBinding
import com.project.soloshoppingcart.databinding.LayoutPromptBinding
import com.project.soloshoppingcart.datamodel.Products
import com.project.soloshoppingcart.ui.callback.CartEventCallback
import com.project.soloshoppingcart.viewmodel.CheckoutViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckoutFragment(private val eventCallback: CartEventCallback) : Fragment() {

    companion object {
        fun newInstance(callback: CartEventCallback) = CheckoutFragment(callback)
    }

    private val viewModel by viewModels<CheckoutViewModel>()
    private lateinit var binding: FragmentCheckoutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        binding.payBtn.setOnClickListener {
            onPay()
        }
        viewModel.productsInCart.observe(viewLifecycleOwner) {
            eventCallback.onCartUpdated()
        }
        viewModel.products.observe(viewLifecycleOwner) {
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
        binding.payBtn.text = String.format(getString(R.string.btn_buy_template), total)
    }

    private fun onPay() {
        //basic validation
        if (binding.termsSw.isChecked) {
            if (binding.checkoutNameEt.text.isNotEmpty() && binding.checkoutEmailEt.text.isNotEmpty()) {
                //proceed to payment
            } else {
                showPromptFieldCheck(getString(R.string.prompt_fields_incomplete))
            }
        } else {
            showPromptFieldCheck(getString(R.string.prompt_accept_tAndC))
        }
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