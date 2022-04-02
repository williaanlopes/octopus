package com.gurpster.octopus.samples

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gurpster.octopus.extensions.toBinding

class SampleAdapter : RecyclerView.Adapter<SampleAdapter.InnerViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
        return InnerViewHolder(parent.toBinding())
    }

    override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    inner class InnerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind() {

        }
    }
}


