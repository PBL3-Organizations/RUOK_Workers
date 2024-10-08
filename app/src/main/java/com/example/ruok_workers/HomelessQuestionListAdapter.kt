package com.example.ruok_workers

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.ruok_workers.databinding.ListItemsBinding
import java.util.Vector

class HomelessQuestionListAdapter(private val context: Context, private val items: Vector<HomelessQuestionListCard>) : RecyclerView.Adapter<com.example.ruok_workers.HomelessQuestionListAdapter.ViewHolder>(){
    lateinit var DetailsFragment:DetailsFragment

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemsBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.tvConsultationNum.text = item.consultationNum.toString()
        holder.binding.tvHomelessName.text = "이름 : " + item.homelessName
        holder.binding.tvHomelessUnusual.text = "특이사항: " + item.homelessUnusual
        holder.binding.tvHomelessLog.text = "작성일 : " + item.homelessLog.substring(0,4)+"년 "+item.homelessLog.substring(5,7)+"월 "+item.homelessLog.substring(8,10)+"일"
        holder.binding.tvHomelessPlace.text = "만난 장소 : " + item.homelessPlace
        holder.binding.root.setOnClickListener {
            DetailsFragment = DetailsFragment()

            var bundle = Bundle()
            bundle.putInt("c_num", item.consultationNum)
            bundle.putInt("h_num", item.homelessNum)

            DetailsFragment.arguments = bundle
            (context as AppCompatActivity).supportFragmentManager.beginTransaction().replace(R.id.rootLayout, DetailsFragment).commit()
        }
    }

    inner class ViewHolder(var binding: ListItemsBinding) : RecyclerView.ViewHolder(binding.root)
}