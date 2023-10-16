package com.project.soloshoppingcart.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.project.soloshoppingcart.R
import com.project.soloshoppingcart.databinding.ActivityMainBinding
import com.project.soloshoppingcart.datamodel.OrderReceipt
import com.project.soloshoppingcart.ui.fragments.ProductsFragment
import com.project.soloshoppingcart.repository.DataRepository
import com.project.soloshoppingcart.ui.callback.CartEventCallback
import com.project.soloshoppingcart.ui.fragments.CartFragment
import com.project.soloshoppingcart.ui.fragments.CheckoutFragment
import com.project.soloshoppingcart.ui.fragments.OrdConfirmationFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), CartEventCallback {

    @Inject
    lateinit var repository: DataRepository

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeView()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragmentHolder)
        if (fragment is OrdConfirmationFragment) {
            onReturnToProducts()
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initializeView() {
        countAndUpdateBadge()

        supportFragmentManager.addOnBackStackChangedListener {
            val fragment = supportFragmentManager.findFragmentById(R.id.fragmentHolder)
            when (fragment) {
                is ProductsFragment -> {
                    binding.shopPageLabelTv.text = getString(R.string.toolbar_page_title_products)
                }

                is CartFragment -> {
                    binding.shopPageLabelTv.text = getString(R.string.toolbar_page_title_cart)
                }

                is CheckoutFragment -> {
                    binding.shopPageLabelTv.text = getString(R.string.toolbar_page_title_checkout)
                }

                is OrdConfirmationFragment -> {
                    binding.shopPageLabelTv.text = getString(R.string.toolbar_page_title_ordconfirm)
                }
            }
        }

        //setting starting page view
        switchToProductsView()
        binding.shopPageLabelTv.text = getString(R.string.toolbar_page_title_products)
        binding.toolbarLayoutIncl.cartCountTv.setOnClickListener {
            switchToCartView()
        }
    }

    private fun switchToProductsView() {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(
            binding.fragmentHolder.id,
            ProductsFragment.newInstance(this),
            getString(R.string.toolbar_page_title_products)
        )
            .commit()
    }

    private fun switchToCartView() {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(
            binding.fragmentHolder.id,
            CartFragment.newInstance(this),
            getString(R.string.toolbar_page_title_cart)
        )
            .addToBackStack("cart")
            .commit()
    }

    private fun switchToCheckoutView() {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(
            binding.fragmentHolder.id,
            CheckoutFragment.newInstance(this),
            getString(R.string.toolbar_page_title_checkout)
        )
            .addToBackStack("checkout")
            .commit()
    }

    private fun switchToOrderConfirmation(orderReceiptRef: String) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(
            binding.fragmentHolder.id,
            OrdConfirmationFragment.newInstance(this, orderReceiptRef),
            getString(R.string.toolbar_page_title_ordconfirm)
        )
            .addToBackStack("ordconfirm")
            .commit()
    }

    private fun countAndUpdateBadge() {
        val products = repository.readInCart()
        Log.d(javaClass.name, "countAndUpdateBadge: $products")
        if (products?.products != null) {
            val count = products.products!!.size
            if (count > 0) {
                binding.toolbarLayoutIncl.cartCountTv.visibility = View.VISIBLE
                binding.toolbarLayoutIncl.cartCountTv.text = String.format("%d", count)
            }
        } else {
            binding.toolbarLayoutIncl.cartCountTv.visibility = View.INVISIBLE
            binding.toolbarLayoutIncl.cartCountTv.text = String.format("%d", 0)
        }
    }

    override fun onCartUpdated() {
        countAndUpdateBadge()
    }

    override fun onProceedToCheckout() {
        switchToCheckoutView()
    }

    override fun onOrderConfirmation(orderReceipt: OrderReceipt) {
        switchToOrderConfirmation(orderReceipt.orderId)
    }

    override fun onReturnToProducts() {
        supportFragmentManager.clearBackStack(getString(R.string.toolbar_page_title_ordconfirm))
        supportFragmentManager.clearBackStack(getString(R.string.toolbar_page_title_checkout))
        supportFragmentManager.clearBackStack(getString(R.string.toolbar_page_title_cart))
        switchToProductsView()
    }
}