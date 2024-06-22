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
        holder.tvfeName.text = "이름: " + itemList[position].name
        holder.tvfeBirth.text = "생년월일: " + itemList[position].birth.substring(0,4) + "." + itemList[position].birth.substring(4,6) + "." + itemList[position].birth.substring(6) + "."


//        // 카드뷰를 클릭하면 ProfileDetailFragment로 이동
//        holder.binding.root.setOnClickListener {
//
//            var bundle = Bundle().apply {
//                putString("name", itemList[position].name)
//                putString("birth", itemList[position].birth)
//            }
//        }
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

}