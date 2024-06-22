package com.example.ruok_workers

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.ruok_workers.databinding.CountingDetailItemsBinding
import com.example.ruok_workers.databinding.CountingTableItemsBinding
import java.util.Vector

class CountingDetailAdapter (private val context: Context, private val items: Vector<CountingDetailItem>) : RecyclerView.Adapter<CountingDetailAdapter.ViewHolder>() {
    lateinit var countingDetailFragment: CountingDetailFragment

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountingDetailAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CountingDetailItemsBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val item = items[position]

        holder.binding.tvPlaceCountingDetailItems.text = item.place
        holder.binding.tvWorkerCountingDetailItems.text = "담당: " + item.worker
        holder.binding.tvTotalCountingDetailItems.text = item.sum.toString() + "명"
        holder.binding.tvSumCountingDetailItems.text = "남 " + item.men.toString() + " | 여 " + item.women.toString()

    }

    inner class ViewHolder(var binding: CountingDetailItemsBinding) : RecyclerView.ViewHolder(binding.root)
}