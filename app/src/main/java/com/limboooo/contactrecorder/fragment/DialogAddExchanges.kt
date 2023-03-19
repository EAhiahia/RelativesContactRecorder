package com.limboooo.contactrecorder.fragment


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.fragivity.navigator
import com.github.fragivity.pop
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.limboooo.contactrecorder.R
import com.limboooo.contactrecorder.adapter.*
import com.limboooo.contactrecorder.databinding.DialogAddContactBinding
import com.limboooo.contactrecorder.repository.ProjectViewModel
import com.limboooo.contactrecorder.repository.room.entity.whole.RelativesBaseInfo
import com.limboooo.contactrecorder.repository.room.entity.whole.RelativesInfoWhole
import com.limboooo.contactrecorder.tools.showShortToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DialogAddExchanges : DialogFragment() {

    private var isNew = false
    private lateinit var binding: DialogAddContactBinding
    private val viewModel: ProjectViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAddContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        binding.topAppBar.apply {
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.save -> {
                        //TODO:展示进度条，进行保存工作
                        //TODO("检查数据，进行数据的本地化存储和UI更改")
                        //todo 不确定是否是点击保存就会自动返回
                        //检查某些数据是否出现过，没有出现过就添加到本地化存储当中
                        lifecycleScope.launch(Dispatchers.IO) {
                            save()
                            //todo 保存常见项
                            viewModel.saveDownList(binding.name.text.toString())
                            "保存完成".showShortToast()
                            up()
                        }
                        true
//                        }
                    }
                    else -> false
                }
            }
            setNavigationOnClickListener {
                //todo 添加是否有添加内容的判断
                if (viewModel.isHaveContent) {
                    MaterialAlertDialogBuilder(requireContext())
                        .setMessage("是否要舍弃已添加的内容？")
                        .setNegativeButton("否，继续添加", null)
                        .setPositiveButton("是，舍弃") { _, _ ->
                            up()
                        }
                        .show()
                }else{
                    navigator.pop()
                }
            }
        }

        binding.name.run {
            //当为空的时候禁用保存按钮
            addTextChangedListener {
                binding.topAppBar.menu.findItem(R.id.save).isEnabled = it!!.isNotBlank()
            }
            setOnItemClickListener { _, _, position, _ ->
                //当items被点击，就弹窗问是否载入已有数据
                val targetName = viewModel.names[position]
                MaterialAlertDialogBuilder(requireContext())
                    .setMessage("发现 $targetName 已存在，是否载入已有数据")
                    .setPositiveButton("载入") { _, _ ->
                        loadTargetContactInfo(targetName)
                        isNew = false
                    }
                    .setNegativeButton("建立为新的联系人") { _, _ ->
                        text.append("_${kotlin.random.Random.nextInt(100000)}")
                        isNew = true
                    }
                    .show()
            }
        }
    }

    private fun initAdapter() {
        binding.phoneList.apply {
            adapter = AddAdapter(viewModel, INPUT_PHONE)
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.emailList.apply {
            adapter = AddAdapter(viewModel, INPUT_EMAIL)
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.gaveList.apply {
            adapter = AddAdapter(viewModel, INPUT_THING_GAVE, parentFragmentManager)
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.receivedList.apply {
            adapter = AddAdapter(viewModel, INPUT_THING_RECEIVED, parentFragmentManager)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun save() {
        val new: RelativesInfoWhole
        if (isNew) {
            //todo 计算收入给出的差额
            new = RelativesInfoWhole(
                RelativesBaseInfo(0, binding.name.text!!.toString(), 0, 0),
                viewModel.inputPhone,
                viewModel.inputEmail,
                viewModel.inputThingReceived,
                viewModel.inputThingGave
            )
        } else {
            new = viewModel.targetData.copy(
                phones = viewModel.inputPhone,
                emails = viewModel.inputEmail,
                moneyGave = viewModel.inputThingGave,
                moneyReceived = viewModel.inputThingReceived
            )
        }
        viewModel.targetData = new
        viewModel.saveTarget()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadTargetContactInfo(name: String) {
        lifecycleScope.launch {
            viewModel.targetData = viewModel.getOneUser(name).apply {
                //todo 载入已有数据，需要找到id，避免重复保存，（应该不需要id，毕竟是update）
                viewModel.inputEmail.run {
                    clear()
                    addAll(emails)
                }
                viewModel.inputPhone.run {
                    clear()
                    addAll(phones)
                }
                viewModel.inputThingGave.run {
                    clear()
                    addAll(moneyGave)
                }
                viewModel.inputThingReceived.run {
                    clear()
                    addAll(moneyReceived)
                }
                binding.phoneList.adapter!!.notifyDataSetChanged()
                binding.emailList.adapter!!.notifyDataSetChanged()
                binding.gaveList.adapter!!.notifyDataSetChanged()
                binding.receivedList.adapter!!.notifyDataSetChanged()
            }
        }
    }

    private fun up() {
        navigator.pop()
    }
}