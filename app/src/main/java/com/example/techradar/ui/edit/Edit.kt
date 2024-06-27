package com.example.techradar.ui.edit

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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

    private var id: Long = 0L




    private var favorite: Boolean = false


    private lateinit var phoneValue: String

    private lateinit var emailValue: String

    private lateinit var birthday: String

    private var wageText :Int = 0


    private lateinit var imageUri: Uri

    private lateinit var noteText: String
    private lateinit var nameText: String
    private lateinit var firstnameText: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.topAppbar)


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
        //val date = binding.dateEditText
        val wage = binding.wageLayout
        val note = binding.noteEditText
        val button = binding.saveButton


        id = arguments?.getLong("id") ?: 0L

        phoneValue = arguments?.getString("phone").toString()
        emailValue = arguments?.getString("email").toString()
        birthday = arguments?.getString("birthday") ?: ""
        wageText = arguments?.getInt("wage") ?: 0
        imageUri = arguments?.getParcelable<Uri>("pics") ?: Uri.EMPTY
        noteText = arguments?.getString("note").toString()
        nameText = arguments?.getString("name").toString()
        firstnameText = arguments?.getString("firstname").toString()
        favorite = arguments?.getBoolean("favorite") ?: false



        prenom.setText(firstnameText)
        nom.setText(nameText)
        email.setText(emailValue)
        phone.setText(phoneValue)
        dateEditText.setText(birthday)

        wage.editText?.setText("${wageText.toString()} €")
        note.setText(noteText)

        //  avatar.setImageURI(arguments?.getParcelable("pics"))


        // val favorite = arguments?.getBoolean("favorite") ?: false


        if (imageUri == Uri.EMPTY) {
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
                    dateEditText.text?.trim().toString(),
                    wage.editText?.text?.trim().toString().toIntOrNull() ?: 0,
                    note.text?.trim().toString(),
                    imageUri

                )
                avatar.setImageURI(imageUri)

            }
        }


        nom.addTextChangedListener(textwatcher)
        prenom.addTextChangedListener(textwatcher)
        phone.addTextChangedListener(textwatcher)
        email.addTextChangedListener(textwatcher)
        dateEditText.addTextChangedListener(textwatcher)
        wage.editText?.addTextChangedListener(textwatcher)
        note.addTextChangedListener(textwatcher)




        fun setupButton():Boolean {
            button.setOnClickListener {
                val content = Content(
                    name = nom.text?.trim().toString(),
                    firstname = prenom.text?.trim().toString(),
                    phone = phone.text?.trim().toString(),
                    email = email.text?.trim().toString(),
                    birthday = dateEditText.text?.trim().toString(),
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
                                    "Données mis à jour",
                                    Snackbar.LENGTH_LONG
                                ).show()
                                viewModel.resetToast()
                                NavHostFragment.findNavController(this@Edit)
                                    .navigate(R.id.action_add_to_home)
                            }

                            is SimpleResponse.Status.Failure -> {
                                viewModel.editError.collect { message ->
                                    message?.let {
                                        Snackbar.make(
                                            binding.root,
                                            it,
                                            Snackbar.LENGTH_LONG
                                        ).show()
                                        viewModel.resetToast()
                                    }
                                }
                            }

                            else -> {
                                Snackbar.make(
                                    binding.root,
                                    "An error occurred",
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }

                viewModel.resetAccountValue()
                viewModel.editAccount(content)
                button.isEnabled = false
            }
            return true
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

            if(!setupButton()){

                val resultBundle = Bundle().apply {
                    putLong("id", id)
                    putString("name", nameText)
                    putString("firstname",firstnameText)
                    putString("phone", phoneValue)
                    putString("email", emailValue)
                    putInt("wage", wageText)
                    putString("note", noteText)
                    putParcelable("pics", imageUri)
                    putString("birthday", birthday)
                    putBoolean("favorite", favorite)
                }
                parentFragmentManager.setFragmentResult("editResult", resultBundle)
                findNavController().navigate(R.id.action_edit_to_detail,resultBundle)


            }else {

                val resultBundle = Bundle().apply {
                    putLong("id", id)
                    putString("name", nom.text?.trim().toString())
                    putString("firstname", prenom.text?.trim().toString())
                    putString("phone", phone.text?.trim().toString())
                    putString("email", email.text?.trim().toString())
                    putInt("wage", wage.editText?.text?.trim().toString().toIntOrNull() ?: 0)
                    putString("note", note.text?.trim().toString())
                    putParcelable("pics", imageUri)
                    putString("birthday", dateEditText.text?.trim().toString())
                    putBoolean("favorite", favorite)
                }
                parentFragmentManager.setFragmentResult("editResult", resultBundle)
                findNavController().navigate(R.id.action_edit_to_detail)


            }


        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}