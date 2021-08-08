package com.example.pokedexkotlinsample.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedexkotlinsample.data.api.response.stats.Stat
import com.example.pokedexkotlinsample.databinding.StatItemPokemonBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StatsAdapter :
    RecyclerView.Adapter<StatsAdapter.VH>() {
    private val stats = mutableListOf<Stat>()

    fun setStats(newList: MutableList<Stat>) {
        stats.addAll(newList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            StatItemPokemonBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(stats[position])
    }

    override fun getItemCount(): Int {
        return stats.size
    }

    inner class VH(private val binding: StatItemPokemonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(stat: Stat) {
            binding.apply {
                val mProgress = progressCircular
                mProgress.secondaryProgress = MAX_BASE_STATE
                mProgress.max = MAX_BASE_STATE

                //The increment animation on progress bar is achieved by this.
                CoroutineScope(Dispatchers.Default).launch {
                    var state = 0
                    while (state <= stat.base_stat) {
                        mProgress.progress = state
                        state++
                        delay(30)
                    }
                }

                statName.text = stat.stat.name.capitalize()
                if (stat.stat.name.contains(NAME_DELIMITER)) {
                    val first = stat.stat.name.substringBefore(NAME_DELIMITER).capitalize()
                    val second = stat.stat.name.substringAfter(NAME_DELIMITER).capitalize()
                    "$first - $second".also { statName.text = it }
                }
                statCount.text = stat.base_stat.toString()
            }
        }
    }

    companion object {
        private const val MAX_BASE_STATE = 255
        private const val NAME_DELIMITER = "-"
    }
}