package com.maslan.capstoneupschool.ui.cart

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.maslan.capstoneupschool.R
import com.maslan.capstoneupschool.common.gone
import com.maslan.capstoneupschool.common.viewBinding
import com.maslan.capstoneupschool.common.visible
import com.maslan.capstoneupschool.databinding.FragmentCartBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment(R.layout.fragment_cart), CartProductsAdapter.CartProductListener {

    private val binding by viewBinding(FragmentCartBinding::bind)

    private val viewModel by viewModels<CartViewModel>()

    private lateinit var cartProductsAdapter: CartProductsAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCartProducts()

        cartProductsAdapter = CartProductsAdapter(this)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val action = CartFragmentDirections.cartToHome()
                findNavController().navigate(action)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        with(binding) {
            rvCartProducts.adapter = cartProductsAdapter

            btnClear.setOnClickListener {
                viewModel.clearProduct()
            }

            btnBuy.setOnClickListener {
                findNavController().navigate(CartFragmentDirections.cartToPayment())
            }
        }
        observeData()
    }

    private fun observeData() = with(binding) {
        viewModel.cartState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is CartState.Loading -> {
                    progressBar.visible()
                }

                is CartState.SuccessState -> {
                    cartProductsAdapter.submitList(state.product)
                    rvCartProducts.isVisible = state.product.isNotEmpty()
                    progressBar.gone()

                    if (state.product.isEmpty()) {
                        rvCartProducts.visibility = View.GONE
                        btnClear.visibility = View.GONE
                        btnBuy.visibility = View.GONE
                        tvError.visibility = View.VISIBLE
                        tvError.setText("There are no products in your cart")
                    } else {
                        rvCartProducts.visibility = View.VISIBLE
                        btnClear.visibility = View.VISIBLE
                        btnBuy.visibility = View.VISIBLE
                        tvError.visibility = View.GONE

                    }
                }

                is CartState.ShowPopUp -> {
                    progressBar.gone()
                }
                else -> {}
            }
        }
    }

    override fun onProductClick(id: Int) {
        val action = CartFragmentDirections.cartToDetail(id)
        findNavController().navigate(action)
    }

    override fun onDeleteClick(id: Int) {
        viewModel.deleteProduct(id)
    }

    override fun onIncreaseClick(price: Double) {
    }

    override fun onDecreaseClick(price: Double) {
    }

}






