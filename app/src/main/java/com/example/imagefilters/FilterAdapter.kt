package com.example.imagefilters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView

class FilterAdapter(val filters: ArrayList<Filter>, val context:Context,val callbak:ViewHolder.FiltersCallback):RecyclerView.Adapter<ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_filter,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ivItem?.setImageBitmap(filters[position].bitmap)
        holder.txItem?.text = (filters[position].name)
        holder.ivItem?.setOnClickListener { callbak.itemSelected(position) }

    }

    override fun getItemCount(): Int {
        return filters.size
    }
}
class ViewHolder(itemview:View): RecyclerView.ViewHolder(itemview) {
    var ivItem: ImageView? = null
    var txItem: TextView? = null
    var clItem:ConstraintSet.Layout? = null

    init {
        ivItem = itemView.findViewById(R.id.ivItem)
        txItem = itemView.findViewById(R.id.tvItem)
//        clItem = itemview.findViewById(R.id.clItem)

    }
    interface FiltersCallback{
        fun itemSelected(index:Int)

    }

}