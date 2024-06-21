package com.example.ruok_workers

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.ruok_workers.databinding.CountingRevisionItemsBinding
import com.example.ruok_workers.databinding.CountingTableItemsBinding
import java.util.Vector

class CountingRevisionAdapter (private val context: Context, private val items: Vector<CountingRevisionItem>) : RecyclerView.Adapter<CountingRevisionAdapter.ViewHolder>() {
    lateinit var countingRevisionFragment: CountingRevisionFragment

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountingRevisionAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CountingRevisionItemsBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        var item = items[position]

        holder.binding.tvPlaceCountingRevisionItems.text = item.place
        holder.binding.tvWorkerCountingRevisionItems.text = "담당: " + item.worker
        holder.binding.etMenCountingRevisionItems.setText(item.men.toString())
        holder.binding.etWomenCountingRevisionItems.setText(item.women.toString())

    }

    inner class ViewHolder(var binding: CountingRevisionItemsBinding) : RecyclerView.ViewHolder(binding.root)
}