package com.example.ruok_workers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FaviconAdapter(val itemList: ArrayList<FaviconItem>) : RecyclerView.Adapter<FaviconAdapter.FaviconViewHolder>(){
    inner class FaviconViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvfeName = itemView.findViewById<TextView>(R.id.tvfeName)
        val tvfeBirth = itemView.findViewById<TextView>(R.id.tvfeBirth)
        val ibtnStar = itemView.findViewById<ImageButton>(R.id.ibtnStar)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FaviconAdapter.FaviconViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favicon_edit_items, parent, false)
        return FaviconViewHolder(view)
    }

    override fun onBindViewHolder(holder: FaviconAdapter.FaviconViewHolder, position: Int) {
        holder.tvfeName.text = "이름: " + itemList[position].name
        holder.tvfeBirth.text = "생년월일: " + itemList[position].birth.substring(0,4) + "." + itemList[position].birth.substring(4,6) + "." + itemList[position].birth.substring(6) + "."

        //ibtnStar 토글을 위한 태그 기본값 설정
        holder.ibtnStar.tag = "off"

        // ibtnStar 클릭 리스너 설정
        holder.ibtnStar.setOnClickListener{
            // 현재 이미지 리소스를 가져옵니다
            val currentTag = it.tag as? String
            if (currentTag == "on") {
                // 이미지가 켜져 있으면, 끄는 이미지로 변경
                holder.ibtnStar.setImageResource(android.R.drawable.btn_star_big_off)
                holder.ibtnStar.tag = "off"
            } else {
                // 이미지가 꺼져 있으면, 켜는 이미지로 변경
                holder.ibtnStar.setImageResource(android.R.drawable.btn_star_big_on)
                holder.ibtnStar.tag = "on"
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

}