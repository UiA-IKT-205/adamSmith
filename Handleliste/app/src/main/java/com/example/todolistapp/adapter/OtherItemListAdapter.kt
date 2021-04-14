package com.example.todolistapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistapp.ChildItem
import com.example.todolistapp.R

class OtherItemListAdapter(
    var allItemList: ArrayList<ChildItem>,
    var deleteItem: (String) -> Unit,
    var updateItem: (String, String, Boolean) -> Unit
) : RecyclerView.Adapter<OtherItemListAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.other_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = allItemList[position]
        holder.itemCheckbox.apply {
            text = item.title
            isChecked = item.isChecked
        }
        holder.ivDelete.setOnClickListener {
            deleteItem(item.id)
        }
        holder.itemCheckbox.setOnClickListener {
            updateItem(item.id, item.title, holder.itemCheckbox.isChecked)
        }
    }

    override fun getItemCount(): Int {
        return allItemList.size
    }


    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var ivDelete: ImageView = view.findViewById(R.id.ivDelete)
        var itemCheckbox: CheckBox = view.findViewById(R.id.cbItem)
    }
}