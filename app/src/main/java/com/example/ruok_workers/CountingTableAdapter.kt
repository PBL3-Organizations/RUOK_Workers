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

        holder.binding.root.setOnClickListener{
            countingTableFragment = CountingTableFragment()

            var bundle = Bundle()

            countingTableFragment.arguments = bundle
            (context as AppCompatActivity).supportFragmentManager.beginTransaction().replace(R.id.rootLayout, countingTableFragment).commit()
        }

    }

    inner class ViewHolder(var binding: CountingTableItemsBinding) : RecyclerView.ViewHolder(binding.root)
}