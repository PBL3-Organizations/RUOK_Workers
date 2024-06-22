package com.example.ruok_workers

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.ruok_workers.databinding.CountingTableItemsBinding
import java.util.Vector

class CountingTableAdapter(private val context: Context, private val items:Vector<CountingTableItem>) : RecyclerView.Adapter<CountingTableAdapter.ViewHolder>() {
    lateinit var countingTableFragment: CountingTableFragment

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountingTableAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CountingTableItemsBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val item = items[position]

        holder.binding.tvPlaceCountingTable.text = item.place
        holder.binding.tvSumCountingTable.text = "인원: " + item.sum.toString() + "명"
        holder.binding.etCountingTableWomen.setText(item.women.toString())
        holder.binding.etCountingTableMen.setText(item.men.toString())
    }

    inner class ViewHolder(var binding: CountingTableItemsBinding) : RecyclerView.ViewHolder(binding.root)
}