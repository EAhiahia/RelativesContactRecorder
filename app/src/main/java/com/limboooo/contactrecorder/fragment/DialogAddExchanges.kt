package com.limboooo.contactrecorder.fragment


import android.icu.text.DateFormat
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.text.InputType
import android.text.method.TextKeyListener.clear
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isEmpty
import androidx.core.view.isNotEmpty
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.limboooo.contactrecorder.R
import com.limboooo.contactrecorder.databinding.DialogAddContactBinding
import com.limboooo.contactrecorder.repository.room.ProjectViewModel
import com.limboooo.contactrecorder.repository.room.entity.*
import com.limboooo.contactrecorder.tools.showShortToast
import kotlinx.coroutines.launch
import java.util.*


class DialogAddExchanges : DialogFragment() {

    private var isNew = false
    private lateinit var binding: DialogAddContactBinding
    private val viewModel: ProjectViewModel by activityViewModels()
    private var mName = ""
    private var mThing1 = ""
    private var mThing2 = ""
    private var mMoney1 = ""
    private var mMoney2 = ""
    private var mPhone = ""
    private var mEmial = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAddContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()
        binding.topAppBar.apply {
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.save -> {
                        //如果是名字是空的，就弹窗提醒
                        if (binding.name.isEmpty()) {
                            MaterialAlertDialogBuilder(requireContext())
                                .setMessage("没有填写是哪一家的人亲")
                                .setIcon(R.drawable.ic_alert)
                                .setPositiveButton("返回修改", null)
                                .show()
                            true
                        } else {
                            //TODO:展示进度条，进行保存工作
                            var dialog = MaterialAlertDialogBuilder(requireContext())
                                .setView(CircularProgressIndicator(requireContext()).apply {
                                    layoutParams = LinearLayout.LayoutParams(
                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT
                                    )
                                })
                                .show()
                            //TODO("检查数据，进行数据的本地化存储和UI更改")
                            //检查某些数据是否出现过，没有出现过就添加到本地化存储当中
                            saveNormalSet()
                            if (save(isNew) > 0) {
                                "保存完成".showShortToast()
                            } else {
                                "保存失败，存储空间不足，请清理".showShortToast()
                            }
                            dialog.cancel()
                            dialog = null
                            up()
                            true
                        }
                    }
                    else -> false
                }
            }
            setNavigationOnClickListener {
                if (binding.name.isNotEmpty()
                    || binding.phone.isNotEmpty()
                    || binding.email.isNotEmpty()
                    || binding.reason.isNotEmpty()
                    || binding.reason1.isNotEmpty()
                    || binding.howMuch.isNotEmpty()
                    || binding.howMuch1.isNotEmpty()
                ) {
                    MaterialAlertDialogBuilder(requireContext())
                        .setMessage("是否要舍弃已添加的内容？")
                        .setNegativeButton("否，继续添加", null)
                        .setPositiveButton("是，舍弃") { _, _ ->
                            up()
                        }
                        .show()
                }
            }
        }
        (binding.name.editText as MaterialAutoCompleteTextView).apply {
            //双向绑定items
            lifecycleScope.launch {
                viewModel.getNormalSet().collect {
                    setSimpleItems(it.names.toTypedArray())
                }
            }
            //当为空的时候禁用保存按钮
            this.addTextChangedListener {
                binding.topAppBar.menu.findItem(R.id.save).isEnabled = it!!.isNotEmpty()
            }
            setOnItemClickListener { _, _, position, _ ->
                //当items被点击，就弹窗问是否载入已有数据
                MaterialAlertDialogBuilder(requireContext())
                    .setMessage("发现   ${viewModel.normalSet.names[position]}   已存在，是否载入已有数据")
                    .setPositiveButton("载入") { _, _ ->
                        loadTargetContactInfo(viewModel.normalSet.names[position])
                        isNew = false
                    }
                    .setNegativeButton("建立为新的联系人") { _, _ ->
                        text.append("_2")
                        isNew = true
                    }
                    .show()
            }
        }
        binding.time.editText!!.apply {
            inputType = InputType.TYPE_NULL
            setOnClickListener {
                showDatePicker()
            }
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    showDatePicker()
                }
            }
        }
        (binding.reason.editText!! as MaterialAutoCompleteTextView).apply {
            //双向绑定items
            lifecycleScope.launch {
                viewModel.getNormalSet().collect {
                    setSimpleItems(it.things.toTypedArray())
                }
            }
        }
        (binding.reason1.editText!! as MaterialAutoCompleteTextView).apply {
            //双向绑定items
            lifecycleScope.launch {
                viewModel.getNormalSet().collect {
                    setSimpleItems(it.things.toTypedArray())
                }
            }
        }
        (binding.howMuch.editText!! as MaterialAutoCompleteTextView).apply {
            //双向绑定items
            lifecycleScope.launch {
                viewModel.getNormalSet().collect {
                    setSimpleItems(it.moneys.toTypedArray())
                }
            }
        }
        (binding.howMuch1.editText!! as MaterialAutoCompleteTextView).apply {
            //双向绑定items
            lifecycleScope.launch {
                viewModel.getNormalSet().collect {
                    setSimpleItems(it.moneys.toTypedArray())
                }
            }
        }
        (binding.email.editText!! as MaterialAutoCompleteTextView).apply {
            //双向绑定items
            lifecycleScope.launch {
                viewModel.getNormalSet().collect {
                    setSimpleItems(it.emails.toTypedArray())
                }
            }
        }
        (binding.phone.editText!! as MaterialAutoCompleteTextView).apply {
            //双向绑定items
            lifecycleScope.launch {
                viewModel.getNormalSet().collect {
                    setSimpleItems(it.phones.toTypedArray())
                }
            }
        }
    }

    private fun save(isNew: Boolean): Int {
        var result = 0
        lifecycleScope.launch {
            if (isNew) {
                RelativesInfoWhole(
                    RelativesBaseInfo(null, mName, 520, 430),
                    arrayListOf(Phones(null, null, mPhone)),
                    arrayListOf(Emails(null, null, mEmial)),
                    arrayListOf(Exchanges(null, null, null, mThing1, mMoney1)),
                    arrayListOf(Exchanges(null, null, null, mThing2, mMoney2))
                ).apply {
                    result = viewModel.save(true, this)
                }
            } else {
                viewModel.allData.forEach {
                    if (it.baseInfo.name == mName) {
                        it.apply {
                            baseInfo.apply {
                                moneyReceivedWhole = 520
                                moneyGaveWhole = 430
                            }
                            moneyReceived.add(Exchanges(null, null, null, mThing1, mMoney1))
                            moneyGave.add(Exchanges(null, null, null, mThing2, mMoney2))
                            phones.add(Phones(null, null, mPhone))
                            emails.add(Emails(null, null, mEmial))
                        }.apply {
                            result = viewModel.save(false, this)
                        }
                    }
                }
            }
        }
        return result
    }

    private fun loadTargetContactInfo(name: String) {
        viewModel.allData.forEach {
            if (it.baseInfo.name == name) {
                binding.apply {
                    phone.editText!!.text.apply {
                        clear()
                        append(it.phones.toString())
                    }
                    email.editText!!.text.apply {
                        clear()
                        append(it.emails.toString())
                    }
                    reason.editText!!.text.apply {
                        clear()
                        append(it.moneyReceived[0].thing)
                    }
                    howMuch.editText!!.text.apply {
                        clear()
                        append(it.moneyReceived[0].money)
                    }
                    reason1.editText!!.text.apply {
                        clear()
                        append(it.moneyGave[0].thing)
                    }
                    howMuch1.editText!!.text.apply {
                        clear()
                        append(it.moneyGave[0].money)
                    }
                }
            }
        }
    }

    private fun saveNormalSet() {
        binding.apply {
            //事件1
            mName = this.name.editText!!.text.toString()
            mThing1 = this.reason.editText!!.text.toString()
            mThing2 = this.reason1.editText!!.text.toString()
            mMoney1 = this.howMuch.editText!!.text.toString()
            mMoney2 = this.howMuch1.editText!!.text.toString()
            mName = this.phone.editText!!.text.toString()
            mEmial = this.email.editText!!.text.toString()
            lifecycleScope.launch {
                viewModel.normalSet.copy().apply {
                    names.add(mName)
                    things.add(mThing1)
                    things.add(mThing2)
                    moneys.add(mMoney1)
                    moneys.add(mMoney2)
                    phones.add(mPhone)
                    emails.add(mEmial)
                    viewModel.updateNormalSet(this)
                }
            }
        }
    }

    private fun showDatePicker() {
        MaterialDatePicker.Builder.datePicker()
            .setSelection(
                //TODO:if (输入框不为空) 把控件定位到输入框的时间上 else 定位到今天
                MaterialDatePicker.todayInUtcMilliseconds()
            )
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            .build()
            .apply {
                addOnPositiveButtonClickListener {
                    //TODO:把long转换为年月日和农历展示在输入框
                    binding.time.editText!!.text.apply {
                        clear()
                        append(
                            DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.CHINA)
                                .format(it)
                        )
                        //TODO: 添加农历
                    }
                }
            }
            .show(parentFragmentManager, "dataPicker")
    }

    private fun up() {
        parentFragmentManager.popBackStack()
    }
}