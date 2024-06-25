package com.example.ruok_workers

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.ruok_workers.LocationTrackingFragment.Companion.TAG
import com.example.ruok_workers.databinding.CountingDetailItemsBinding
import java.util.Vector

class CountingDetailAdapter (private val context: Context, val items: Vector<CountingDetailItem>) : RecyclerView.Adapter<CountingDetailAdapter.ViewHolder>() {
    lateinit var countingDetailFragment: CountingDetailFragment

    val TAG = "CountingDetailFragment"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountingDetailAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CountingDetailItemsBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount: ${items.size}")
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val item = items[position]

        Log.d(TAG, "onBindViewHolder: Position $position, Item $item")

        holder.binding.tvPlaceCountingDetailItems.text = item.place
        holder.binding.tvWorkerCountingDetailItems.text = "담당: " + item.worker
        item.sum = item.women + item.men
        holder.binding.tvTotalCountingDetailItems.text = item.sum.toString() + "명"
        holder.binding.tvSumCountingDetailItems.text = "남 " + item.men.toString() + " | 여 " + item.women.toString()

    }

    inner class ViewHolder(var binding: CountingDetailItemsBinding) : RecyclerView.ViewHolder(binding.root)
}