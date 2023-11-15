package com.maslan.capstoneupschool.ui.cart

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.maslan.capstoneupschool.common.loadImage
import com.maslan.capstoneupschool.data.model.response.ProductUI
import com.maslan.capstoneupschool.databinding.ItemCartProductBinding


class CartProductsAdapter(
    private val cartProductListener: CartProductListener,
) : androidx.recyclerview.widget.ListAdapter<ProductUI,CartProductsAdapter.CartProductViewHolder>(ProductDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductViewHolder =
        CartProductViewHolder(
            ItemCartProductBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            cartProductListener
        )

    override fun onBindViewHolder(holder: CartProductViewHolder, position: Int) = holder.bind(getItem(position))

    inner class CartProductViewHolder(
        private val binding: ItemCartProductBinding,
        private val cartProductListener: CartProductListener,

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

                ivProduct.loadImage(product.imageOne)

                root.setOnClickListener {
                    cartProductListener.onProductClick(product.id)
                }

                ivDelete.setOnClickListener {
                    cartProductListener.onDeleteClick(product.id)
                }
            }
        }
    }

    class ProductDiffCallBack : DiffUtil.ItemCallback<ProductUI>() {
        override fun areItemsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem == newItem
        }
    }

    interface CartProductListener {
        fun onProductClick(id: Int)
        fun onDeleteClick(id: Int)
        fun onIncreaseClick(price: Double)
        fun onDecreaseClick(price: Double)
    }
}