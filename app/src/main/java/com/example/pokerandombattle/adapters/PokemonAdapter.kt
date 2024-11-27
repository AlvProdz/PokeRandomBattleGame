package com.example.pokerandombattle.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokerandombattle.data.Pokemon
import com.example.pokerandombattle.databinding.ItemPokemonBinding
import com.squareup.picasso.Picasso

class PokemonAdapter(private var items: List<Pokemon>, val onItemClick: (Pokemon) -> Unit) : RecyclerView.Adapter<PokemonViewHolder>() {
    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val superHeroItem = items[position]
        holder.init(superHeroItem)
        holder.itemView.setOnClickListener {
            onItemClick(superHeroItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val binding = ItemPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PokemonViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItems(items: List<Pokemon>) {
        this.items = items
        notifyDataSetChanged()
    }
}

class PokemonViewHolder(private val binding: ItemPokemonBinding) : RecyclerView.ViewHolder(binding.root) {

    fun init(pokemon: Pokemon) {
        binding.pokemonName.text = pokemon.name
        Picasso.get().load(pokemon.sprites.otherSprites.officialArtWork.frontImgURL).into(binding.pokemonImageView)
        // binding.cardHero.setCardBackgroundColor(pokemon.getAlignmentColor())
    }
}


