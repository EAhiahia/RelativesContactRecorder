package com.limboooo.contactrecorder.adapter

import android.icu.text.SimpleDateFormat
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            //todo 当用户删除所有数据的时候，需要删除空行剩一个
            when (viewType) {
                INPUT_THING_GAVE -> {
                    val binding = AddThingViewBinding.bind(view)
                    if (position != viewModel.inputThingGave.size - 1) {
                        val targetData = viewModel.inputThingGave[position]
                        binding.time.text!!.run {
                            clear()
                            append(targetData.time)
                        }
                        binding.money.text!!.run {
                            clear()
                            append(targetData.money)
                        }
                        binding.thing.text!!.run {
                            clear()
                            append(targetData.thing)
                        }
                    } else {
                        binding.time.text!!.run {
                            clear()
                            append("${viewModel.today}   ${viewModel.today.lunar}")
                        }
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
                    if (position != viewModel.inputThingReceived.size - 1) {
                        val targetData = viewModel.inputThingGave[position]
                        binding.time.text!!.run {
                            clear()
                            append(targetData.time)
                        }
                        binding.money.text!!.run {
                            clear()
                            append(targetData.money)
                        }
                        binding.thing.text!!.run {
                            clear()
                            append(targetData.thing)
                        }
                    } else {
                        binding.time.text!!.run {
                            clear()
                            append("${viewModel.today}   ${viewModel.today.lunar}")
                        }
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
                    if (position != viewModel.inputEmail.size - 1) {
                        val targetData = viewModel.inputEmail[position]
                        binding.email.text!!.run {
                            clear()
                            append(targetData.email)
                        }
                    } else {
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
                    if (position != viewModel.inputEmail.size - 1) {
                        val targetData = viewModel.inputPhone[position]
                        binding.phone.text!!.run {
                            clear()
                            append(targetData.phone)
                        }
                    } else {
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
                    if (viewModel.inputThingGave.size < position + 2) {
                        viewModel.inputThingGave.add(
                            MoneyGave(
                                0,
                                0,
                                "${viewModel.today}   ${viewModel.today.lunar}",
                                "",
                                ""
                            )
                        )
                        notifyItemInserted(position + 1)
                    }
                }
                "received" -> {
                    if (viewModel.inputThingReceived.size < position + 2) {
                        viewModel.inputThingReceived.add(
                            MoneyReceived(
                                0,
                                0,
                                "${viewModel.today}   ${viewModel.today.lunar}",
                                "",
                                ""
                            )
                        )
                        notifyItemInserted(position + 1)
                    }
                }
                "phone" -> {
                    if (viewModel.inputPhone.size < position + 2) {
                        viewModel.inputPhone.add(
                            Phones(
                                0,
                                0,
                                ""
                            )
                        )
                        notifyItemInserted(position + 1)
                    }
                }
                "email" -> {
                    if (viewModel.inputEmail.size < position + 2) {
                        viewModel.inputEmail.add(
                            Emails(
                                0,
                                0,
                                ""
                            )
                        )
                        notifyItemInserted(position + 1)
                    }
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddViewHolder {
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
        INPUT_THING_GAVE -> viewModel.inputThingGave.size
        INPUT_THING_RECEIVED -> viewModel.inputThingReceived.size
        INPUT_EMAIL -> viewModel.inputEmail.size
        INPUT_PHONE -> viewModel.inputPhone.size
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