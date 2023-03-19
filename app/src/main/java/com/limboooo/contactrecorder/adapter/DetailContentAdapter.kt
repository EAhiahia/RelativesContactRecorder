package com.limboooo.contactrecorder.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.limboooo.contactrecorder.R
import com.limboooo.contactrecorder.databinding.ItemContactDetailCardBinding
import com.limboooo.contactrecorder.repository.ProjectViewModel

const val DETAIL_RECEIVED = 0
const val DETAIL_GAVE = 1

class DetailContentAdapter(private val viewModel: ProjectViewModel, private val type: Int) :
    Adapter<DetailContentAdapter.DetailViewHolder>() {

    inner class DetailViewHolder(val binding: ItemContactDetailCardBinding) :
        ViewHolder(binding.root) {

        fun bind(position: Int) {
            when(type){
                DETAIL_GAVE -> {
                    val target = viewModel.targetData.moneyGave[position]
                    binding.thing.text = target.thing
                    binding.money.text = target.money
                    //todo 更改数据库结构，保存已删除部分和其他信息
                }
                DETAIL_RECEIVED -> {
                    val target = viewModel.targetData.moneyReceived[position]

                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact_detail_card, parent, false)
        val binding = ItemContactDetailCardBinding.bind(view)
        return DetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int =
        when (type) {
            DETAIL_GAVE -> viewModel.targetData.moneyGave.size
            DETAIL_RECEIVED -> viewModel.targetData.moneyReceived.size
            else -> throw Exception("不可能走到这里")
        }
}