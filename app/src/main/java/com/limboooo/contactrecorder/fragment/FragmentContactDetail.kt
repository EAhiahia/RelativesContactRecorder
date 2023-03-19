package com.limboooo.contactrecorder.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.limboooo.contactrecorder.R
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
                findNavController().navigateUp()
            }
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.delete -> {
                        //TODO 待实现删除功能
                        MaterialAlertDialogBuilder(requireContext())
                            .setMessage("确定要删除XXX？")
                            .setPositiveButton("删除") { _, _ ->
                                Snackbar.make(binding.container, "已删除XXX", Snackbar.LENGTH_SHORT)
                                    .setAction("撤销") {

                                    }.show()
                            }
                            .setNegativeButton("取消", null)
                            .show()
                        true
                    }
                    R.id.share -> {
                        //TODO 待实现分享功能，复制文字吧，申请权限
                        true
                    }
                    else -> false
                }
            }
        }

        //TODO 载入viewmodel.detailTarget数据
        binding.apply {
            detailTitle.title = viewModel.targetData.baseInfo.name
            cardReceived.apply {
                cardTitle.text = "收到的钱"
                if (viewModel.targetData.moneyReceived.isEmpty()) {
                    listEmpty.visibility = View.VISIBLE
                    listReceivedDetail.visibility = View.GONE
                }
                addReceivedMoney.setOnClickListener {

                }
                showPaid.setOnClickListener {

                }
            }
            cardGave.apply {
                cardTitle.text = "给出的钱"
                listEmpty.visibility = View.VISIBLE
                listReceivedDetail.visibility = View.GONE
                addReceivedMoney.setOnClickListener {

                }
                showPaid.setOnClickListener {

                }
            }
        }
    }

}