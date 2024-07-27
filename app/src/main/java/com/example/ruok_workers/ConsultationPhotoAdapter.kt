package com.example.ruok_workers

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.ruok_workers.databinding.ConsultationPhotoItemsBinding
import com.example.ruok_workers.databinding.ListItemsBinding
import java.io.File
import java.util.Vector

class ConsultationPhotoAdapter(private val context: Context, private val items: Vector<String>): RecyclerView.Adapter<ConsultationPhotoAdapter.ViewHolder>() {
    inner class ViewHolder(var binding: ConsultationPhotoItemsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ConsultationPhotoItemsBinding.inflate(layoutInflater, parent, false)
        return ViewHolder((binding))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        if (item.contains(".")) { //사진이 drawable에 저장된 경우
            var resId = (context as AppCompatActivity).resources.getIdentifier(item.substringBefore('.'), "drawable", (context as AppCompatActivity).packageName)
            holder.binding.ivConsultationPhoto.setImageResource(resId)
        } else { //사진이 내부저장소에 저장된 경우
            val filePath = (context as AppCompatActivity).filesDir.absolutePath + "/" + item

            val imgFile = File(filePath)
            if(imgFile.exists()) {
                val bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                holder.binding.ivConsultationPhoto.setImageBitmap(bitmap)
            }
        }
    }
}