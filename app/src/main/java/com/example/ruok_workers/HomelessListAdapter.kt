package com.example.ruok_workers

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ruok_workers.databinding.FaviconEditItemsBinding

class HomelessListAdapter (private val context: Context, val itemList: ArrayList<FaviconItem>, private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<HomelessListAdapter.FaviconViewHolder>(){
    lateinit var homelessListFragment: HomelessListFragment

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    // 인터페이스 정의
    interface OnItemClickListener {
        fun onItemClicked()
    }

    var h_num = 0;

    inner class FaviconViewHolder(var binding: FaviconEditItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvfeName = itemView.findViewById<TextView>(R.id.tvfeName)
        val tvfeBirth = itemView.findViewById<TextView>(R.id.tvfeBirth)
        val ibtnStar = itemView.findViewById<ImageButton>(R.id.ibtnStar)
    }

    private var selectedPosition = RecyclerView.NO_POSITION

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
            sqlitedb.close()
            dbManager.close()
        }

        // 아이템 클릭 리스너 설정
        holder.binding.cvFaviconEdit.setOnClickListener {
            selectedPosition = holder.adapterPosition
            h_num = item.num
            notifyDataSetChanged()

            // 아이템이 클릭되었음을 Fragment에 알림
            itemClickListener.onItemClicked()
        }

        // 선택된 아이템에 이펙트 추가
        if (selectedPosition == position) {
            holder.binding.cvFaviconEdit.setCardBackgroundColor(ContextCompat.getColor(context, R.color.selected_card))
            holder.binding.LinearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.selected_card))
            holder.binding.ibtnStar.setBackgroundColor(ContextCompat.getColor(context, R.color.selected_card))
            holder.binding.cvFaviconEdit.elevation = 20f // 그림자 강하게
        } else {
            holder.binding.cvFaviconEdit.setCardBackgroundColor(ContextCompat.getColor(context, R.color.default_card))
            holder.binding.LinearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.default_card))
            holder.binding.ibtnStar.setBackgroundColor(ContextCompat.getColor(context, R.color.default_card))
            holder.binding.cvFaviconEdit.elevation = 10f // 기본 그림자
        }
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

    // 모든 아이템의 배경색을 default_card로 변경하는 메서드 추가
    fun resetItemBackgrounds() {
        selectedPosition = RecyclerView.NO_POSITION
        notifyDataSetChanged()
    }

}