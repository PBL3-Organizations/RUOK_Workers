package com.example.ruok_workers

import ProfileDetailFragment
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ruok_workers.databinding.FaviconEditItemsBinding

class FaviconAdapter(private val context: Context, val itemList: ArrayList<FaviconItem>) : RecyclerView.Adapter<FaviconAdapter.FaviconViewHolder>(){
    lateinit var profileDetailFragment: ProfileDetailFragment

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

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

        val item = itemList[position]

        holder.binding.tvfeName.text = "이름: " + item.name
        holder.binding.tvfeBirth.text = "생년월일: " + item.birth.substring(0,4) + "." + item.birth.substring(4,6) + "." + item.birth.substring(6) + "."
        holder.binding.ivProfileEdit.setImageResource(item.photo)

        // 즐겨찾기 상태에 따라 별 아이콘 설정
        if (item.bookmark == 1) {
            holder.binding.ibtnStar.setImageResource(android.R.drawable.btn_star_big_on)
            holder.binding.ibtnStar.tag = "on"
        } else {
            holder.binding.ibtnStar.setImageResource(android.R.drawable.btn_star_big_off)
            holder.binding.ibtnStar.tag = "off"
        }

        // ibtnStar 배경색을 기본값으로 설정
        holder.binding.ibtnStar.setBackgroundColor(ContextCompat.getColor(context, R.color.default_card))

        // ibtnStar 클릭 리스너 설정
        holder.binding.ibtnStar.setOnClickListener{
            dbManager = DBManager((context as AppCompatActivity), "RUOKsample", null, 1)
            sqlitedb = dbManager.writableDatabase

            // 현재 이미지 리소스를 가져옵니다
            val currentTag = it.tag as? String
            if (currentTag == "on") {
                // 이미지가 켜져 있으면, 끄는 이미지로 변경
                holder.binding.ibtnStar.setImageResource(android.R.drawable.btn_star_big_off)
                holder.binding.ibtnStar.tag = "off"

                //즐겨찾기 삭제
                val sql = "DELETE FROM bookmark WHERE h_num = ? AND m_num = ?;"
                sqlitedb.execSQL(sql, arrayOf(item.num.toString(), item.m_num.toString()))
            } else {
                // 이미지가 꺼져 있으면, 켜는 이미지로 변경
                holder.binding.ibtnStar.setImageResource(android.R.drawable.btn_star_big_on)
                holder.binding.ibtnStar.tag = "on"

                //즐겨찾기 추가
                val sql = "INSERT INTO bookmark (h_num, m_num) VALUES (?, ?);"
                sqlitedb.execSQL(sql, arrayOf(item.num.toString(), item.m_num.toString()))
            }
        }

        // 카드뷰를 클릭하면 ProfileDetailFragment로 이동
        holder.binding.cvFaviconEdit.setOnClickListener {
            val name = item.name
            val birth = item.birth

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
