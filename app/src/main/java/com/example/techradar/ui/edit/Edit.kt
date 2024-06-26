package com.example.techradar.ui.edit

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.techradar.R
import com.example.techradar.databinding.FragmentEditBinding
import com.example.techradar.model.Content
import com.example.techradar.model.SimpleResponse
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.properties.Delegates


fun getDrawableUri(context: Context, drawableId: Int): Uri {
    return Uri.Builder()
        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
        .authority(context.resources.getResourcePackageName(drawableId))
        .appendPath(context.resources.getResourceTypeName(drawableId))
        .appendPath(context.resources.getResourceEntryName(drawableId))
        .build()
}


@AndroidEntryPoint
class Edit : Fragment(R.layout.fragment_edit) {

    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EditViewModel by viewModels()

    private var id by Delegates.notNull<Long>()


    private var imageUri: Uri? = null


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


        val dateButton = binding.calendarButton
        val dateEditText = binding.dateEditText


        // calendar constraint time and year
        val today = MaterialDatePicker.todayInUtcMilliseconds()
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("utc"))

        calendar.timeInMillis = today
        val constraintsBuilder = CalendarConstraints.Builder()
            .setEnd(today)


        dateButton.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(constraintsBuilder.build())
                .setTitleText("Entrer une date")
                .build()


            datePicker.addOnPositiveButtonClickListener { selection ->
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val date = sdf.format(Date(selection))
                dateEditText.setText(date)
            }

            datePicker.show(parentFragmentManager, "Date_picker")

        }


        val avatar = binding.avatar
        val prenom = binding.prenomEditText
        val nom = binding.nomEditText
        val email = binding.mailEditText
        val phone = binding.phoneEditText
        val date = binding.dateEditText
        val wage = binding.wageLayout
        val note = binding.noteEditText
        val button = binding.saveButton


        id = arguments?.getLong("id") ?: 0L

        prenom.setText(arguments?.getString("firstname"))
        nom.setText(arguments?.getString("name"))
        email.setText(arguments?.getString("email"))
        phone.setText(arguments?.getString("phone"))
        date.setText(arguments?.getString("birthday"))
        val wageValue = arguments?.getInt("wage")
        if (wageValue != null) {
            wage.editText?.setText(wageValue.toString())
        }
        note.setText(arguments?.getString("note"))

        avatar.setImageURI(arguments?.getParcelable("pics"))

        val favorite = arguments?.getBoolean("favorite")?: false

        binding.topAppbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_edit_to_detail)
        }


        if (imageUri == null) {
            imageUri = getDrawableUri(
                requireContext(),
                R.drawable.baseline_category_24
            )
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

        avatar.setImageURI(imageUri)

        button.isEnabled = false



        email.addTextChangedListener(object : TextWatcher {

            var current = ""
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                current = s.toString()

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // TODO("Not yet implemented")
            }

            override fun afterTextChanged(s: Editable?) {
                val userMail = s.toString()
                if (s == null) return
                if (userMail == current) return

                val isValidMail = if (viewModel.checkMail(userMail)) true
                else {
                    binding.mailEditText.error = "format d'email invalide"
                    false
                }
                if (isValidMail) current = userMail


            }

        })


        val textwatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
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
                    favorite = favorite,
                    picture = imageUri
                )





                lifecycleScope.launch {
                    viewModel.editAdd.collect { response ->
                        when (response.status) {
                            is SimpleResponse.Status.Success -> {
                                Snackbar.make(
                                    binding.root,
                                    "User added successfully",
                                    Snackbar.LENGTH_LONG
                                )
                                    .show()
                                viewModel.resetToast()
                                NavHostFragment.findNavController(this@Edit)
                                    .navigate(R.id.action_add_to_home)
                            }

                            is SimpleResponse.Status.Failure -> {

                                lifecycleScope.launch {

                                    viewModel.editError.collect { message ->
                                        message?.let {
                                            Snackbar.make(
                                                binding.root,
                                                it,
                                                Snackbar.LENGTH_LONG
                                            )
                                                .show()
                                            viewModel.resetToast()
                                        }
                                    }
                                }

                            }

                            else -> {
                                Snackbar.make(
                                    binding.root,
                                    "An error occurred",
                                    Snackbar.LENGTH_LONG
                                )
                                    .show()
                            }
                        }
                    }
                }



                viewModel.resetAccountValue()

                viewModel.editAccount(content)


            } else {
                Snackbar.make(
                    binding.root,
                    "Please fill all the necessary fields",
                    Snackbar.LENGTH_LONG
                ).show()
            }


        }

        dateEditText.addTextChangedListener(object : TextWatcher {
            private var current = ""
            private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Store the current text before it changes
                current = s.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing here
            }

            override fun afterTextChanged(s: Editable?) {
                if (s == null) return

                val userInput = s.toString()
                if (userInput == current) return

                val cleanInput = userInput.replace("\\D".toRegex(), "")
                val formattedInput = StringBuilder()

                for (i in cleanInput.indices) {
                    formattedInput.append(cleanInput[i])
                    if ((i == 1 || i == 3) && i != cleanInput.length - 1) {
                        formattedInput.append('/')
                    }
                }

                current = formattedInput.toString()
                dateEditText.removeTextChangedListener(this)
                dateEditText.setText(current)
                dateEditText.setSelection(current.length)
                dateEditText.addTextChangedListener(this)

                // Handle error if the format is incorrect
                try {
                    LocalDate.parse(current, formatter)
                } catch (e: Exception) {
                    dateEditText.error = "format de date invalide"
                }
            }
        })


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}