package com.halfplatepoha.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import java.lang.Exception
import kotlin.properties.Delegates

const val TYPE_ITEM = 1
const val TYPE_BUTTON = 2

class Adapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items: List<Model> by Delegates.observable(emptyList()) { _, oldList, newList ->
        DiffUtil.calculateDiff(object: DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
               return oldList[oldItemPosition].id == newList[newItemPosition].id
            }

            override fun getOldListSize() = oldList.size

            override fun getNewListSize() = newList.size

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].title == newList[newItemPosition].title
            }

        }).dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            TYPE_ITEM -> ItemViewHolder(layoutInflater.inflate(R.layout.item_list, parent, false))
            TYPE_BUTTON -> ButtonViewHolder(layoutInflater.inflate(R.layout.item_button, parent, false))
            else -> throw Exception("Unknown Type")
        }
    }

    override fun getItemCount() = items.size + 1 //(if (items.isNotEmpty()) 1 else 0)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(position < items.size) {
            (holder as ItemViewHolder).bind(items[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == items.size) TYPE_BUTTON
        else TYPE_ITEM
    }

    inner class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(item: Model) {
            itemView.findViewById<AppCompatTextView>(R.id.title).text = item.title
            itemView.findViewById<AppCompatTextView>(R.id.subtitle).text = item.subtitle
            itemView.findViewById<AppCompatTextView>(R.id.description).text = item.description
        }
    }

    inner class ButtonViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

}

data class Model(val id: Int, val title: String, val subtitle: String, val description: String)