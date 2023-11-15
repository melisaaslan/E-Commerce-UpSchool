package com.maslan.capstoneupschool.ui.home

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.maslan.capstoneupschool.R
import com.maslan.capstoneupschool.data.model.response.ProductUI
import com.maslan.capstoneupschool.databinding.ItemSalesProductBinding


class SalesProductAdapter (
    private val onProductClik:(Int)->Unit,
    private val onFavClick: (ProductUI) -> Unit,
) : ListAdapter<ProductUI, SalesProductAdapter.SalesProductViewHolder>(SalesProductDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalesProductViewHolder =
        SalesProductViewHolder(
            ItemSalesProductBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onProductClik,
            onFavClick
        )

    override fun onBindViewHolder(holder: SalesProductViewHolder, position: Int) =
        holder.bind(getItem(position))

    class SalesProductViewHolder(
        private val binding: ItemSalesProductBinding,
        private val onProductClik: (Int) -> Unit,
        private val onFavClick: (ProductUI) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductUI) = with(binding) {
            tvTitle.text = product.title
            tvCategory.text = product.category
            tvPrice.text = "${product.price} ₺"

            ivFav.setBackgroundResource(
                if (product.isFav) R.drawable.ic_fav
                else R.drawable.ic_fav_unselected
            )

            Glide.with(imgProduct).load(product.imageOne).into(imgProduct)

            if (product.saleState == true) {
                tvSalePrice.isVisible = true
                tvSalePrice.text = "${product.salePrice} ₺"
                tvPrice.text = "${product.price} ₺"
                tvPrice.paintFlags = tvPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
            else {
                tvPrice.text = "${product.price} ₺"
                tvSalePrice.isVisible = false
            }


            root.setOnClickListener {
                onProductClik(product.id)
            }

            ivFav.setOnClickListener {
                onFavClick(product)
        }
        }
    }

    class SalesProductDiffCallBack : DiffUtil.ItemCallback<ProductUI>() {
        override fun areItemsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem == newItem
        }
    }

}