package com.my.targetDemoApp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.my.targetDemoApp.databinding.ItemMainBinding

class ItemsAdapter(private val onDelete: (Int) -> Unit) :
        RecyclerView.Adapter<ItemsAdapter.MainActivityViewHolder>() {

    private var adItems: ArrayList<ListItem>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainActivityViewHolder {
        return MainActivityViewHolder(
                ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return adItems?.size ?: 0
    }

    override fun onBindViewHolder(holder: MainActivityViewHolder, position: Int) {
        adItems?.get(position)
                ?.let { holder.bind(it) }
    }

    override fun onViewRecycled(holder: MainActivityViewHolder) {
        super.onViewRecycled(holder)
        holder.containerViewBinding.root.setOnClickListener(null)
    }

    fun setItems(types: ArrayList<ListItem>) {
        adItems = types
        notifyDataSetChanged()
    }

    fun addItem(listItem: ListItem) {
        adItems?.let {
            it.add(listItem)
            notifyItemInserted(it.size)
        }
    }

    fun deleteItem(num: Int) {
        val items = adItems ?: return
        if (num >= items.size) {
            return
        }
        items.removeAt(num)
        adItems = items
        notifyItemRemoved(num)
        onDelete.invoke(num)
    }

    class MainActivityViewHolder(var containerViewBinding: ItemMainBinding) :
            RecyclerView.ViewHolder(containerViewBinding.root) {
        fun bind(listItem: ListItem) {
            containerViewBinding.tvTitle.text = listItem.title
            containerViewBinding.tvDescription.text = listItem.description
            containerViewBinding.root.setOnClickListener { listItem.onClick.invoke() }
        }
    }

    class ListItem(var onClick: () -> Unit, var title: String, var description: String)
}

