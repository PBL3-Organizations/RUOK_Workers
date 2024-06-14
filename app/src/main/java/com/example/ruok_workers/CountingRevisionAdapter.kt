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
        val item = items[position]

        holder.binding.root.setOnClickListener{
            countingRevisionFragment = CountingRevisionFragment()

            var bundle = Bundle()

            countingRevisionFragment.arguments = bundle
            (context as AppCompatActivity).supportFragmentManager.beginTransaction().replace(R.id.rootLayout, countingRevisionFragment).commit()
        }

    }

    inner class ViewHolder(var binding: CountingRevisionItemsBinding) : RecyclerView.ViewHolder(binding.root)
}