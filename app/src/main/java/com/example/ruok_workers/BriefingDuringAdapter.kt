package com.example.ruok_workers

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.ruok_workers.databinding.BriefingDuringItemBinding

import java.util.Vector

class BriefingDuringAdapter (private val context: Context, private val items: Vector<BriefingDuringCard>):
    RecyclerView.Adapter<BriefingDuringAdapter.ViewHolder>() {
    lateinit var BriefingDetailFragment:BriefingDetailFragment
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = BriefingDuringItemBinding.inflate(layoutInflater,parent,false)
        return ViewHolder(binding)
    }
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.tvBriefingDuringNum.text = item.DuringNum.toString()
        holder.binding.tvBriefingDuringTitle.text= item.DuringTitle
        holder.binding.tvBriefingDuringTime.text = "작성시간 : " + item.DuringTime
        //listView선택시 BriefingDetailFragment로 이동
        holder.binding.cvBriefingDuringItem.setOnClickListener {
            BriefingDetailFragment = BriefingDetailFragment()

            var bundle = Bundle()
            bundle.putInt("b_num",item.DuringNum)
            bundle.putInt("tabPosition", 2)

            BriefingDetailFragment.arguments = bundle
            (context as AppCompatActivity).supportFragmentManager.beginTransaction().replace(R.id.rootLayout, BriefingDetailFragment).commit()
        }

    }
    inner class ViewHolder(var binding: BriefingDuringItemBinding): RecyclerView.ViewHolder(binding.root)
}