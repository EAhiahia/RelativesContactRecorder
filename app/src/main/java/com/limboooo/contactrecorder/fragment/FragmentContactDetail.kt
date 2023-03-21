package com.limboooo.contactrecorder.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.fragivity.navigator
import com.github.fragivity.pop
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.limboooo.contactrecorder.R
import com.limboooo.contactrecorder.adapter.*
import com.limboooo.contactrecorder.databinding.FragmentContactDetailBinding
import com.limboooo.contactrecorder.repository.ProjectViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class FragmentContactDetail : Fragment() {

    private lateinit var binding: FragmentContactDetailBinding
    private val viewModel: ProjectViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        runBlocking {
            lifecycleScope.launch(Dispatchers.IO) {
                viewModel.loadTargetDetail(requireArguments().getInt("position"))
            }
        }
        binding.detailTitle.apply {
            setNavigationOnClickListener {
                navigator.pop()
            }
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.save -> {

                        true
                    }
                    R.id.delete -> {
                        //TODO 待实现删除功能
                        MaterialAlertDialogBuilder(requireContext())
                            .setMessage("确定要删除${viewModel.targetData.baseInfo.name}的所有信息？")
                            .setPositiveButton("删除") { _, _ ->
                                lifecycleScope.launch(Dispatchers.IO) {
                                    viewModel.deleteTargetUser()
                                    navigator.pop()
                                }
                            }
                            .setNegativeButton("取消", null)
                            .show()
                        true
                    }
//                    R.id.share -> {
//                        //TODO 待实现分享功能，复制文字吧，申请权限
//                        true
//                    }
                    else -> throw Exception("不可能走到这里")
                }
            }
        }
        //自动加载viewModel.targetData的数据
        binding.cardReceived.apply {
            val cardReceivedAdapter = DetailContentAdapter(viewModel, DETAIL_RECEIVED)
            val cardReceivedBackAdapter = DetailContentAdapter(viewModel, DETAIL_RECEIVED_BACK)
            cardReceivedAdapter.anotherAdapter = cardReceivedBackAdapter
            cardReceivedBackAdapter.anotherAdapter = cardReceivedAdapter
            if(viewModel.targetData.moneyReceivedBack.isEmpty()){
                binding.showPaid.visibility = View.GONE
                binding.listBack.visibility = View.GONE
            }
            if (viewModel.targetData.moneyReceived.isEmpty()) {
                binding.listContent.visibility = View.GONE
                binding.listEmpty.visibility = View.VISIBLE
            } else {
                binding.listContent.run {
                    layoutManager = LinearLayoutManager(context)
                    adapter = cardReceivedAdapter
                }
                binding.listBack.run {
                    layoutManager = LinearLayoutManager(context)
                    adapter = cardReceivedBackAdapter
                }
                binding.receivedAll.text =
                    "累计收到${viewModel.targetData.baseInfo.moneyReceivedWhole}元"
                binding.showPaid.setOnClickListener {
                    if (binding.listBack.visibility == View.GONE) {
                        binding.listBack.visibility = View.VISIBLE
                        binding.showPaid.text = "隐藏已归还的钱"
                        (binding.showPaid as MaterialButton).icon =
                            AppCompatResources.getDrawable(context, R.drawable.ic_hide)
                    } else {
                        binding.listBack.visibility = View.GONE
                        binding.showPaid.text = "显示已归还的钱"
                        (binding.showPaid as MaterialButton).icon =
                            AppCompatResources.getDrawable(context, R.drawable.ic_show)
                    }
                }
            }
        }
        binding.cardGave.apply {
            val cardGaveAdapter = DetailContentAdapter(viewModel, DETAIL_GAVE)
            val cardGaveBackAdapter = DetailContentAdapter(viewModel, DETAIL_GAVE_BACK)
            cardGaveAdapter.anotherAdapter = cardGaveBackAdapter
            cardGaveBackAdapter.anotherAdapter = cardGaveAdapter
            if(viewModel.targetData.moneyGaveBack.isEmpty()){
                binding.showPaid.visibility = View.GONE
                binding.listBack.visibility = View.GONE
            }
            if (viewModel.targetData.moneyGave.isEmpty()) {
                binding.listContent.visibility = View.GONE
                binding.listEmpty.visibility = View.VISIBLE
            } else {
                binding.listContent.run {
                    layoutManager = LinearLayoutManager(context)
                    adapter = cardGaveAdapter
                }
                binding.listBack.run {
                    layoutManager = LinearLayoutManager(context)
                    adapter = cardGaveBackAdapter
                }
                binding.receivedAll.text = "累计给出${viewModel.targetData.baseInfo.moneyGaveWhole}元"
                binding.showPaid.setOnClickListener {
                    if (binding.listBack.visibility == View.GONE) {
                        binding.listBack.visibility = View.VISIBLE
                        binding.showPaid.text = "隐藏已归还的钱"
                        (binding.showPaid as MaterialButton).icon =
                            AppCompatResources.getDrawable(context, R.drawable.ic_hide)
                    } else {
                        binding.listBack.visibility = View.GONE
                        binding.showPaid.text = "显示已归还的钱"
                        (binding.showPaid as MaterialButton).icon =
                            AppCompatResources.getDrawable(context, R.drawable.ic_show)
                    }
                }
            }
        }
    }

}