package com.maslan.capstoneupschool.ui.favorite

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.maslan.capstoneupschool.data.model.response.ProductUI
import com.maslan.capstoneupschool.databinding.ItemFavProductBinding

class FavoriteAdapter(
    private val onProductClick: (Int) -> Unit,
    private val onDeleteClick: (ProductUI) -> Unit
) : ListAdapter<ProductUI, FavoriteAdapter.FavProductViewHolder>(FavProductDiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavProductViewHolder {
        return FavProductViewHolder(
            ItemFavProductBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onProductClick,
            onDeleteClick
        )
    }
    override fun onBindViewHolder(holder: FavProductViewHolder, position: Int) = holder.bind(getItem(position))

    class FavProductViewHolder(
        private val binding: ItemFavProductBinding,
        private val onProductClick: (Int) -> Unit,
        private val onDeleteClick: (ProductUI) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductUI) {
            with(binding) {
                tvTitle.text = product.title
                tvPrice.text = "${product.price} ₺"
                if (product.saleState == true) {
                    tvSalePrice.isVisible = true
                    tvSalePrice.text = "${product.salePrice} ₺"
                    tvPrice.text = "${product.price} ₺"
                    tvPrice.paintFlags = tvPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    tvPrice.text = "${product.price} ₺"
                    tvSalePrice.isVisible = false
                }
                
                Glide.with(ivProduct).load(product.imageOne).into(ivProduct)

                root.setOnClickListener {
                    onProductClick(product.id)
                }
                ivDelete.setOnClickListener {
                    onDeleteClick(product)
                }
            }
        }
    }

    class FavProductDiffUtilCallBack : DiffUtil.ItemCallback<ProductUI>() {
        override fun areItemsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem == newItem
        }
    }
}