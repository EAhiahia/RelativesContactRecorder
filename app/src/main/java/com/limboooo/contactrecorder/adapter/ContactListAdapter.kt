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
                return oldItem.uid == newItem.uid
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
                it.navigator.push(FragmentContactDetail::class){
                    initAnimator()
                    arguments = bundleOf("position" to position)
                }
            }
            binding.apply {
                name.text = entity.name
                received.text = entity.moneyReceivedWhole.toString()
                gave.text = entity.moneyGaveWhole.toString()
                item.setOnClickListener {
                    it.navigator.push(FragmentContactDetail::class)
                }
                deleteButton.setOnClickListener {
                    viewModel.mainListDataBackup.remove(entity)
                    viewModel.deletedList.add(entity)
                    submitList(viewModel.mainListDataBackup.toList())
                    Snackbar.make(itemView.parent as View, "已删除", Snackbar.LENGTH_SHORT)
                        .setAction("撤销") {
                            viewModel.deletedList.remove(entity)
                            if (position == 0)
                                viewModel.mainListDataBackup.add(0, entity)
                            else {
                                for (index in position - 1 downTo 0) {
                                    if (viewModel.mainListDataBackup.contains(viewModel.mainListData.value[index])) {
                                        if (index + 1 != viewModel.mainListDataBackup.size)
                                            viewModel.mainListDataBackup.add(index + 1, entity)
                                        else {
                                            viewModel.mainListDataBackup.add(entity)
                                        }
                                        break
                                    }
                                }
                            }
                            submitList(viewModel.mainListDataBackup.toList())
                        }
                        .show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact_list, parent, false)
        val binding = ItemContactListBinding.bind(view)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun onViewAttachedToWindow(holder: ListViewHolder) {
        if (viewModel.deleteMode) {
            holder.binding.deleteButton.visibility = View.VISIBLE
        } else {
            holder.binding.deleteButton.visibility = View.GONE
        }
        super.onViewAttachedToWindow(holder)
    }

}