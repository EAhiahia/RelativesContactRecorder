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
import com.github.fragivity.dialog.showDialog
import com.github.fragivity.navigator
import com.google.android.material.button.MaterialButton
import com.limboooo.contactrecorder.R
import com.limboooo.contactrecorder.adapter.ContactListAdapter
import com.limboooo.contactrecorder.databinding.FragmentContactListBinding
import com.limboooo.contactrecorder.repository.ProjectViewModel
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
            addCallback(viewLifecycleOwner){
                if(viewModel.deleteMode){
                    myAdapter.submitList(viewModel.mainListData.value)
                    viewModel.mainListDataBackup.run {
                        clear()
                        addAll(viewModel.mainListData.value)
                    }
                    Toast.makeText(requireContext(), "已撤销所有修改", Toast.LENGTH_SHORT).show()
//                    binding.finish.visibility = View.GONE
                    binding.contactList.children.forEach {
                        it.findViewById<MaterialButton>(R.id.delete_button).visibility = View.GONE
                    }
                    viewModel.deleteMode = false
                } else {
                    requireActivity().finish()
                    viewModel.save()
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
                            binding.lottieContainer.visibility = View.VISIBLE
                            binding.contactList.visibility = View.GONE
                        } else {
                            binding.lottieContainer.visibility = View.GONE
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
            navigator.showDialog(DialogAddExchanges::class)
        }
        return binding.root
    }

    override fun onResume() {
        if (viewModel.deleteMode) {
            binding.contactList.children.forEach {
                it.findViewById<MaterialButton>(R.id.delete_button).visibility = View.VISIBLE
            }
        } else {
            binding.contactList.children.forEach {
                it.findViewById<MaterialButton>(R.id.delete_button).visibility = View.VISIBLE
            }
        }
        super.onResume()
    }
}