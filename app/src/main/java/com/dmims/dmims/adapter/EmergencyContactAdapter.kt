package com.dmims.dmims.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.dmims.dmims.dataclass.EmUser
import com.dmims.dmims.R

class EmergencyContactAdapter(val userlist: ArrayList<EmUser>) :
    RecyclerView.Adapter<EmergencyContactAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.list_layout, p0, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userlist.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val emUser: EmUser = userlist[p1]
        p0.textPurpose?.text = emUser.purpose
        p0.textName?.text = emUser.name
        p0.textContact?.text = emUser.contact
        p0.imageViews?.setImageResource(emUser.image)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textContact = itemView.findViewById<TextView>(R.id.txtcontact)
        val textName = itemView.findViewById<TextView>(R.id.txtname)
        val textPurpose = itemView.findViewById<TextView>(R.id.txtpurpose)
        val imageViews = itemView.findViewById<ImageView>(R.id.camera_image)
    }

}