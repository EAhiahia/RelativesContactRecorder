package com.limboooo.contactrecorder.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.limboooo.contactrecorder.R
import com.limboooo.contactrecorder.adapter.ContactListAdapter
import com.limboooo.contactrecorder.databinding.FragmentContactListBinding
import com.limboooo.contactrecorder.repository.room.ProjectViewModel
import kotlinx.coroutines.launch

class FragmentContactList : Fragment() {

    private lateinit var binding: FragmentContactListBinding

    private val viewModel: ProjectViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.contactList.apply {
            layoutManager = LinearLayoutManager(context)
            val myAdapter = ContactListAdapter {
                viewModel.targetContact = it
                findNavController().navigate(R.id.action_fragmentContactList_to_fragmentContactDetail)
            }
            adapter = myAdapter
            lifecycleScope.launch {
                viewModel.getAllContact().collect() {
                    myAdapter.submitList(it)
                }
            }
        }
        binding.addInformation.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.fragment_host, DialogAddExchanges(), "dialog")
                addToBackStack("dialog")
                setTransition(TRANSIT_FRAGMENT_OPEN)
            }
        }
    }
}