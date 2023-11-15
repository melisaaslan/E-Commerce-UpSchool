package com.maslan.capstoneupschool.ui.favorite

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
import com.maslan.capstoneupschool.databinding.FragmentFavoriteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private val binding by viewBinding(FragmentFavoriteBinding::bind)

    private val viewModel by viewModels<FavoriteViewModel>()

    private val productAdapter = FavoriteAdapter(onProductClick = ::onProductClick, onDeleteClick = ::onDeleteClick)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getFavorites()
        with(binding) {
            rvFavProducts.adapter = productAdapter

            button3.setOnClickListener {
                viewModel.deleteAllProducts()
            }
        }
        observeData()
    }
    private fun observeData() = with(binding) {

        viewModel.favoritesState.observe(viewLifecycleOwner) { state ->
            when (state) {
                FavoritesState.Loading -> progressBar.visible()

                is FavoritesState.SuccessState -> {
                    progressBar.gone()
                    productAdapter.submitList(state.products)
                }

                is FavoritesState.EmptyScreen -> {
                    progressBar.gone()
                    ivEmpty.visible()
                    tvEmpty.visible()
                    rvFavProducts.gone()
                    tvEmpty.text = state.failMessage
                }

                is FavoritesState.ShowPopUp -> {
                    progressBar.gone()
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }
            }
        }
    }

    private fun onProductClick(id: Int) {
        findNavController().navigate(FavoriteFragmentDirections.favoritesToDetail(id))
    }

    private fun onDeleteClick(product: ProductUI) {
        viewModel.deleteFromFavorites(product)
    }
}