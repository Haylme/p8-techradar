package com.example.techradar.ui.add

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.techradar.R
import com.example.techradar.data.DataRepository
import com.example.techradar.databinding.FragmentAddBinding
import com.example.techradar.model.Content
import com.example.techradar.model.SimpleResponse
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class Add : Fragment() {


    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddViewModel by viewModels()


    private var imageUri: Uri? = null

    companion object {

        @JvmStatic
        fun newInstance() = Add()


    }


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


        val avatar = binding.avatar
        val prenom = binding.prenomEditText
        val nom = binding.nomEditText
        val email = binding.mailEditText
        val phone = binding.phoneEditText
        val date = binding.dateEditText
        val wage = binding.wageLayout
        val note = binding.noteEditText
        val button = binding.saveButton




        binding.topAppbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_add_to_home)
        }



        val imageContract =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                if (uri != null) {
                    imageUri = uri
                    avatar.setImageURI(uri)
                }
            }

        avatar.setOnClickListener {
            imageContract.launch("image/*")
        }

        button.isEnabled = false


        val textwatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //TODO("Not yet implemented")
            }

            override fun afterTextChanged(s: Editable?) {
                button.isEnabled = viewModel.checkField(
                    nom.text?.trim().toString(),
                    prenom.text?.trim().toString(),
                    phone.text?.trim().toString(),
                    email.text?.trim().toString(),
                    date.text?.trim().toString(),
                    wage.editText?.text?.trim().toString().toIntOrNull() ?: 0,
                    note.text?.trim().toString()
                )


            }
        }


        nom.addTextChangedListener(textwatcher)
        prenom.addTextChangedListener(textwatcher)
        phone.addTextChangedListener(textwatcher)
        email.addTextChangedListener(textwatcher)
        date.addTextChangedListener(textwatcher)
        wage.editText?.addTextChangedListener(textwatcher)
        note.addTextChangedListener(textwatcher)







        button.setOnClickListener {

            if (viewModel.checkField(
                    nom.text?.trim().toString(),
                    prenom.text?.trim().toString(),
                    phone.text?.trim().toString(),
                    email.text?.trim().toString(),
                    date.text?.trim().toString(),
                    wage.editText?.text?.trim().toString().toIntOrNull() ?: 0,
                    note.text?.trim().toString()
                )
            ) {
                val content = Content(

                    name = nom.text?.trim().toString(),
                    firstname = prenom.text?.trim().toString(),
                    phone = phone.text?.trim().toString(),
                    email = email.text?.trim().toString(),
                    birthday = date.text?.trim().toString(),
                    wage = wage.editText?.text?.trim().toString().toIntOrNull() ?: 0,
                    note = note.text?.trim().toString(),
                    favorite = false, // Default value for new content
                    picture = imageUri
                )






                viewModel.resetAccountValue()

                viewModel.addNewUser(content)


            } else {
                Snackbar.make(
                    binding.root,
                    "Please fill all the necessary fields",
                    Snackbar.LENGTH_LONG
                ).show()
            }


        }



        lifecycleScope.launch {
            viewModel.userAdd.collect { response ->
                when (response.status) {
                    is SimpleResponse.Status.Success -> {
                        Snackbar.make(binding.root, "User added successfully", Snackbar.LENGTH_LONG)
                            .show()
                        viewModel.resetToast()
                        NavHostFragment.findNavController(this@Add)
                            .navigate(R.id.action_detail_to_home)
                    }

                    is SimpleResponse.Status.Failure -> {

                        lifecycleScope.launch {

                            viewModel.error.collect { message ->
                                message?.let {
                                    Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                                    viewModel.resetToast()
                                }
                            }
                        }

                    }

                    else -> {
                        Snackbar.make(binding.root, "An error occurred", Snackbar.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}