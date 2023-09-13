package com.leo.practiceproject.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leo.practiceproject.databinding.RecylerviewHolderBinding

class NewsAdapter(var newsList:List<String>) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
    private var i : Int = 1

    var ii: Int
        get() {
            return i
        }
        set(value) {
            i = value
        }


    inner class ViewHolder(var viewHolderBinding : RecylerviewHolderBinding) : RecyclerView.ViewHolder(viewHolderBinding.root) {
        fun bind(text: String) {
            viewHolderBinding.holderButton.text = text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = RecylerviewHolderBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(itemBinding);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(newsList.get(position))
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}