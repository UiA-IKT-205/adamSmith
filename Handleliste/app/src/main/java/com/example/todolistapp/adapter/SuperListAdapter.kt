package com.example.todolistapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistapp.R
import com.example.todolistapp.SuperListItem

class SuperListAdapter(
    var superList: ArrayList<SuperListItem>,
    var deleteItem: (String) -> Unit,
    var moveNext: (String, String) -> Unit
) :
    RecyclerView.Adapter<SuperListAdapter.SuperItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuperItemViewHolder {
        return SuperItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.super_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SuperItemViewHolder, position: Int) {
        val item = superList[position]
        holder.tvTitle.text = item.heading
        holder.progressBar.progress =
            if (item.detailItem.isNotEmpty()) {
                (item.detailItem.filter { it.isChecked }.size * 100) / item.detailItem.size
            } else 100
        holder.ivDelete.setOnClickListener {
            deleteItem(item.id)
        }
        holder.rlMain.setOnClickListener {
            moveNext(item.id, item.heading)
        }
    }

    override fun getItemCount(): Int {
        return superList.size
    }

    inner class SuperItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var rlMain: RelativeLayout = view.findViewById(R.id.rlMain)
        var ivDelete: ImageView = view.findViewById(R.id.ivDelete)
        var tvTitle: TextView = view.findViewById(R.id.tvTitle)
        var progressBar: ProgressBar = view.findViewById(R.id.progressbar)
    }
}