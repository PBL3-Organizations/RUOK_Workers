package com.example.ruok_workers

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ruok_workers.databinding.UnknownItemsBinding
import java.util.Vector

class UnknownAdapter(private val context: Context, private val items: Vector<UnknownCard>) : RecyclerView.Adapter<UnknownAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = UnknownItemsBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.tvMeetLog.text = "만난 날짜 : " + item.meeetLog
        holder.binding.tvMeetPlace.text = "만난 장소 : " + item.meetPlace
        holder.binding.root.setOnClickListener {
            val place = item.meetPlace
            val image = R.drawable.p1
            val time = item.meeetLog
            var bundle = Bundle()
            val dialog = UnknownDialog(place = place, time = time)
            dialog.isCancelable = false
            dialog.show((context as AppCompatActivity).supportFragmentManager, "UnknownDialog")

        }
    }

    inner class ViewHolder(var binding: UnknownItemsBinding) : RecyclerView.ViewHolder(binding.root)
}