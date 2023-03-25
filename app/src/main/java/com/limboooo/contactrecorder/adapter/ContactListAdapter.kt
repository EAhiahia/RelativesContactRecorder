package com.limboooo.contactrecorder.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.fragivity.navigator
import com.github.fragivity.push
import com.google.android.material.snackbar.Snackbar
import com.limboooo.contactrecorder.R
import com.limboooo.contactrecorder.databinding.ItemContactListBinding
import com.limboooo.contactrecorder.fragment.FragmentContactDetail
import com.limboooo.contactrecorder.repository.ProjectViewModel
import com.limboooo.contactrecorder.repository.initAnimator
import com.limboooo.contactrecorder.repository.room.entity.whole.RelativesBaseInfo

class ContactListAdapter(private val viewModel: ProjectViewModel) :
    ListAdapter<RelativesBaseInfo, ContactListAdapter.ListViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<RelativesBaseInfo>() {
            override fun areItemsTheSame(
                oldItem: RelativesBaseInfo,
                newItem: RelativesBaseInfo
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: RelativesBaseInfo,
                newItem: RelativesBaseInfo
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class ListViewHolder(val binding: ItemContactListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val entity = getItem(position)
            binding.item.setOnClickListener {
                it.navigator.push(FragmentContactDetail::class) {
                    initAnimator()
                    arguments = bundleOf("entity_id" to entity.id)
                }
            }
            binding.item.setOnLongClickListener {
                viewModel.deleteMode.value = !viewModel.deleteMode.value!!
                true
            }
            binding.apply {
                name.text = entity.name
                received.text = "收到${entity.moneyReceivedWhole}元"
                gave.text = "给出${entity.moneyGaveWhole}元"
                deleteButton.setOnClickListener {
                    //用于撤销所有修改
                    if (viewModel.mainListDataBackup.isEmpty()) viewModel.mainListDataBackup =
                        viewModel.mainListData.value.toMutableList()
                    viewModel.mainListData.value =
                        viewModel.mainListData.value.toMutableList().apply { remove(entity) }
                    viewModel.deletedList.value =
                        viewModel.deletedList.value!!.toMutableList().apply { add(entity) }
                    Snackbar.make(itemView.parent as View, "已删除", Snackbar.LENGTH_SHORT)
                        .setAction("撤销") {
                            viewModel.deletedList.value =
                                viewModel.deletedList.value!!.toMutableList().apply { remove(entity) }
                            viewModel.mainListData.value =
                                viewModel.mainListData.value.toMutableList()
                                    .apply { add(0, entity) }
                        }
                        .show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_contact_list, parent, false)
        val binding = ItemContactListBinding.bind(view)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun onViewAttachedToWindow(holder: ListViewHolder) {
        if (viewModel.deleteMode.value!!) {
            holder.binding.deleteButton.visibility = View.VISIBLE
        } else {
            holder.binding.deleteButton.visibility = View.GONE
        }
        super.onViewAttachedToWindow(holder)
    }

}