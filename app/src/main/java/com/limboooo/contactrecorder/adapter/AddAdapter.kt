package com.limboooo.contactrecorder.adapter

import android.icu.text.SimpleDateFormat
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.limboooo.contactrecorder.R
import com.limboooo.contactrecorder.databinding.AddEmailViewBinding
import com.limboooo.contactrecorder.databinding.AddPhoneViewBinding
import com.limboooo.contactrecorder.databinding.AddThingViewBinding
import com.limboooo.contactrecorder.repository.ProjectViewModel
import com.limboooo.contactrecorder.repository.room.entity.whole.Emails
import com.limboooo.contactrecorder.repository.room.entity.whole.MoneyGave
import com.limboooo.contactrecorder.repository.room.entity.whole.MoneyReceived
import com.limboooo.contactrecorder.repository.room.entity.whole.Phones
import com.nlf.calendar.Solar
import java.util.*


const val INPUT_THING_GAVE = 0
const val INPUT_THING_RECEIVED = 3
const val INPUT_EMAIL = 1
const val INPUT_PHONE = 2

class AddAdapter(
    private val viewModel: ProjectViewModel,
    private val viewType: Int,
    private val fragmentManager: FragmentManager? = null
) : Adapter<AddAdapter.AddViewHolder>() {

    inner class AddViewHolder(val view: View) : ViewHolder(view) {

        fun bindData(position: Int) {
            when (viewType) {
                INPUT_THING_GAVE -> {
                    val binding = AddThingViewBinding.bind(view)
                    val targetData = viewModel.targetData.moneyGave[position]
                    binding.time.text!!.run {
                        clear()
                        append(targetData.time)
                    }
                    binding.time.addTextChangedListener {
                        viewModel.targetData.moneyGave[position].time= it.toString()
                    }
                    binding.money.text!!.run {
                        clear()
                        append(targetData.money)
                    }
                    binding.money.addTextChangedListener {
                        viewModel.targetData.moneyGave[position].money = it.toString()
                    }
                    binding.thing.text!!.run {
                        clear()
                        append(targetData.thing)
                    }
                    binding.thing.addTextChangedListener {
                        viewModel.targetData.moneyGave[position].thing = it.toString()
                    }
                    if (position == viewModel.targetData.moneyGave.size - 1) {
                        binding.money.setOnFocusChangeListener { _, hasFocus ->
                            if (!hasFocus) {
                                if (binding.money.text.isNotEmpty()) {
                                    addData(position, "gave")
                                }
                            }
                        }
                        binding.thing.setOnFocusChangeListener { _, hasFocus ->
                            if (!hasFocus) {
                                if (binding.thing.text.isNotEmpty()) {
                                    addData(position, "gave")
                                }
                            }
                        }
                    }
                }
                INPUT_THING_RECEIVED -> {
                    val binding = AddThingViewBinding.bind(view)
                    val targetData = viewModel.targetData.moneyReceived[position]
                    binding.time.text!!.run {
                        clear()
                        append(targetData.time)
                    }
                    binding.time.addTextChangedListener {
                        viewModel.targetData.moneyReceived[position].time = it.toString()
                    }
                    binding.money.text!!.run {
                        clear()
                        append(targetData.money)
                    }
                    binding.money.addTextChangedListener {
                        viewModel.targetData.moneyReceived[position].money = it.toString()
                    }
                    binding.thing.text!!.run {
                        clear()
                        append(targetData.thing)
                    }
                    binding.thing.addTextChangedListener {
                        viewModel.targetData.moneyReceived[position].thing = it.toString()
                    }
                    if (position == viewModel.targetData.moneyReceived.size - 1) {
                        binding.money.setOnFocusChangeListener { _, hasFocus ->
                            if (!hasFocus) {
                                if (binding.money.text.isNotEmpty()) {
                                    addData(position, "received")
                                }
                            }
                        }
                        binding.thing.setOnFocusChangeListener { _, hasFocus ->
                            if (!hasFocus) {
                                if (binding.thing.text.isNotEmpty()) {
                                    addData(position, "received")
                                }
                            }
                        }
                    }
                }
                INPUT_EMAIL -> {
                    val binding = AddEmailViewBinding.bind(view)
                    val targetData = viewModel.targetData.emails[position]
                    binding.email.text!!.run {
                        clear()
                        append(targetData.email)
                    }
                    binding.email.addTextChangedListener {
                        viewModel.targetData.emails[position].email = it.toString()
                    }
                    if (position == viewModel.targetData.emails.size - 1) {
                        binding.email.setOnFocusChangeListener { _, hasFocus ->
                            if (!hasFocus) {
                                if (binding.email.text.isNotEmpty()) {
                                    addData(position, "email")
                                }
                            }
                        }
                    }
                }
                INPUT_PHONE -> {
                    val binding = AddPhoneViewBinding.bind(view)
                    val targetData = viewModel.targetData.phones[position]
                    binding.phone.text!!.run {
                        clear()
                        append(targetData.phone)
                    }
                    binding.phone.addTextChangedListener {
                        viewModel.targetData.phones[position].phone = it.toString()
                    }
                    if (position == viewModel.targetData.phones.size - 1) {
                        binding.phone.setOnFocusChangeListener { _, hasFocus ->
                            if (!hasFocus) {
                                if (binding.phone.text.isNotEmpty()) {
                                    addData(position, "phone")
                                }
                            }
                        }
                    }
                }
            }
        }

        private fun addData(position: Int, where: String) {
            when (where) {
                "gave" -> {
                    if (viewModel.targetData.moneyGave.size < position + 2) {
                        viewModel.targetData.moneyGave.add(
                            MoneyGave(
                                0, 0, "${viewModel.today}   ${viewModel.today.lunar}", "", ""
                            )
                        )
                        notifyItemInserted(position + 1)
                    }
                }
                "received" -> {
                    if (viewModel.targetData.moneyReceived.size < position + 2) {
                        viewModel.targetData.moneyReceived.add(
                            MoneyReceived(
                                0, 0, "${viewModel.today}   ${viewModel.today.lunar}", "", ""
                            )
                        )
                        notifyItemInserted(position + 1)
                    }
                }
                "phone" -> {
                    if (viewModel.targetData.phones.size < position + 2) {
                        viewModel.targetData.phones.add(
                            Phones(
                                0, 0, ""
                            )
                        )
                        notifyItemInserted(position + 1)
                    }
                }
                "email" -> {
                    if (viewModel.targetData.emails.size < position + 2) {
                        viewModel.targetData.emails.add(
                            Emails(
                                0, 0, ""
                            )
                        )
                        notifyItemInserted(position + 1)
                    }
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddAdapter.AddViewHolder {
        val layoutId: Int = when (viewType) {
            INPUT_THING_GAVE -> R.layout.add_thing_view
            INPUT_THING_RECEIVED -> R.layout.add_thing_view
            INPUT_EMAIL -> R.layout.add_email_view
            INPUT_PHONE -> R.layout.add_phone_view
            else -> throw Exception("不可能走到这里")
        }
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        when (viewType) {
            INPUT_THING_GAVE -> {
                inputThingBind(view)
            }
            INPUT_THING_RECEIVED -> {
                inputThingBind(view)
            }
            INPUT_EMAIL -> {
                val binding = AddEmailViewBinding.bind(view)
                binding.email.setSimpleItems(viewModel.emails.toTypedArray())
            }
            INPUT_PHONE -> {
                val binding = AddPhoneViewBinding.bind(view)
                binding.phone.setSimpleItems(viewModel.phones.toTypedArray())
            }
        }
        return AddViewHolder(view)
    }

    override fun getItemCount(): Int = when (viewType) {
        INPUT_THING_GAVE -> viewModel.targetData.moneyGave.size
        INPUT_THING_RECEIVED -> viewModel.targetData.moneyReceived.size
        INPUT_EMAIL -> viewModel.targetData.emails.size
        INPUT_PHONE -> viewModel.targetData.phones.size
        else -> throw Exception("不可能走到这里")
    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    override fun onBindViewHolder(holder: AddViewHolder, position: Int) {
        holder.bindData(position)
    }

    private fun inputThingBind(view: View) {
        val binding = AddThingViewBinding.bind(view)
        binding.thing.setSimpleItems(viewModel.things.toTypedArray())
        binding.money.setSimpleItems(viewModel.moneys.toTypedArray())
        binding.time.run {
            inputType = InputType.TYPE_NULL
            setOnClickListener {
                showDatePicker(this)
            }
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    showDatePicker(this)
                }
            }
            text!!.append("${viewModel.today}   ${viewModel.today.lunar}")
        }
    }

    private fun showDatePicker(time: TextInputEditText) {
        val dateString = time.text!!.substring(0, 10)
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        val date: Date = inputFormat.parse(dateString)
        val timeInMillis: Long = date.time
        //todo 有bug，默认选择的是上一天
        MaterialDatePicker.Builder.datePicker().setSelection(
            timeInMillis
        ).setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR).build().apply {
            addOnPositiveButtonClickListener {
                val today = Solar.fromDate(Date(it))
                time.text!!.run {
                    clear()
                    append("$today   ${today.lunar}")
                }
            }
        }.show(fragmentManager!!, "dataPicker")
    }
}