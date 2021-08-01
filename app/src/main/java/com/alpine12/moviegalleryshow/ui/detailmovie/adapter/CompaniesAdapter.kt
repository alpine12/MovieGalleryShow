package com.alpine12.moviegalleryshow.ui.detailmovie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alpine12.moviegalleryshow.BuildConfig
import com.alpine12.moviegalleryshow.R
import com.alpine12.moviegalleryshow.data.model.detailmovie.Companies
import com.alpine12.moviegalleryshow.databinding.ItemListCompaniesProductionBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class CompaniesAdapter : ListAdapter<Companies, CompaniesAdapter.ViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListCompaniesProductionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class ViewHolder(private val binding: ItemListCompaniesProductionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(companies: Companies) {
            binding.apply {
                Glide.with(binding.root)
                    .load(BuildConfig.imageUrl + companies.logo_path)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_movie_nav)
                    .dontAnimate()
                    .into(binding.ivCompanies)

                tvTitleCompanies.text = companies.name
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Companies>() {
        override fun areItemsTheSame(oldItem: Companies, newItem: Companies): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Companies, newItem: Companies): Boolean {
            return oldItem == newItem
        }
    }

}

