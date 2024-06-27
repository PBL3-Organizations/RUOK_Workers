package com.example.ruok_workers

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.ruok_workers.databinding.UnknownItemsBinding
import java.util.Vector

class UnknownAdapter(private val context: Context, private val items: Vector<UnknownCard>) : RecyclerView.Adapter<UnknownAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = UnknownItemsBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.tvMeetLog.text = "만난 날짜 : " + item.meeetLog
        holder.binding.tvMeetPlace.text = "만난 장소 : " + item.meetPlace

        if (item.meetPhotoResId != null) {
            holder.binding.tvMeetPhoto.setImageResource(item.meetPhotoResId!!)
        } else if (item.meetPhotoBitmap != null) {
            holder.binding.tvMeetPhoto.setImageBitmap(item.meetPhotoBitmap)
        }

        // 카드뷰 클릭시 이미지 다이얼로그 보이기
        holder.binding.root.setOnClickListener {
            val place = item.meetPlace
            val time = item.meeetLog

            val dialog = if (item.meetPhotoResId != null) {
                UnknownDialog(item.meetPhotoResId, null, place, time)
            } else if (item.meetPhotoBitmap != null) {
                UnknownDialog(null, item.meetPhotoBitmap, place, time)
            } else {
                UnknownDialog(null, null, place, time)
            }

            dialog.isCancelable = false
            dialog.show((context as AppCompatActivity).supportFragmentManager, "UnknownDialog")
        }
    }

//        try {
//            holder.binding.tvMeetPhoto.setImageResource(item.meetPhotoResId!!)
//        } catch (e:Exception){
//            holder.binding.tvMeetPhoto.setImageBitmap(item.meetPhotoBitmap)
//        }
//
//        //카드뷰 클릭시 이미지 다이얼로그 보이기
//        holder.binding.root.setOnClickListener {
//            val place = item.meetPlace
//            val time = item.meeetLog
//            val image = item.meetPhotoResId
//            val image2 = item.meetPhotoBitmap
//            val dialog = UnknownDialog(image= image!!, image2=image2!!, place = place, time = time)
//            dialog.isCancelable = false
//            dialog.show((context as AppCompatActivity).supportFragmentManager, "UnknownDialog")
//
//        }

//        item.meetPhotoResId?.let {
//            holder.binding.tvMeetPhoto.setImageResource(it)
//        } ?: run {
//            holder.binding.tvMeetPhoto.setImageBitmap(item.meetPhotoBitmap)
//        }

//        //카드뷰 클릭시 이미지 다이얼로그 보이기
//        holder.binding.root.setOnClickListener {
//            val dialog = UnknownDialog(image = item.meetPhotoResId?.toString() ?: "", place = item.meetPlace, time = item.meeetLog)
//            dialog.isCancelable = false
//            dialog.show((context as AppCompatActivity).supportFragmentManager, "UnknownDialog")
//        }


    inner class ViewHolder(var binding: UnknownItemsBinding) : RecyclerView.ViewHolder(binding.root)

    }

//    private fun UnknownDialog(image: String, place: String, time: String): UnknownDialog {
//        TODO()
//    }


