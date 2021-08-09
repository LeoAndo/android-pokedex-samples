package com.example.pokedexkotlinsample.presentation.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.example.pokedexkotlinsample.databinding.ListItemPokemonBinding
import com.example.pokedexkotlinsample.domain.model.PokemonResult
import com.example.pokedexkotlinsample.presentation.extractId
import com.example.pokedexkotlinsample.presentation.getGifUrl
import com.example.pokedexkotlinsample.presentation.getPictureUrl


class PokemonAdapter(private val navigate: (PokemonResult, Int, String) -> Unit) :
    PagingDataAdapter<PokemonResult, PokemonAdapter.VH>(DIFF_CALLBACK) {

    override fun onBindViewHolder(holder: VH, position: Int) {
        val data = getItem(position)!!
        holder.bind(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            ListItemPokemonBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    inner class VH(private val binding: ListItemPokemonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var dominantColor: Int = 0
        var pictureUrl = ""
        fun bind(pokemonResult: PokemonResult) {
            binding.apply {
                pokemonItemTitle.text = pokemonResult.name.capitalize()
                loadImage(this, pokemonResult)
                root.setOnClickListener {
                    navigate(pokemonResult, dominantColor, pictureUrl)
                }
            }
        }

        private fun loadImage(binding: ListItemPokemonBinding, pokemonResult: PokemonResult) {
            pictureUrl = pokemonResult.url.getGifUrl()
            binding.apply {
                Glide.with(root)
                    .load(pokemonResult.url.getPictureUrl())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(object : DrawableImageViewTarget(pokemonItemImage) {
                        override fun onLoadFailed(errorDrawable: Drawable?) {
                            super.onLoadFailed(errorDrawable)
                            progressCircular.isVisible = false
                        }

                        override fun onResourceReady(
                            resource: Drawable,
                            transition: Transition<in Drawable>?
                        ) {
                            super.onResourceReady(resource, transition)
                            progressCircular.isVisible = false
                            if (resource is GifDrawable) {
                                resource.setLoopCount(3)
                            }
                        }
                    })
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount) NETWORK_VIEW_TYPE else PRODUCT_VIEW_TYPE
    }

    companion object {
        const val NETWORK_VIEW_TYPE = 2
        const val PRODUCT_VIEW_TYPE = 1
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PokemonResult>() {
            override fun areItemsTheSame(oldItem: PokemonResult, newItem: PokemonResult): Boolean {
                return oldItem.url.extractId() == newItem.url.extractId()
            }

            override fun areContentsTheSame(
                oldItem: PokemonResult,
                newItem: PokemonResult
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}