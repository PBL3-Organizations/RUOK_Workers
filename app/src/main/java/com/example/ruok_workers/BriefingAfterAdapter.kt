package com.example.ruok_workers

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.ruok_workers.databinding.BriefingAfterItemBinding
import java.util.Vector

class BriefingAfterAdapter (private val context: Context, private val items: Vector<BriefingAfterCard>):
    RecyclerView.Adapter<BriefingAfterAdapter.ViewHolder>(){
    lateinit var BriefingDetailFragment:BriefingDetailFragment
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = BriefingAfterItemBinding.inflate(layoutInflater,parent,false)
        return ViewHolder(binding)
    }
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.tvBriefingAfterNum.text = item.AfterNum.toString()
        holder.binding.tvBriefingAfterTitle.text = item.AfterTitle
        holder.binding.tvBriefingAfterTime.text = "작성시간 : " + item.AfterTime
        //listView선택시 BriefingDetailFragment로 이동
        holder.binding.cvBriefingAfterItem.setOnClickListener {
            BriefingDetailFragment = BriefingDetailFragment()

            var bundle = Bundle()
            bundle.putInt("b_num",item.AfterNum)
            bundle.putInt("tabPosition", 0)

            BriefingDetailFragment.arguments = bundle
            (context as AppCompatActivity).supportFragmentManager.beginTransaction().replace(R.id.rootLayout, BriefingDetailFragment).commit()
        }

    }
    inner class ViewHolder(var binding: BriefingAfterItemBinding): RecyclerView.ViewHolder(binding.root)
}