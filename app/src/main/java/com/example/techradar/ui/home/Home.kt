package com.example.techradar.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.techradar.R
import com.example.techradar.adapter.ListAdapter
import com.example.techradar.databinding.FragmentHomeBinding
import com.example.techradar.model.Content
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Home : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var listAdapter: ListAdapter

    companion object {
        @JvmStatic
        fun newInstance() = Home()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listAdapter = ListAdapter(mutableListOf()) { content ->
            onItemClicked(content)
        }

        binding.recycler.adapter = listAdapter
        binding.recycler.layoutManager = LinearLayoutManager(context)
        collectNotes()

        binding.addButton.setOnClickListener { moveTo() }

        viewModel.allUsers()

        binding.tablelayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> viewModel.allUsers()
                    1 -> viewModel.allFavorites()
                    else -> viewModel.allUsers()
                }
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {}
            override fun onTabReselected(p0: TabLayout.Tab?) {}
        })
    }

    private fun moveTo() {
        findNavController().navigate(R.id.action_home_to_add)
    }

    private fun collectNotes() {
        lifecycleScope.launch {
            viewModel.homeAdd.collect {
                listAdapter.updateList(it)
            }
        }
    }

    private fun onItemClicked(content: Content) {
        val bundle = bundleOf(
            "id" to content.id.toString(),
            "name" to content.name,
            "firstname" to content.firstname,
            "phone" to content.phone,
            "email" to content.email,
            "birthday" to content.birthday,
            "wage" to content.wage,
            "note" to content.note,
            "favorite" to content.favorite,
            "picture" to (content.picture ?: "")

        )
        findNavController().navigate(R.id.action_home_to_detail, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
