package com.example.ruok_workers

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.ruok_workers.databinding.BriefingItemBinding
import java.util.Vector

class BriefingBeforeAdapter (private val context: Context, private val items:Vector<BriefingBeforeCard>):RecyclerView.Adapter<BriefingBeforeAdapter.ViewHolder>(){
    lateinit var BriefingDetailFragment:BriefingDetailFragment
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = BriefingItemBinding.inflate(layoutInflater,parent,false)
        return ViewHolder(binding)
    }
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.tvBriefingNum.text = item.beforeNum.toString()
        holder.binding.tvBriefingTitle.text = item.beforeTitle
        holder.binding.tvBriefingTime.text = "작성시간 : " + item.beforeTime
        //listView선택시 BriefingDetailFragment로 이동
        holder.binding.cvBriefingItem.setOnClickListener {
            BriefingDetailFragment = BriefingDetailFragment()

            var bundle = Bundle()
            bundle.putInt("b_num",item.beforeNum)
            bundle.putInt("m_num", item.loginNum.toInt())

            BriefingDetailFragment.arguments = bundle
            (context as AppCompatActivity).supportFragmentManager.beginTransaction().replace(R.id.rootLayout, BriefingDetailFragment).commit()
        }

    }
    inner class ViewHolder(var binding: BriefingItemBinding): RecyclerView.ViewHolder(binding.root)
}