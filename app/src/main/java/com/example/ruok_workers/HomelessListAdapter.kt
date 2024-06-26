package com.example.ruok_workers

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.ruok_workers.databinding.FaviconEditItemsBinding

class HomelessListAdapter (private val context: Context, val itemList: ArrayList<FaviconItem>) : RecyclerView.Adapter<HomelessListAdapter.FaviconViewHolder>(){
    lateinit var homelessListFragment: HomelessListFragment
    var h_num = 0;

    inner class FaviconViewHolder(var binding: FaviconEditItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvfeName = itemView.findViewById<TextView>(R.id.tvfeName)
        val tvfeBirth = itemView.findViewById<TextView>(R.id.tvfeBirth)
        val ibtnStar = itemView.findViewById<ImageButton>(R.id.ibtnStar)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomelessListAdapter.FaviconViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FaviconEditItemsBinding.inflate(layoutInflater, parent, false)
        return FaviconViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomelessListAdapter.FaviconViewHolder, position: Int) {

        val item = itemList[position]

        holder.binding.ivProfileEdit.setImageResource(itemList[position].photo)
        holder.binding.tvfeName.text = "이름: " + itemList[position].name
        holder.binding.tvfeBirth.text = "생년월일: " + itemList[position].birth.substring(0,4) + "." + itemList[position].birth.substring(4,6) + "." + itemList[position].birth.substring(6) + "."

        // 즐겨찾기 상태에 따라 별 아이콘 설정
        if (item.bookmark == 1) {
            holder.binding.ibtnStar.setImageResource(android.R.drawable.btn_star_big_on)
            holder.binding.ibtnStar.tag = "on"
        } else {
            holder.binding.ibtnStar.setImageResource(android.R.drawable.btn_star_big_off)
            holder.binding.ibtnStar.tag = "off"
        }

        // ibtnStar 클릭 리스너 설정
        holder.binding.ibtnStar.setOnClickListener{

            // 현재 이미지 리소스를 가져옵니다
            val currentTag = it.tag as? String
            if (currentTag == "on") {
                // 이미지가 켜져 있으면, 끄는 이미지로 변경
                holder.binding.ibtnStar.setImageResource(android.R.drawable.btn_star_big_off)
                holder.binding.ibtnStar.tag = "off"

            } else {
                // 이미지가 꺼져 있으면, 켜는 이미지로 변경
                holder.binding.ibtnStar.setImageResource(android.R.drawable.btn_star_big_on)
                holder.binding.ibtnStar.tag = "on"

            }
        }

        holder.binding.cvFaviconEdit.setOnClickListener {
            h_num = item.num
        }
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

}