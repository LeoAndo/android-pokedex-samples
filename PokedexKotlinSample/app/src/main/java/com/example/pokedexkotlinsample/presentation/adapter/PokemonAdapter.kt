package com.example.pokedexkotlinsample.presentation.adapter

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.example.pokedexkotlinsample.R
import com.example.pokedexkotlinsample.databinding.ListItemPokemonBinding
import com.example.pokedexkotlinsample.domain.model.PokemonModel
import com.example.pokedexkotlinsample.domain.model.PokemonResult
import com.example.pokedexkotlinsample.domain.model.toPokemonModel
import com.example.pokedexkotlinsample.presentation.util.extractId
import com.example.pokedexkotlinsample.presentation.util.getPictureUrl


class PokemonAdapter(private val navigate: (PokemonModel) -> Unit) :
    PagingDataAdapter<PokemonResult, PokemonAdapter.VH>(DIFF_CALLBACK), ImageLoadable {

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
        @ColorInt
        var dominantColor: Int = 0
        fun bind(pokemonResult: PokemonResult) {
            binding.apply {
                val model = pokemonResult.toPokemonModel()
                pokemonItemTitle.text = "${model.id}\n${model.name}"
                loadImage(this, pokemonResult)
                root.setOnClickListener {
                    model.also { it.dominantColor = dominantColor }
                    navigate(model)
                }
            }
        }

        private fun loadImage(binding: ListItemPokemonBinding, pokemonResult: PokemonResult) {
            binding.apply {
                val requestBuilder: RequestBuilder<Drawable> =
                    pokemonItemImage.createGlideRequestBuilder(
                        imageUrl = pokemonResult.url.getPictureUrl(),
                        fitCenter = true
                    )
                requestBuilder.into(object : DrawableImageViewTarget(pokemonItemImage) {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        super.onResourceReady(resource, transition)
                        if (resource is GifDrawable) {
                            resource.setLoopCount(3)
                        }
                        val bitmap = (resource as? BitmapDrawable)?.bitmap ?: return
                        // async
                        Palette.Builder(bitmap).generate {
                            it?.let { palette ->
                                dominantColor = palette.getDominantColor(
                                    ContextCompat.getColor(root.context, R.color.black)
                                )
                                pokemonItemImage.setBackgroundColor(dominantColor)
                                pokemonItemTitle.setTextColor(dominantColor)
                            }
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