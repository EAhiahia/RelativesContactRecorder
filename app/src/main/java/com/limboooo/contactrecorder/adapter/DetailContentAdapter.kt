package com.limboooo.contactrecorder.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.limboooo.contactrecorder.R
import com.limboooo.contactrecorder.databinding.ItemContactDetailCardBinding
import com.limboooo.contactrecorder.repository.ProjectViewModel
import com.limboooo.contactrecorder.repository.room.entity.whole.MoneyGave
import com.limboooo.contactrecorder.repository.room.entity.whole.MoneyGaveBack
import com.limboooo.contactrecorder.repository.room.entity.whole.MoneyReceived
import com.limboooo.contactrecorder.repository.room.entity.whole.MoneyReceivedBack

const val DETAIL_RECEIVED = 0
const val DETAIL_RECEIVED_BACK = 3
const val DETAIL_GAVE = 4
const val DETAIL_GAVE_BACK = 1

class DetailContentAdapter(
    private val viewModel: ProjectViewModel,
    private val type: Int,
    var anotherAdapter: DetailContentAdapter? = null
) :
    Adapter<DetailContentAdapter.DetailViewHolder>() {

    inner class DetailViewHolder(val binding: ItemContactDetailCardBinding) :
        ViewHolder(binding.root) {

        fun bind(position: Int) {
            when (type) {
                DETAIL_GAVE -> {
                    val target = viewModel.targetData.moneyGave[position]
                    binding.thing.text = target.thing
                    binding.money.text = target.money
                    binding.delete.setOnClickListener {
                        viewModel.targetData.moneyGave.remove(target)
                        notifyItemRemoved(position)
                    }
                    //todo 需要更该MoneyGave的数据结构，并将勾选的移动到当中
                    binding.state.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            viewModel.targetData.moneyGave.remove(target)
                            notifyItemRemoved(position)
                            val new = MoneyGaveBack(
                                0,
                                target.ownerId,
                                target.time,
                                target.thing,
                                target.money
                            )
                            viewModel.targetData.moneyGaveBack.add(new)
                            anotherAdapter!!.notifyItemInserted(viewModel.targetData.moneyGaveBack.size - 1)
                        }
                    }
                }
                DETAIL_GAVE_BACK -> {
                    val target = viewModel.targetData.moneyGaveBack[position]
                    binding.thing.text = target.thing
                    binding.money.text = target.money
                    binding.delete.setOnClickListener {
                        viewModel.targetData.moneyGaveBack.remove(target)
                        notifyItemRemoved(position)
                    }
                    //todo 需要更该MoneyGave的数据结构，并将勾选的移动到当中
                    binding.state.setOnCheckedChangeListener { _, isChecked ->
                        if (!isChecked) {
                            viewModel.targetData.moneyGaveBack.remove(target)
                            notifyItemRemoved(position)
                            val new = MoneyGave(
                                0,
                                target.ownerId,
                                target.time,
                                target.thing,
                                target.money
                            )
                            viewModel.targetData.moneyGave.add(new)
                            anotherAdapter!!.notifyItemInserted(viewModel.targetData.moneyGave.size - 1)
                        }
                    }
                }
                DETAIL_RECEIVED -> {
                    val target = viewModel.targetData.moneyReceived[position]
                    binding.thing.text = target.thing
                    binding.money.text = target.money
                    binding.delete.setOnClickListener {
                        viewModel.targetData.moneyReceived.remove(target)
                        notifyItemRemoved(position)
                    }
                    //todo 需要更该MoneyGave的数据结构，并将勾选的移动到当中
                    binding.state.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            viewModel.targetData.moneyReceived.remove(target)
                            notifyItemRemoved(position)
                            val new = MoneyReceivedBack(
                                0,
                                target.ownerId,
                                target.time,
                                target.thing,
                                target.money
                            )
                            viewModel.targetData.moneyReceivedBack.add(new)
                            anotherAdapter!!.notifyItemInserted(viewModel.targetData.moneyReceivedBack.size - 1)
                        }
                    }
                }
                DETAIL_RECEIVED_BACK -> {
                    val target = viewModel.targetData.moneyReceivedBack[position]
                    binding.thing.text = target.thing
                    binding.money.text = target.money
                    binding.delete.setOnClickListener {
                        viewModel.targetData.moneyReceivedBack.remove(target)
                        notifyItemRemoved(position)
                    }
                    //todo 需要更该MoneyGave的数据结构，并将勾选的移动到当中
                    binding.state.setOnCheckedChangeListener { _, isChecked ->
                        if (!isChecked) {
                            viewModel.targetData.moneyReceivedBack.remove(target)
                            notifyItemRemoved(position)
                            val new = MoneyReceived(
                                0,
                                target.ownerId,
                                target.time,
                                target.thing,
                                target.money
                            )
                            viewModel.targetData.moneyReceived.add(new)
                            anotherAdapter!!.notifyItemInserted(viewModel.targetData.moneyReceived.size - 1)
                        }
                    }
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
            DETAIL_GAVE_BACK -> viewModel.targetData.moneyGaveBack.size
            DETAIL_RECEIVED -> viewModel.targetData.moneyReceived.size
            DETAIL_RECEIVED_BACK -> viewModel.targetData.moneyReceivedBack.size
            else -> throw Exception("不可能走到这里")
        }
}