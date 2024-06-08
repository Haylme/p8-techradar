package com.example.techradar.ui.add

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.techradar.R
import com.example.techradar.data.DataRepository
import com.example.techradar.databinding.FragmentAddBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class Add : Fragment() {




    private var _binding: FragmentAddBinding? = null
   private val binding get() = _binding!!

    private val viewModel: AddViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentAddBinding.inflate(inflater, container, false)
       return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}