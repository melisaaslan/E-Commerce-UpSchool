package com.maslan.capstoneupschool.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.maslan.capstoneupschool.R
import com.maslan.capstoneupschool.common.gone
import com.maslan.capstoneupschool.common.viewBinding
import com.maslan.capstoneupschool.common.visible
import com.maslan.capstoneupschool.data.model.response.ProductUI
import com.maslan.capstoneupschool.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home)  {

    private val binding by viewBinding(FragmentHomeBinding::bind)

    private val viewModel by viewModels<HomeViewModel>()

    private val productAdapter = ProductsAdapter(onProductClick = ::onProductClick, onFavClick = ::onFavClick)

    private val salesProductAdapter = SalesProductAdapter(onProductClik = ::onProductClick , onFavClick = ::onFavClick)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getProducts()
        viewModel.getSaleProducts()

        with(binding) {
            rvDiscountedProducts.adapter=salesProductAdapter
            rvProducts.adapter = productAdapter


            ivLogout.setOnClickListener {
                viewModel.logOut()
            }
        }

        observeData()
    }

    private fun observeData() = with(binding) {
        viewModel.homeState.observe(viewLifecycleOwner) { state ->
            when (state) {
                HomeState.Loading -> progressBar.visible()

                is HomeState.SuccessState -> {
                    progressBar.gone()
                    productAdapter.submitList(state.products)
                }

                is HomeState.EmptyScreen -> {
                    progressBar.gone()
                    ivEmpty.visible()
                    tvEmpty.visible()
                    tvEmpty.text = state.failMessage
                }

                is HomeState.ShowPopUp -> {
                    progressBar.gone()
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }

                HomeState.GoToSignIn -> {
                    findNavController().navigate(R.id.homeToSplash)
                }
            }
        }

        viewModel.salesState.observe(viewLifecycleOwner) { state ->
            when (state) {
                SalesState.Loading -> progressBar.visible()

                is SalesState.SuccessState -> {
                    progressBar.gone()
                    salesProductAdapter.submitList(state.products)
                }
                is SalesState.EmptyScreen -> {
                    progressBar.gone()
                    rvDiscountedProducts.gone()
                    tvEmpty.text = state.failMessage
                    tvEmpty.visible()
                    ivEmpty.visible()
                }

                is SalesState.ShowPopUp -> {
                    progressBar.gone()
                    Snackbar.make(requireView(), state.errorMessage.orEmpty(), 1000).show()
                }
            }
        }
    }

    private fun onProductClick(id: Int) {
        findNavController().navigate(HomeFragmentDirections.homeToDetail(id))
    }

    private fun onFavClick(product: ProductUI) {
        viewModel.setFavoriteState(product)
    }
}