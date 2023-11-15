package com.maslan.capstoneupschool.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.maslan.capstoneupschool.common.loadImage
import com.maslan.capstoneupschool.data.model.response.ProductUI
import com.maslan.capstoneupschool.databinding.ItemSearchBinding
class SearchAdapter (
    private val searchProductListener: SearchProductListener
) : ListAdapter<ProductUI, SearchAdapter.SearchProductViewHolder>(ProductDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchProductViewHolder =
        SearchProductViewHolder(
            ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            searchProductListener
        )

    override fun onBindViewHolder(holder: SearchProductViewHolder, position: Int) = holder.bind(getItem(position))

    class SearchProductViewHolder(
        private val binding: ItemSearchBinding,
        private val searchProductListener: SearchProductListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductUI) = with(binding) {
            tvTitle.text = product.title
            imgProduct.loadImage(product.imageOne)
            ratingBar.rating = product.rate.toFloat()

            root.setOnClickListener {
                searchProductListener.onProductClick(product.id)
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

    interface SearchProductListener {
        fun onProductClick(id: Int)
    }

}