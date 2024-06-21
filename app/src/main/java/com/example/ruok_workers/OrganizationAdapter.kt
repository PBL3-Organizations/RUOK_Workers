package com.example.ruok_workers

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrganizationAdapter(
    val context: Context,
    val itemList: ArrayList<OrganizationItem>,
    val dialog: Dialog,
    val orgName: EditText,
    val orgNum: TextView
) : RecyclerView.Adapter<OrganizationAdapter.OrganizationViewHolder>() {
    inner class OrganizationViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var wfNum = view.findViewById<TextView>(R.id.tvOrgNum)
        var wfName = view.findViewById<TextView>(R.id.tvOrg)
        var wfAddr = view.findViewById<TextView>(R.id.tvOrgAddr)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrganizationViewHolder {
        val cardView = LayoutInflater.from(parent.context).inflate(R.layout.organization_items, parent, false)
        return OrganizationViewHolder(cardView)
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

    override fun onBindViewHolder(holder: OrganizationViewHolder, position: Int) {
        holder.wfNum.setText(itemList[position].wfNum.toString())
        holder.wfName.setText(itemList[position].wfName)
        holder.wfAddr.setText(itemList[position].wfAddr)

        holder.wfName.setOnClickListener {
            orgName.setText(holder.wfName.text)
            orgNum.setText(holder.wfNum.text)
            dialog.dismiss()
        }
    }
}