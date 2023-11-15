package com.maslan.capstoneupschool.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.maslan.capstoneupschool.R
import com.maslan.capstoneupschool.common.gone
import com.maslan.capstoneupschool.common.viewBinding
import com.maslan.capstoneupschool.common.visible
import com.maslan.capstoneupschool.data.model.request.AddToCartRequest
import com.maslan.capstoneupschool.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {

    private val binding by viewBinding(FragmentDetailBinding::bind)
    private val viewModel by viewModels<DetailViewModel>()
    private val args by navArgs<DetailFragmentArgs>()
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        val userId = auth.currentUser!!.uid

        viewModel.getProductDetail(args.id)
        val request = AddToCartRequest(userId, args.id)

        with(binding){
            btnAddCart.setOnClickListener {
                viewModel.addProductToCart(request)
                Snackbar.make(requireView(), "The product has been added to your cart", 1000).show()
            }

        }
        observeData()

    }

    private fun observeData() = with(binding) {

        icBackHome.setOnClickListener {
            findNavController().navigate(DetailFragmentDirections.detailToHome())
        }

        viewModel.detailState.observe(viewLifecycleOwner) { state ->
            when (state) {
                DetailState.Loading -> progressBar.visible()

                is DetailState.SuccessState -> {
                    progressBar.gone()
                    Glide.with(ivProduct).load(state.product.imageOne).into(ivProduct)
                    tvTitle.text = state.product.title
                    tvDescription.text = state.product.description
                    tvCategory.text = state.product.category
                    ratingBar.rating = state.product.rate.toFloat()

                    btnFav.setBackgroundResource(
                        if (state.product.isFav)R.drawable.ic_fav
                        else R.drawable.ic_fav_unselected
                    )
                    btnFav.setOnClickListener{
                        viewModel.setFavoriteState(state.product)
                    }

                    if (state.product.saleState == true) {
                        tvSale.isVisible = true
                        tvSale.text = "${state.product.salePrice} ₺"
                        tvPrice.text = "${state.product.price} ₺"


                    } else {
                        tvPrice.text = "${state.product.price} ₺"
                        tvSale.isVisible = false
                    }
                }
                is DetailState.EmptyScreen -> {
                    progressBar.gone()
                    ivEmpty.visible()
                    tvEmpty.visible()
                    tvEmpty.text = state.failMessage
                }

                is DetailState.ShowPopUp -> {
                    progressBar.gone()
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }

            }
        }
    }

    }


