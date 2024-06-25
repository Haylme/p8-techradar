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


    override fun onCreate(savedInstanceState: Bundle?) {
       // checkPermissions()

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
            "id" to content.id,
            "name" to content.name,
            "firstname" to content.firstname,
            "phone" to content.phone,
            "email" to content.email,
            "birthday" to content.birthday,
            "wage" to content.wage,
            "note" to content.note,
            "favorite" to content.favorite,
            "picture" to content.picture

        )
        findNavController().navigate(R.id.action_home_to_detail, bundle)
    }





  /**  private fun checkPermissions() {
        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(requireContext(), it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(requireActivity(), permissionsToRequest.toTypedArray(), PERMISSION_REQUEST_CODE)
        }
    }

   @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // Permissions granted
            } else {
                // Permissions denied, show a message to the user
                Snackbar.make(binding.root, "Permissions are required to access files.", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1001
    }**/




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
