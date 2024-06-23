package com.example.ruok_workers

import ProfileDetailFragment
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.ruok_workers.databinding.FaviconEditItemsBinding

class FaviconAdapter(private val context: Context, val itemList: ArrayList<FaviconItem>) : RecyclerView.Adapter<FaviconAdapter.FaviconViewHolder>(){
    lateinit var profileDetailFragment: ProfileDetailFragment

    inner class FaviconViewHolder(var binding: FaviconEditItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvfeName = itemView.findViewById<TextView>(R.id.tvfeName)
        val tvfeBirth = itemView.findViewById<TextView>(R.id.tvfeBirth)
        val ibtnStar = itemView.findViewById<ImageButton>(R.id.ibtnStar)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FaviconAdapter.FaviconViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FaviconEditItemsBinding.inflate(layoutInflater, parent, false)
        return FaviconViewHolder(binding)
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

        // 카드뷰를 클릭하면 ProfileDetailFragment로 이동
        holder.binding.root.setOnClickListener {
            val name = itemList[position].name
            val birth = itemList[position].birth

            // ProfileDetailFragment의 인스턴스 생성
            val profileDetailFragment = ProfileDetailFragment()

            // ProfileDetailFragment로 전달할 데이터 번들 생성
            val bundle = Bundle().apply {
                putString("name", name)
                putString("birth", birth)
            }
            profileDetailFragment.arguments = bundle

            // ProfileDetailFragment로 이동
            (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.rootLayout, profileDetailFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

}
