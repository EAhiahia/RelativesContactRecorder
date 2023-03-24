package com.limboooo.contactrecorder.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
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
import com.limboooo.contactrecorder.tools.showShortToast

const val DETAIL_RECEIVED = 0
const val DETAIL_RECEIVED_BACK = 3
const val DETAIL_GAVE = 4
const val DETAIL_GAVE_BACK = 1
const val DETAIL_PHONE = 5
const val DETAIL_EMAIL = 6

class DetailContentAdapter(
    private val viewModel: ProjectViewModel,
    private val type: Int,
    var anotherAdapter: DetailContentAdapter? = null
) :
    Adapter<DetailContentAdapter.DetailViewHolder>() {

    //是否可见
    lateinit var changeShowPaidVisibility: (Boolean) -> Unit

    //类型；金钱数
    lateinit var notifyMoneyChanged: (Int, Int) -> Unit

    inner class DetailViewHolder(val binding: ItemContactDetailCardBinding) :
        ViewHolder(binding.root) {

        fun bind(position: Int) {
            when (type) {
                DETAIL_EMAIL -> {
                    val target = viewModel.targetData.emails[position]
                    binding.thing.apply {
                        text = target.email
                        setTextColor(Color.BLUE)
                        isClickable = true
                        isFocusable = true
                        setOnClickListener {
                            val clipBoard =
                                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip: ClipData = ClipData.newPlainText("一个邮箱账号", target.email)
                            clipBoard.setPrimaryClip(clip)
                            "已粘贴到剪切板".showShortToast()
                        }
                    }
                    binding.money.visibility = View.GONE
                    binding.state.visibility = View.GONE
                    binding.delete.setOnClickListener {
                        viewModel.targetData.emails.remove(target)
                        notifyItemRemoved(position)
                    }
                }
                DETAIL_PHONE -> {
                    val target = viewModel.targetData.phones[position]
                    binding.thing.apply {
                        text = target.phone
                        setTextColor(Color.BLUE)
                        isClickable = true
                        isFocusable = true
                        setOnClickListener {
                            context.startActivity(Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:${target.phone}")
                            })
                        }
                    }
                    binding.money.visibility = View.GONE
                    binding.state.visibility = View.GONE
                    binding.delete.setOnClickListener {
                        viewModel.targetData.phones.remove(target)
                        notifyItemRemoved(position)
                    }
                }
                DETAIL_GAVE -> {
                    val target = viewModel.targetData.moneyGave[position]
                    binding.thing.text = target.thing
                    binding.money.text = "${target.money}元"
                    binding.delete.setOnClickListener {
                        viewModel.targetData.moneyGave.remove(target)
                        notifyItemRemoved(position)
                    }
                    binding.state.isChecked = false
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
                            //通知更新金钱总数
                            notifyMoneyChanged.invoke(DETAIL_GAVE, -target.money.toInt())
                            //通知更新按钮是否可见
                            changeShowPaidVisibility.invoke(viewModel.targetData.moneyGaveBack.isNotEmpty())
                        }
                    }
                }
                DETAIL_GAVE_BACK -> {
                    val target = viewModel.targetData.moneyGaveBack[position]
                    binding.thing.text = target.thing
                    binding.money.text = "${target.money}元"
                    binding.delete.setOnClickListener {
                        viewModel.targetData.moneyGaveBack.remove(target)
                        notifyItemRemoved(position)
                    }
                    binding.state.isChecked = true
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
                            //通知更新金钱总数
                            notifyMoneyChanged.invoke(DETAIL_GAVE, target.money.toInt())
                            //通知更新按钮是否可见
                            changeShowPaidVisibility.invoke(viewModel.targetData.moneyGaveBack.isNotEmpty())
                        }
                    }
                }
                DETAIL_RECEIVED -> {
                    val target = viewModel.targetData.moneyReceived[position]
                    binding.thing.text = target.thing
                    binding.money.text = "${target.money}元"
                    binding.delete.setOnClickListener {
                        viewModel.targetData.moneyReceived.remove(target)
                        notifyItemRemoved(position)
                    }
                    binding.state.isChecked = false
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
                            //通知更新金钱总数
                            notifyMoneyChanged.invoke(DETAIL_RECEIVED, -target.money.toInt())
                            //通知更新按钮是否可见
                            changeShowPaidVisibility.invoke(viewModel.targetData.moneyReceivedBack.isNotEmpty())
                        }
                    }
                }
                DETAIL_RECEIVED_BACK -> {
                    val target = viewModel.targetData.moneyReceivedBack[position]
                    binding.thing.text = target.thing
                    binding.money.text = "${target.money}元"
                    binding.delete.setOnClickListener {
                        viewModel.targetData.moneyReceivedBack.remove(target)
                        notifyItemRemoved(position)
                    }
                    binding.state.isChecked = true
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
                            //通知更新金钱总数
                            notifyMoneyChanged.invoke(DETAIL_RECEIVED, target.money.toInt())
                            //通知更新按钮是否可见
                            changeShowPaidVisibility.invoke(viewModel.targetData.moneyReceivedBack.isNotEmpty())
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
            DETAIL_PHONE -> viewModel.targetData.phones.size
            DETAIL_EMAIL -> viewModel.targetData.emails.size
            else -> throw Exception("不可能走到这里")
        }
}