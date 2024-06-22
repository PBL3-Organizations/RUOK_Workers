package com.example.ruok_workers

import ProfileDetailFragment
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ruok_workers.databinding.FaviconItemsBinding
import androidx.appcompat.app.AppCompatActivity
import java.util.Vector

class DashboardAdapter(private val context: Context, private val items: Vector<Dashboard>) : RecyclerView.Adapter<DashboardAdapter.ViewHolder>(){
    lateinit var profileDetailFragment: ProfileDetailFragment

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FaviconItemsBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.binding.ivProfile.setImageResource(item.hPhoto)
        holder.binding.tvfName.text = "이름: " + item.hName
        holder.binding.tvfBirth.text = "생년월일: " + item.hBirth.substring(0,4)+"."+item.hBirth.substring(4,6)+"."+item.hBirth.substring(6)

        holder.binding.root.setOnClickListener {
            profileDetailFragment = ProfileDetailFragment()

            var bundle = Bundle()

            profileDetailFragment.arguments = bundle
            (context as AppCompatActivity).supportFragmentManager.beginTransaction().replace(R.id.rootLayout, profileDetailFragment).commit()
        }
    }


    inner class ViewHolder(var binding: FaviconItemsBinding) : RecyclerView.ViewHolder(binding.root)
}