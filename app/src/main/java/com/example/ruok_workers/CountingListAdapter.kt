package com.example.ruok_workers

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.ruok_workers.databinding.CountingListItemsBinding
import com.example.ruok_workers.databinding.CountingTableItemsBinding
import java.util.Vector

class CountingListAdapter (private val context: Context, private val items: Vector<CountingListItem>) : RecyclerView.Adapter<CountingListAdapter.ViewHolder>() {
    lateinit var countingDetailFragment: CountingDetailFragment

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountingListAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CountingListItemsBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val item = items[position]

        holder.binding.tvCountingListItems.text = item.title
        holder.binding.tvCourseCountingListItems.text = item.course
        holder.binding.tvNumCountingListItems.text = "총 인원: " + item.sum.toString() + "명"

        holder.binding.cvCountingListItems.setOnClickListener{
            countingDetailFragment = CountingDetailFragment()

            var bundle = Bundle()
            bundle.putString("CL_TITLE", item.title)
            bundle.putString("CC_NAME", item.course)
            bundle.putInt("CL_SUM", item.sum)

            countingDetailFragment.arguments = bundle
            (context as AppCompatActivity).supportFragmentManager.beginTransaction().replace(R.id.rootLayout, countingDetailFragment).commit()
        }
    }

    inner class ViewHolder(var binding: CountingListItemsBinding) : RecyclerView.ViewHolder(binding.root)
}