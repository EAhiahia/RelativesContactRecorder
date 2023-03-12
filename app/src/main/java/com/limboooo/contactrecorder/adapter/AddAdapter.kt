package com.limboooo.contactrecorder.adapter

import android.icu.text.DateFormat
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

    inner class AddViewHolder(val view: View) : ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddViewHolder {
        val layoutId: Int = when (viewType) {
            INPUT_THING_GAVE -> R.layout.add_thing_view
            INPUT_THING_RECEIVED -> R.layout.add_thing_view
            INPUT_EMAIL -> R.layout.add_email_view
            INPUT_PHONE -> R.layout.add_phone_view
            else -> throw Exception("不可能走到这里")
        }
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
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
        when (viewType) {
            INPUT_THING_GAVE -> {
                inputThingBind(holder)
            }
            INPUT_THING_RECEIVED -> {
                inputThingBind(holder)
            }
            INPUT_EMAIL -> {
                val binding = AddEmailViewBinding.bind(holder.view)
                binding.email.setSimpleItems(viewModel.emails.value.toTypedArray())
            }
            INPUT_PHONE -> {
                val binding = AddPhoneViewBinding.bind(holder.view)
                binding.phone.setSimpleItems(viewModel.phones.value.toTypedArray())
            }
        }
    }

    private fun inputThingBind(holder: AddViewHolder) {
        val binding = AddThingViewBinding.bind(holder.view)
        binding.thing.setSimpleItems(viewModel.things.value.toTypedArray())
        binding.money.setSimpleItems(viewModel.moneys.value.toTypedArray())
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
            text!!.append(viewModel.today)
        }
    }

    private fun showDatePicker(time: TextInputEditText) {
        MaterialDatePicker.Builder.datePicker().setSelection(
            //TODO:if (输入框不为空) 把控件定位到输入框的时间上 else 定位到今天
            MaterialDatePicker.todayInUtcMilliseconds()
        ).setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR).build().apply {
            addOnPositiveButtonClickListener {
                //TODO:把long转换为年月日和农历展示在输入框
                time.text?.append(
                    DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.CHINA).format(it)
                )
                //TODO: 添加农历
            }
        }.show(fragmentManager!!, "dataPicker")
    }
}