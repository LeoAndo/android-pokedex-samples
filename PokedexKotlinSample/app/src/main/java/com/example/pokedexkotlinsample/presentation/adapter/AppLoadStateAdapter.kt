package com.example.pokedexkotlinsample.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedexkotlinsample.databinding.NetworkStateItemBinding

class AppLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<AppLoadStateAdapter.VH>() {

    inner class VH(private val binding: NetworkStateItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindTo(loadState: LoadState) {
            binding.apply {
                progressBar.isVisible = loadState is LoadState.Loading
                retryButton.isVisible = loadState is LoadState.Error
                errorMsg.isVisible =
                    !(loadState as? LoadState.Error)?.error?.localizedMessage.isNullOrBlank()
                errorMsg.text = (loadState as? LoadState.Error)?.error?.localizedMessage
                retryButton.setOnClickListener { retry() }
            }
        }
    }

    override fun onBindViewHolder(holder: VH, loadState: LoadState) {
        holder.bindTo(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): VH {
        val binding = NetworkStateItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding)
    }
}