package com.limboooo.contactrecorder.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.limboooo.contactrecorder.R
import com.limboooo.contactrecorder.adapter.*
import com.limboooo.contactrecorder.databinding.DialogAddContactBinding
import com.limboooo.contactrecorder.repository.ProjectViewModel
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
                        //如果是名字是空的，就弹窗提醒
                        if (binding.name.text.isEmpty()) {
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
                            lifecycleScope.launch(Dispatchers.IO) {
                                saveNormalSet()
                                save(isNew)
                                dialog.cancel()
                                dialog = null
                                up()
                            }
                            true
                        }
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
                }
            }
        }

        binding.name.run {
            //当为空的时候禁用保存按钮
            addTextChangedListener {
                binding.topAppBar.menu.findItem(R.id.save).isEnabled = it!!.isNotEmpty()
            }
            setOnItemClickListener { _, _, position, _ ->
                //当items被点击，就弹窗问是否载入已有数据
                MaterialAlertDialogBuilder(requireContext())
                    .setMessage("发现   ${viewModel.names.value[position]}   已存在，是否载入已有数据")
                    .setPositiveButton("载入") { _, _ ->
                        loadTargetContactInfo(viewModel.names.value[position])
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
            adapter = AddAdapter(viewModel, INPUT_THING_GAVE,parentFragmentManager)
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.receivedList.apply {
            adapter = AddAdapter(viewModel, INPUT_THING_RECEIVED,parentFragmentManager)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun save(isNew: Boolean) {
        if (isNew) {

        } else {

        }
        "保存完成".showShortToast()
    }

    private fun loadTargetContactInfo(name: String) {
        viewModel.dataList.value.forEach {
            if (it.baseInfo.name == name) {
                viewModel.targetData = it
            }
        }
    }

    private fun saveNormalSet() {

    }

    private fun up() {
        parentFragmentManager.popBackStack()
    }
}