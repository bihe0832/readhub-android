package com.bihe0832.readhub.test.base

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


import com.bihe0832.readhub.R

import java.util.ArrayList


class TestPagerAdapter(context: Context) : RecyclerView.Adapter<TestPagerAdapter.MyViewHolder>() {
    private val items = arrayListOf<TestItem>()
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    fun setData(items: List<TestItem>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = inflater.inflate(R.layout.com_bihe0832_test_item_pager, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items[position]
        holder.tv_title.text = item.title
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            items[position].onItemClick()
        }
    }

    override fun getItemCount(): Int = items.size

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_title: TextView = itemView.findViewById<View>(R.id.test_title) as TextView
    }
}
