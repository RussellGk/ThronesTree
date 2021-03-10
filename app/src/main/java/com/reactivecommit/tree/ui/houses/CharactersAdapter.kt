package com.reactivecommit.tree.ui.houses

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.reactivecommit.tree.data.CharacterItem
import com.reactivecommit.tree.data.HouseType
import com.reactivecommit.tree.databinding.ItemCharacterBinding

class CharacterAdapter(private val listener: (CharacterItem) -> Unit) :
    ListAdapter<CharacterItem, CharacterAdapter.CharacterVH>(DIFF_CALLBACK) {

    lateinit var bindind: ItemCharacterBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterVH {
        bindind = ItemCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterVH(bindind)
    }

    override fun onBindViewHolder(holder: CharacterVH, position: Int) {
        holder.bindTo(getItem(position), listener)
    }

    inner class CharacterVH(bindind: ItemCharacterBinding) : RecyclerView.ViewHolder(bindind.root) {

        fun bindTo(item: CharacterItem, listener: (CharacterItem) -> Unit) {
            bindind.imageAvatar.setImageResource(HouseType.fromString(item.house).icon)
            bindind.textViewName.text = item.name.ifEmpty { "Information is unknown" }
            bindind.textViewAliases.text =
                item.titles.joinToString(" • ").ifEmpty { "Information is unknown" }
            itemView.setOnClickListener {
                listener(item)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CharacterItem>() {
            override fun areItemsTheSame(oldItem: CharacterItem, newItem: CharacterItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: CharacterItem,
                newItem: CharacterItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}