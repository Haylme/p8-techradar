package com.example.techradar.ui.edit

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
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


fun getDrawableUri(context: Context, drawableId: Int): Uri {
    return Uri.Builder().scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
        .authority(context.resources.getResourcePackageName(drawableId))
        .appendPath(context.resources.getResourceTypeName(drawableId))
        .appendPath(context.resources.getResourceEntryName(drawableId)).build()
}


@AndroidEntryPoint
class Edit : Fragment(R.layout.fragment_edit) {

    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EditViewModel by viewModels()

    private var id: Long = 0L


    private var favorite: Boolean = false


    private lateinit var phoneValue: String

    private lateinit var emailValue: String

    private lateinit var birthday: String

    private var wageText: Int = 0


    private lateinit var imageUri: String

    private lateinit var noteText: String
    private lateinit var nameText: String
    private lateinit var firstnameText: String


    private lateinit var textwatcher: TextWatcher


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.topAppbar)


        return binding.root


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val dateButton = binding.calendarButton
        val dateEditText = binding.dateEditText


        val today = MaterialDatePicker.todayInUtcMilliseconds()
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("utc"))

        calendar.timeInMillis = today
        val constraintsBuilder = CalendarConstraints.Builder().setEnd(today)


        dateButton.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(constraintsBuilder.build()).setTitleText("Entrer une date")
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
        val wage = binding.wageLayout
        val note = binding.noteEditText
        val button = binding.saveButton


        id = arguments?.getLong("id") ?: 0L

        phoneValue = arguments?.getString("phone").toString()
        emailValue = arguments?.getString("email").toString()
        birthday = arguments?.getString("birthday") ?: ""
        wageText = arguments?.getInt("wage") ?: 0
        imageUri = arguments?.getString("pics").toString()
        noteText = arguments?.getString("note").toString()
        nameText = arguments?.getString("name").toString()
        firstnameText = arguments?.getString("firstname").toString()
        favorite = arguments?.getBoolean("favorite") ?: false



        prenom.setText(firstnameText)
        nom.setText(nameText)
        email.setText(emailValue)
        phone.setText(phoneValue)
        dateEditText.setText(birthday)

        wage.editText?.setText(wageText.toString())
        note.setText(noteText)

        imageUri = arguments?.getString("pics").toString()
        bindAvatar(binding.avatar, imageUri)




        if (imageUri.isEmpty()) {
            imageUri = getDrawable(
                requireContext(), R.drawable.baseline_category_24
            ).toString()
        }


        val imageContract = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                imageUri = uri.toString()

                bindAvatar(avatar, imageUri)



                textwatcher.afterTextChanged(null)

            }
        }

        avatar.setOnClickListener {
            imageContract.launch("image/*")
        }

        // imageUri = arguments?.getString("picture").toString()
        bindAvatar(binding.avatar, imageUri)


        // button.isEnabled = false


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


        textwatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?, start: Int, count: Int, after: Int
            ) {
                //TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


                nom.error = null
                prenom.error = null
                phone.error = null
                email.error = null
                dateEditText.error = null
                wage.editText?.error = null
                note.error = null


            }

            override fun afterTextChanged(s: Editable?) {

                viewModel.checkField(
                    nom.text?.trim().toString(),
                    prenom.text?.trim().toString(),
                    phone.text?.trim().toString(),
                    email.text?.trim().toString(),
                    dateEditText.text?.trim().toString(),
                    wage.editText?.text?.trim().toString().toIntOrNull() ?: 0,
                    note.text?.trim().toString(),
                    imageUri
                )


            }
        }

        nom.removeTextChangedListener(textwatcher)
        prenom.removeTextChangedListener(textwatcher)
        phone.removeTextChangedListener(textwatcher)
        email.removeTextChangedListener(textwatcher)
        dateEditText.removeTextChangedListener(textwatcher)
        wage.editText?.removeTextChangedListener(textwatcher)
        note.removeTextChangedListener(textwatcher)


        nom.addTextChangedListener(textwatcher)
        prenom.addTextChangedListener(textwatcher)
        phone.addTextChangedListener(textwatcher)
        email.addTextChangedListener(textwatcher)
        dateEditText.addTextChangedListener(textwatcher)
        wage.editText?.addTextChangedListener(textwatcher)
        note.addTextChangedListener(textwatcher)


        var bool = false



        button.setOnClickListener {

            val nameCheck = nom.text?.trim().toString()
            val firstnameCheck = prenom.text?.trim().toString()
            val phoneCheck = phone.text?.trim().toString()
            val emailCheck = email.text?.trim().toString()
            val dateCheck = dateEditText.text?.trim().toString()
            val wageCheck = wage.editText?.text?.trim().toString()
            val noteCheck = note.text?.trim().toString()
            val imageUriCheck = imageUri

            val isValid = viewModel.checkField(
                nameCheck,
                firstnameCheck,
                phoneCheck,
                emailCheck,
                dateCheck,
                wageCheck.toIntOrNull() ?: 0,
                noteCheck,
                imageUriCheck
            )

            val hasChanges = (
                    phoneValue != phoneCheck ||
                            emailValue != emailCheck ||
                            birthday != dateCheck ||
                            wageText != (wageCheck.toIntOrNull() ?: 0) ||
                            imageUri != imageUriCheck ||
                            noteText != noteCheck ||
                            nameText != nameCheck ||
                            firstnameText != firstnameCheck
                    )

            if (!isValid || !hasChanges ) {
                if (nom.text?.trim().isNullOrEmpty() || nom.text?.trim().toString() == nameText) {
                    nom.error = getString(R.string.champ_obligatoire)
                }

                if (prenom.text?.trim().isNullOrEmpty() || prenom.text?.trim() == firstnameText) {
                    prenom.error = getString(R.string.champ_obligatoire)
                }

                if (phone.text?.trim().isNullOrEmpty() || phone.text?.trim() == phoneValue) {
                    phone.error = getString(R.string.champ_obligatoire)
                }

                if (email.text?.trim().isNullOrEmpty() || email.text?.trim() == emailValue) {
                    email.error = getString(R.string.champ_obligatoire)
                }

                if (dateEditText.text?.trim()
                        .isNullOrEmpty() || dateEditText.text?.trim() == birthday
                ) {
                    dateEditText.error = getString(R.string.champ_obligatoire)
                }

                if (wage.editText?.text?.trim().isNullOrEmpty() || wage.editText?.text?.trim()
                        .toString().toIntOrNull() == wageText
                ) {
                    wage.editText?.error = getString(R.string.champ_obligatoire)
                }

                if (note.text?.trim().isNullOrEmpty() || note.text?.trim() == noteText) {
                    note.error = getString(R.string.champ_obligatoire)
                }

                Toast.makeText(
                    requireContext(),
                    getString(R.string.veuillez_remplir_au_moins_un_champ),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                val content = Content(
                    id = id,
                    name = nom.text?.trim().toString(),
                    firstname = prenom.text?.trim().toString(),
                    phone = phone.text?.trim().toString(),
                    email = email.text?.trim().toString(),
                    birthday = dateEditText.text?.trim().toString(),
                    wage = wage.editText?.text?.trim().toString().toIntOrNull() ?: 0,
                    note = note.text?.trim().toString(),
                    favorite = favorite,
                    picture = imageUri.toString()
                )

                lifecycleScope.launch {
                    viewModel.editAdd.collect { response ->
                        when (response.status) {
                            is SimpleResponse.Status.Success -> {
                                Snackbar.make(
                                    binding.root, "Données mis à jour", Snackbar.LENGTH_LONG
                                ).show()
                                bool = true
                                viewModel.resetToast()

                                nameText = nom.text?.trim().toString()
                                firstnameText = prenom.text?.trim().toString()
                                phoneValue = phone.text?.trim().toString()
                                emailValue = email.text?.trim().toString()
                                birthday = dateEditText.text?.trim().toString()
                                wageText = wage.editText?.text?.trim().toString().toIntOrNull() ?: 0
                                noteText = note.text?.trim().toString()
                                imageUri


                            }

                            is SimpleResponse.Status.Failure -> {
                                viewModel.editError.collect { message ->
                                    message?.let {
                                        Snackbar.make(
                                            binding.root, it, Snackbar.LENGTH_LONG
                                        ).show()
                                        bool = false
                                        viewModel.resetToast()

                                    }
                                }
                            }

                            else -> {
                                Snackbar.make(
                                    binding.root, "An error occurred", Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
                viewModel.editAccount(content, id)
                viewModel.resetAccountValue()
            }
        }









        dateEditText.addTextChangedListener(object : TextWatcher {
            private var current = ""
            private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                current = s.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

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


                try {
                    LocalDate.parse(current, formatter)
                } catch (e: Exception) {
                    dateEditText.error = "format de date invalide"
                }
            }
        })



        binding.topAppbar.setNavigationOnClickListener {

            if (!bool) {

                val resultBundle = Bundle().apply {
                    putLong("id", id)
                    putString("name", nameText)
                    putString("firstname", firstnameText)
                    putString("phone", phoneValue)
                    putString("email", emailValue)
                    putInt("wage", wageText)
                    putString("note", noteText)
                    putString("pics", imageUri)
                    putString("birthday", birthday)
                    putBoolean("favorite", favorite)
                }
                parentFragmentManager.setFragmentResult("editResult", resultBundle)
                findNavController().navigate(R.id.action_edit_to_detail, resultBundle)


            } else {

                val resultBundle = Bundle().apply {
                    putLong("id", id)
                    putString("name", nom.text?.trim().toString())
                    putString("firstname", prenom.text?.trim().toString())
                    putString("phone", phone.text?.trim().toString())
                    putString("email", email.text?.trim().toString())
                    putInt("wage", wage.editText?.text?.trim().toString().toIntOrNull() ?: 0)
                    putString("note", note.text?.trim().toString())
                    putString("pics", imageUri)
                    putString("birthday", dateEditText.text?.trim().toString())
                    putBoolean("favorite", favorite)
                }
                parentFragmentManager.setFragmentResult("editResult", resultBundle)
                findNavController().navigate(R.id.action_edit_to_detail)


            }


        }


    }


    private fun bindAvatar(avatar: ImageView, str: String) {
        val uri = Uri.parse((str))
        Glide.with(avatar.context)
            .load(uri)
            .into(avatar)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}