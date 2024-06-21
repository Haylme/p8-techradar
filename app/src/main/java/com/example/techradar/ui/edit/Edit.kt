package com.example.techradar.ui.edit

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.techradar.R
import com.example.techradar.databinding.FragmentEditBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class Edit : Fragment() {

 private var _binding : FragmentEditBinding? = null
 private val binding get() = _binding!!

    private val viewModel: EditViewModel by viewModels()

    companion object {
        @JvmStatic
        fun newInstance() = Edit()
    }





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
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