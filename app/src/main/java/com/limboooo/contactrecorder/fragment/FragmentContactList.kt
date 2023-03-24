package com.limboooo.contactrecorder.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.fragivity.navigator
import com.github.fragivity.push
import com.google.android.material.button.MaterialButton
import com.limboooo.contactrecorder.R
import com.limboooo.contactrecorder.adapter.ContactListAdapter
import com.limboooo.contactrecorder.databinding.FragmentContactListBinding
import com.limboooo.contactrecorder.repository.ProjectViewModel
import com.limboooo.contactrecorder.repository.initAnimator
import com.limboooo.contactrecorder.repository.room.entity.whole.*
import com.limboooo.contactrecorder.tools.showShortToast
import kotlinx.coroutines.launch

class FragmentContactList : Fragment() {

    private lateinit var binding: FragmentContactListBinding
    private val viewModel: ProjectViewModel by activityViewModels()
    private lateinit var myAdapter: ContactListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().onBackPressedDispatcher.run {
            addCallback(viewLifecycleOwner) {
                if (viewModel.deleteMode.value!!) {
                    binding.toolbar.run {
                        menu.getItem(0).isVisible = false
                        navigationIcon = null
                    }
                    myAdapter.submitList(viewModel.mainListDataBackup)
                    viewModel.mainListData.value = viewModel.mainListDataBackup.toMutableList()
                    viewModel.mainListDataBackup.clear()
                    Toast.makeText(requireContext(), "已撤销所有修改", Toast.LENGTH_SHORT).show()
                    binding.contactList.children.forEach {
                        it.findViewById<MaterialButton>(R.id.delete_button).visibility = View.GONE
                    }
                    viewModel.deleteMode.value = false
                } else {
                    isEnabled = false
                    onBackPressed()
                }
            }
        }
        binding = FragmentContactListBinding.inflate(inflater, container, false)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.mainListData.collect {
                        if (it.isEmpty()) {
                            binding.lottie.visibility = View.VISIBLE
                            binding.lottieTitle.visibility = View.VISIBLE
                            binding.contactList.visibility = View.GONE
                        } else {
                            binding.lottie.visibility = View.GONE
                            binding.lottieTitle.visibility = View.GONE
                            binding.contactList.visibility = View.VISIBLE
                            myAdapter.submitList(it)
                        }
                    }
                }
            }
        }
        binding.contactList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            myAdapter = ContactListAdapter(viewModel)
            adapter = myAdapter
        }
        binding.add.setOnClickListener {
            viewModel.targetData = RelativesInfoWhole(
                RelativesBaseInfo(0, "", 0, 0),
                mutableListOf(Phones(0, 0, "")),
                mutableListOf(Emails(0, 0, "")),
                mutableListOf(
                    MoneyReceived(
                        0,
                        0,
                        "${viewModel.today}   ${viewModel.today.lunar}",
                        "",
                        ""
                    )
                ),
                mutableListOf(),
                mutableListOf(
                    MoneyGave(
                        0,
                        0,
                        "${viewModel.today}   ${viewModel.today.lunar}",
                        "",
                        ""
                    )
                ),
                mutableListOf()
            )
            navigator.push(DialogAddExchanges::class) {
                initAnimator()
            }
        }
        binding.toolbar.menu.findItem(R.id.save).setOnMenuItemClickListener {
            if (viewModel.deletedList.value!!.isEmpty()) {
                "没有任何修改".showShortToast()
            } else {
                viewModel.saveAll()
            }
            viewModel.deleteMode.value = false
            true
        }
        viewModel.deletedList.observe(viewLifecycleOwner) {
            binding.toolbar.menu.getItem(0).isEnabled = it.isNotEmpty()
        }
        viewModel.deleteMode.observe(viewLifecycleOwner) { deleteMode ->
            if (deleteMode) {
                binding.toolbar.run {
                    menu.getItem(0).isVisible = true
                }
                binding.contactList.children.forEach {
                    it.findViewById<MaterialButton>(R.id.delete_button).visibility = View.VISIBLE
                }
                binding.add.visibility = View.GONE
            } else {
                binding.toolbar.run {
                    menu.getItem(0).isVisible = false
                }
                binding.contactList.children.forEach {
                    it.findViewById<MaterialButton>(R.id.delete_button).visibility = View.GONE
                }
                binding.add.visibility = View.VISIBLE
            }
        }
        return binding.root
    }

    override fun onStop() {
        viewModel.deleteMode.value = false
        super.onStop()
    }
}