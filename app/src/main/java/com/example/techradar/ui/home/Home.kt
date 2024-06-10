package com.example.techradar.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.example.techradar.R
import com.example.techradar.adapter.ListAdapter
import com.example.techradar.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class Home : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private var listAdapter = ListAdapter(mutableListOf())


    companion object {
        fun newInstance(): Home {
            return Home()
        }
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


        binding.recycler.adapter = listAdapter

        collectNotes()


        fab.setOnClickListener { moveTo() }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

                when (tab?.position) {

                    0 -> {
                        viewModel.allUsers()


                    }

                    1 -> {

                        viewModel.allFavorites()

                    }

                    else -> {

                        viewModel.allUsers()

                    }
                }
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
                TODO("Not yet implemented")
            }

            override fun onTabReselected(p0: TabLayout.Tab?) {
                TODO("Not yet implemented")
            }
        })

    }


    private val tabLayout = binding.tablelayout

    private val tousTab = tabLayout.getTabAt(0)

    private val favorisTab = tabLayout.getTabAt(1)


    private val search = binding.searchBar

    private val fab = binding.addButton


    private fun moveTo() {
        NavHostFragment.findNavController(this@Home)
            .navigate(R.id.action_home_to_add)


    }




    private fun collectNotes() {
        lifecycleScope.launch {
            viewModel.homeAdd.collect {

                listAdapter.updateList(it)


            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

