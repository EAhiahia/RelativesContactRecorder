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
import com.github.fragivity.push
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.limboooo.contactrecorder.R
import com.limboooo.contactrecorder.adapter.*
import com.limboooo.contactrecorder.databinding.CardContactDetailBinding
import com.limboooo.contactrecorder.databinding.FragmentContactDetailBinding
import com.limboooo.contactrecorder.repository.ProjectViewModel
import com.limboooo.contactrecorder.repository.initAnimator
import com.limboooo.contactrecorder.repository.room.entity.whole.Emails
import com.limboooo.contactrecorder.repository.room.entity.whole.MoneyGave
import com.limboooo.contactrecorder.repository.room.entity.whole.MoneyReceived
import com.limboooo.contactrecorder.repository.room.entity.whole.Phones
import com.limboooo.contactrecorder.repository.showLog
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
            viewModel.loadTargetDetail(requireArguments().getInt("position"))
        }
        initContactInformation()
        initToolbar()
        initCardReceived()
        initCardGave()
    }

    private fun initCardGave() {
        binding.cardGave.apply {
            binding.addReceivedMoney.setOnClickListener {
                backupBeforeJump()
                navigator.push(DialogAddExchanges::class) {
                    initAnimator()
                }
            }
            val cardGaveAdapter = DetailContentAdapter(viewModel, DETAIL_GAVE).apply {
                changeShowPaidVisibility = {
                    if (it) {
                        binding.showPaid.visibility = View.VISIBLE
                    } else {
                        binding.showPaid.visibility = View.GONE
                    }
                }
                notifyMoneyChanged = { type, money ->
                    if (type == DETAIL_RECEIVED) {
                        viewModel.targetData.baseInfo.moneyReceivedWhole += money
                        if(viewModel.targetData.baseInfo.moneyReceivedWhole==0){
                            binding.listEmpty.visibility = View.VISIBLE
                            binding.listContainer.visibility = View.GONE
                        }else{
                            binding.listEmpty.visibility = View.GONE
                            binding.listContainer.visibility = View.VISIBLE
                            binding.receivedAll.text =
                                "累计收到${viewModel.targetData.baseInfo.moneyReceivedWhole}元"
                        }
                    } else {
                        viewModel.targetData.baseInfo.moneyGaveWhole += money
                        if(viewModel.targetData.baseInfo.moneyGaveWhole==0){
                            binding.listEmpty.visibility = View.VISIBLE
                            binding.listContainer.visibility = View.GONE
                        }else{
                            binding.listEmpty.visibility = View.GONE
                            binding.listContainer.visibility = View.VISIBLE
                            binding.receivedAll.text =
                                "累计给出${viewModel.targetData.baseInfo.moneyGaveWhole}元"
                        }
                    }
                }
            }
            val cardGaveBackAdapter = DetailContentAdapter(viewModel, DETAIL_GAVE_BACK).apply {
                changeShowPaidVisibility = {
                    if (it) {
                        binding.showPaid.visibility = View.VISIBLE
                    } else {
                        binding.showPaid.visibility = View.GONE
                    }
                }
                notifyMoneyChanged = { type, money ->
                    if (type == DETAIL_RECEIVED) {
                        viewModel.targetData.baseInfo.moneyReceivedWhole += money
                        if(viewModel.targetData.baseInfo.moneyReceivedWhole==0){
                            binding.listEmpty.visibility = View.VISIBLE
                            binding.listContainer.visibility = View.GONE
                        }else{
                            binding.listEmpty.visibility = View.GONE
                            binding.listContainer.visibility = View.VISIBLE
                            binding.receivedAll.text =
                                "累计收到${viewModel.targetData.baseInfo.moneyReceivedWhole}元"
                        }
                    } else {
                        viewModel.targetData.baseInfo.moneyGaveWhole += money
                        if(viewModel.targetData.baseInfo.moneyGaveWhole==0){
                            binding.listEmpty.visibility = View.VISIBLE
                            binding.listContainer.visibility = View.GONE
                        }else{
                            binding.listEmpty.visibility = View.GONE
                            binding.listContainer.visibility = View.VISIBLE
                            binding.receivedAll.text =
                                "累计给出${viewModel.targetData.baseInfo.moneyGaveWhole}元"
                        }
                    }
                }
            }
            cardGaveAdapter.anotherAdapter = cardGaveBackAdapter
            cardGaveBackAdapter.anotherAdapter = cardGaveAdapter
            showPaidClickListener(binding)
            if (viewModel.targetData.moneyGaveBack.isEmpty()) {
                binding.showPaid.visibility = View.GONE
            } else {
                binding.showPaid.visibility = View.VISIBLE
            }
            binding.listBack.run {
                layoutManager = LinearLayoutManager(context)
                adapter = cardGaveBackAdapter
            }
            binding.listContent.run {
                layoutManager = LinearLayoutManager(context)
                adapter = cardGaveAdapter
            }
            if (viewModel.targetData.moneyReceived.isEmpty()) {
                binding.listContent.visibility = View.GONE
                binding.receivedAll.visibility = View.GONE
                binding.listEmpty.visibility = View.VISIBLE
            } else {
                binding.receivedAll.text =
                    "累计收到${viewModel.targetData.baseInfo.moneyGaveWhole}元"
            }
        }
    }

    private fun initCardReceived() {
        binding.cardReceived.apply {
            binding.addReceivedMoney.setOnClickListener {
                backupBeforeJump()
                navigator.push(DialogAddExchanges::class) {
                    initAnimator()
                }
            }
            val cardReceivedAdapter = DetailContentAdapter(viewModel, DETAIL_RECEIVED).apply {
                changeShowPaidVisibility = {
                    if (it) {
                        binding.showPaid.visibility = View.VISIBLE
                    } else {
                        binding.showPaid.visibility = View.GONE
                    }
                }
                notifyMoneyChanged = { type, money ->
                    if (type == DETAIL_RECEIVED) {
                        viewModel.targetData.baseInfo.moneyReceivedWhole += money
                        if(viewModel.targetData.baseInfo.moneyReceivedWhole==0){
                            binding.listEmpty.visibility = View.VISIBLE
                            binding.listContainer.visibility = View.GONE
                        }else{
                            binding.listEmpty.visibility = View.GONE
                            binding.listContainer.visibility = View.VISIBLE
                            binding.receivedAll.text =
                                "累计收到${viewModel.targetData.baseInfo.moneyReceivedWhole}元"
                        }
                    } else {
                        viewModel.targetData.baseInfo.moneyGaveWhole += money
                        if(viewModel.targetData.baseInfo.moneyGaveWhole==0){
                            binding.listEmpty.visibility = View.VISIBLE
                            binding.listContainer.visibility = View.GONE
                        }else{
                            binding.listEmpty.visibility = View.GONE
                            binding.listContainer.visibility = View.VISIBLE
                            binding.receivedAll.text =
                                "累计给出${viewModel.targetData.baseInfo.moneyGaveWhole}元"
                        }
                    }
                }
            }
            val cardReceivedBackAdapter =
                DetailContentAdapter(viewModel, DETAIL_RECEIVED_BACK).apply {
                    changeShowPaidVisibility = {
                        if (it) {
                            binding.showPaid.visibility = View.VISIBLE
                        } else {
                            binding.showPaid.visibility = View.GONE
                        }
                    }
                    notifyMoneyChanged = { type, money ->
                        if (type == DETAIL_RECEIVED) {
                            viewModel.targetData.baseInfo.moneyReceivedWhole += money
                            if(viewModel.targetData.baseInfo.moneyReceivedWhole==0){
                                binding.listEmpty.visibility = View.VISIBLE
                                binding.listContainer.visibility = View.GONE
                            }else{
                                binding.listEmpty.visibility = View.GONE
                                binding.listContainer.visibility = View.VISIBLE
                                binding.receivedAll.text =
                                    "累计收到${viewModel.targetData.baseInfo.moneyReceivedWhole}元"
                            }
                        } else {
                            viewModel.targetData.baseInfo.moneyGaveWhole += money
                            if(viewModel.targetData.baseInfo.moneyGaveWhole==0){
                                binding.listEmpty.visibility = View.VISIBLE
                                binding.listContainer.visibility = View.GONE
                            }else{
                                binding.listEmpty.visibility = View.GONE
                                binding.listContainer.visibility = View.VISIBLE
                                binding.receivedAll.text =
                                    "累计给出${viewModel.targetData.baseInfo.moneyGaveWhole}元"
                            }
                        }
                    }
                }
            cardReceivedAdapter.anotherAdapter = cardReceivedBackAdapter
            cardReceivedBackAdapter.anotherAdapter = cardReceivedAdapter
            showPaidClickListener(binding)
            if (viewModel.targetData.moneyReceivedBack.isEmpty()) {
                binding.showPaid.visibility = View.GONE
            } else {
                binding.showPaid.visibility = View.VISIBLE
            }
            binding.listBack.run {
                layoutManager = LinearLayoutManager(context)
                adapter = cardReceivedBackAdapter
            }
            binding.listContent.run {
                layoutManager = LinearLayoutManager(context)
                adapter = cardReceivedAdapter
            }
            if (viewModel.targetData.moneyReceived.isEmpty()) {
                binding.listContent.visibility = View.GONE
                binding.receivedAll.visibility = View.GONE
                binding.listEmpty.visibility = View.VISIBLE
            } else {
                binding.receivedAll.text =
                    "累计收到${viewModel.targetData.baseInfo.moneyReceivedWhole}元"
            }
        }
    }

    private fun initToolbar() {
        binding.detailTitle.apply {
            title = viewModel.targetData.baseInfo.name
            setNavigationOnClickListener {
                navigator.pop()
            }
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.save -> {
                        lifecycleScope.launch(Dispatchers.IO) {
                            viewModel.saveTarget()
                            launch(Dispatchers.Main) {
                                navigator.pop()
                            }
                        }
                        true
                    }
                    R.id.delete -> {
                        MaterialAlertDialogBuilder(requireContext())
                            .setMessage("确定要删除${viewModel.targetData.baseInfo.name}的所有信息？")
                            .setPositiveButton("删除") { _, _ ->
                                lifecycleScope.launch(Dispatchers.IO) {
                                    viewModel.deleteTargetUser()
                                    launch(Dispatchers.Main) {
                                        navigator.pop()
                                    }
                                }
                            }
                            .setNegativeButton("取消", null)
                            .show()
                        true
                    }
                    else -> throw Exception("不可能走到这里")
                }
            }
        }
    }

    private fun initContactInformation() {
        viewModel.targetData.toString().showLog()
        if (viewModel.targetData.phones.isEmpty() && viewModel.targetData.emails.isEmpty()) {
            binding.listEmpty.visibility = View.VISIBLE
            binding.listPhone.visibility = View.GONE
            binding.listPhoneTitle.visibility = View.GONE
            binding.listEmail.visibility = View.GONE
            binding.listEmailTitle.visibility = View.GONE
            return
        }
        if (viewModel.targetData.phones.isNotEmpty()) {
            binding.listPhone.adapter = DetailContentAdapter(viewModel, DETAIL_PHONE)
            binding.listPhone.layoutManager = LinearLayoutManager(context)
        } else {
            binding.listPhone.visibility = View.GONE
            binding.listPhoneTitle.visibility = View.GONE
        }
        if (viewModel.targetData.emails.isNotEmpty()) {
            binding.listEmail.adapter = DetailContentAdapter(viewModel, DETAIL_EMAIL)
            binding.listEmail.layoutManager = LinearLayoutManager(context)
        } else {
            binding.listEmail.visibility = View.GONE
            binding.listEmailTitle.visibility = View.GONE
        }
        binding.add.setOnClickListener {
            backupBeforeJump()
            navigator.push(DialogAddExchanges::class) {
                initAnimator()
            }
        }
    }

    private fun showPaidClickListener(binding: CardContactDetailBinding) {
        binding.showPaid.setOnClickListener {
            if (binding.paidList.visibility == View.GONE) {
                binding.paidList.visibility = View.VISIBLE
                binding.showPaid.text = "隐藏已归还的钱"
                (binding.showPaid as MaterialButton).icon =
                    AppCompatResources.getDrawable(requireContext(), R.drawable.ic_hide)
            } else {
                binding.paidList.visibility = View.GONE
                binding.showPaid.text = "显示已归还的钱"
                (binding.showPaid as MaterialButton).icon =
                    AppCompatResources.getDrawable(requireContext(), R.drawable.ic_show)
            }
        }
    }

    private fun backupBeforeJump() {
        viewModel.targetData.apply {
            viewModel.targetDataBackup = viewModel.targetData.copy(
                baseInfo = baseInfo.copy(),
                phones = phones.toMutableList(),
                emails = emails.toMutableList(),
                moneyReceived = moneyReceived.toMutableList(),
                moneyReceivedBack = moneyReceivedBack.toMutableList(),
                moneyGave = moneyGave.toMutableList(),
                moneyGaveBack = moneyGaveBack.toMutableList()
            )
        }
        viewModel.targetData.apply {
            phones.add(Phones(0, 0, ""))
            emails.add(Emails(0, 0, ""))
            moneyGave.add(
                MoneyGave(
                    0,
                    0,
                    "${viewModel.today}   ${viewModel.today.lunar}",
                    "",
                    ""
                )
            )
            moneyReceived.add(
                MoneyReceived(
                    0,
                    0,
                    "${viewModel.today}   ${viewModel.today.lunar}",
                    "",
                    ""
                )
            )
        }
    }
}