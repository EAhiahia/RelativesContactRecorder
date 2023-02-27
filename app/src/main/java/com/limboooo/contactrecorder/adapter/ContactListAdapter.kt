package com.limboooo.contactrecorder.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.limboooo.contactrecorder.databinding.ItemContactListBinding
import com.limboooo.contactrecorder.repository.room.entity.Emails
import com.limboooo.contactrecorder.repository.room.entity.RelativesInfoWhole

class ContactListAdapter(private val onItemClicked: (RelativesInfoWhole) -> Unit) :
    ListAdapter<RelativesInfoWhole, ContactListAdapter.ListViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<RelativesInfoWhole>() {
            override fun areItemsTheSame(
                oldItem: RelativesInfoWhole,
                newItem: RelativesInfoWhole
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: RelativesInfoWhole,
                newItem: RelativesInfoWhole
            ): Boolean {
                return oldItem.baseInfo.uid == newItem.baseInfo.uid
            }
        }
    }

    inner class ListViewHolder(private var binding: ItemContactListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(single: RelativesInfoWhole) {
            binding.apply {
                relativesName.text = single.baseInfo.name
                receivedMoney.text = single.baseInfo.moneyReceivedWhole.toString()
                gaveMoney.text = single.baseInfo.moneyGaveWhole.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val viewHolder = ListViewHolder(
            ItemContactListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        viewHolder.itemView.setOnClickListener {
            onItemClicked(getItem(viewHolder.adapterPosition))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}