package com.limboooo.contactrecorder.fragment


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
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
import com.limboooo.contactrecorder.tools.showShortToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DialogAddExchanges : DialogFragment() {

    private var isNew = true
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
        requireActivity().onBackPressedDispatcher.run {
            addCallback(viewLifecycleOwner) {
                viewModel.targetDataBackup?.let {
                    viewModel.targetData = it
                }
                isEnabled = false
                onBackPressed()
            }
        }
        initAdapter()
        binding.topAppBar.apply {
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.save -> {
                        lifecycleScope.launch(Dispatchers.IO) {
                            save()
                            viewModel.saveDownList(binding.name.text.toString())
                            launch(Dispatchers.Main) {
                                "保存完成".showShortToast()
                                up()
                            }
                        }
                        true
                    }
                    else -> false
                }
            }
            setNavigationOnClickListener {
                if (binding.name.text.isNotBlank()) {
                    MaterialAlertDialogBuilder(requireContext())
                        .setMessage("是否要舍弃已添加的内容？")
                        .setNegativeButton("否，继续添加", null)
                        .setPositiveButton("是，舍弃") { _, _ ->
                            up()
                        }
                        .show()
                } else {
                    up()
                }
            }
        }
        binding.name.run {
            text.append(viewModel.targetData.baseInfo.name)
            if (viewModel.targetData.baseInfo.name.isNotBlank()) binding.topAppBar.menu.findItem(R.id.save).isEnabled = true
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
                        //todo 查找同名字的数目，然后递增
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
        checkData()
        viewModel.saveTarget()
    }

    private fun checkData() {
        var moneyReceivedWhole = 0
        var moneyGaveWhole = 0
        viewModel.targetData.moneyReceived.iterator().let {
            while (it.hasNext()) {
                val next = it.next()
                if (next.money.isNotBlank() && next.thing.isNotBlank()) moneyReceivedWhole += next.money.toInt()
                else it.remove()
            }
        }
        viewModel.targetData.moneyGave.iterator().let {
            while (it.hasNext()) {
                val next = it.next()
                if (next.money.isNotBlank() && next.thing.isNotBlank()) moneyGaveWhole += next.money.toInt()
                else it.remove()
            }
        }
        viewModel.targetData.baseInfo.apply {
            name = binding.name.text.toString()
            this.moneyReceivedWhole = moneyReceivedWhole
            this.moneyGaveWhole = moneyGaveWhole
        }

        viewModel.targetData.moneyReceivedBack.iterator().let {
            while (it.hasNext()) {
                val next = it.next()
                if (next.money.isNotBlank() && next.thing.isNotBlank()) moneyReceivedWhole += next.money.toInt()
                else it.remove()
            }
        }
        viewModel.targetData.moneyGaveBack.iterator().let {
            while (it.hasNext()) {
                val next = it.next()
                if (next.money.isNotBlank() && next.thing.isNotBlank()) moneyGaveWhole += next.money.toInt()
                else it.remove()
            }
        }
        viewModel.targetData.emails.iterator().let {
            while (it.hasNext()) {
                val next = it.next()
                if (next.email.isBlank()) it.remove()
            }
        }
        viewModel.targetData.phones.iterator().let {
            while (it.hasNext()) {
                val next = it.next()
                if (next.phone.isBlank()) it.remove()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadTargetContactInfo(name: String) {
        lifecycleScope.launch {
            viewModel.targetData = viewModel.getOneUser(name).apply {
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