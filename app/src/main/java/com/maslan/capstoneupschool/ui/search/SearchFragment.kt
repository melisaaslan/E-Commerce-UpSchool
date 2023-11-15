package com.maslan.capstoneupschool.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.maslan.capstoneupschool.R
import com.maslan.capstoneupschool.common.gone
import com.maslan.capstoneupschool.common.viewBinding
import com.maslan.capstoneupschool.common.visible
import com.maslan.capstoneupschool.databinding.FragmentSearchBinding
import com.maslan.capstoneupschool.ui.favorite.FavoritesState
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search), SearchAdapter.SearchProductListener {

    private val binding by viewBinding(FragmentSearchBinding::bind)

    private val viewModel by viewModels<SearchViewModel>()

    private val searchAdapter by lazy { SearchAdapter(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            rvSearch.adapter = searchAdapter

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.let { query ->
                        if (query.length >= 3) {
                            viewModel.getSearchProduct(query)
                        } else {
                            Snackbar.make(
                                requireView(),
                                "Enter at least 3 characters for the product you want to search",
                                1000
                            ).show()
                        }
                    }
                    return true
                }
            })
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val action = SearchFragmentDirections.searchToHome()
                findNavController().navigate(action)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        observeData()
    }

    private fun observeData() = with(binding) {

        viewModel.searchState.observe(viewLifecycleOwner) { state ->

            when (state) {
                is SearchViewModel.SearchState.Loading -> {
                    progressBar2.visible()
                }

                is SearchViewModel.SearchState.SuccessState -> {
                    progressBar2.gone()
                    searchAdapter.submitList(state.products)
                }

                is SearchViewModel.SearchState.ShowPopUp -> {
                    progressBar2.gone()
                }

                else -> {}
            }
        }
    }

    override fun onProductClick(id: Int) {
        val action = SearchFragmentDirections.searchToDetail(id)
        findNavController().navigate(action)
    }
}