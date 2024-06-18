package com.example.techradar.ui.detail

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.load.engine.Resource
import com.example.techradar.R
import com.example.techradar.databinding.FragmentDetailBinding
import com.example.techradar.databinding.FragmentHomeBinding
import com.example.techradar.model.SimpleResponse
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class Detail : Fragment(R.layout.fragment_detail) {

    private var _binding: FragmentDetailBinding? = null

    private val binding get() = _binding!!

    companion object {

        @JvmStatic
        fun newInstance() = Detail()
    }


    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val backBar = binding.back
        val avatar = binding.avatar
        val call = binding.call
       val sms = binding.message
        val mail = binding.mailButton
        val


        backBar.setOnClickListener {

            findNavController().navigate(R.id.action_detail_to_home)

        }


        val userId = arguments?.getString("id")?.toLongOrNull()

        userId?.let {
            viewModel.detailUser(it)
            fetchDetail(it)
        }

    }


    private fun fetchDetail(userId: Long) {

        lifecycleScope.launch {
            viewModel.detailAdd.collect { response ->
                when (response.status) {

                    is SimpleResponse.Status.Success -> {

                        return@collect

                    }

                    is SimpleResponse.Status.Failure -> {

                        lifecycleScope.launch {

                            viewModel.detailError.collect {

                                    message ->

                                message?.let {
                                    Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
                                        .show()
                                    viewModel.resetError()
                                }

                            }

                        }
                    }

                    else -> {}
                }


            }


        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
