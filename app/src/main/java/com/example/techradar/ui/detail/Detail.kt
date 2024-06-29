package com.example.techradar.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.techradar.R
import com.example.techradar.databinding.FragmentDetailBinding
import com.example.techradar.model.Content
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.properties.Delegates


@AndroidEntryPoint
class Detail : Fragment(R.layout.fragment_detail) {

    private var _binding: FragmentDetailBinding? = null

    private val binding get() = _binding!!


    private var id: Long = 0L


    private var favorite by Delegates.notNull<Boolean>()


    private val viewModel: DetailViewModel by viewModels()


    private lateinit var phoneValue: String

    private lateinit var emailValue: String

    private lateinit var birthday: String

    private var wage by Delegates.notNull<Int>()

    // private lateinit var note: String

    private lateinit var pictureUri: Uri

    private lateinit var noteText: String
    private lateinit var nameText: String
    private lateinit var firstnameText: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)


        (activity as? AppCompatActivity)?.setSupportActionBar(binding.topAppbar)


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)





        id = arguments?.getLong("id") ?: 0L




        favorite = arguments?.getBoolean("favorite") ?: false

        nameText = arguments?.getString("name") ?: ""

        firstnameText = arguments?.getString("firstname") ?: ""

        noteText = arguments?.getString("note") ?: ""

        birthday = arguments?.getString("birthday").toString()

        wage = arguments?.getInt("wage") ?: 0

        phoneValue = arguments?.getString("phone").toString()
        emailValue = arguments?.getString(("email")).toString()

        pictureUri = arguments?.


        val avatar = binding.avatar
        val callButton = binding.call
        val smsButton = binding.message
        val mailButton = binding.mailButton
        val about = binding.about
        val wageText = binding.wage
        val note = binding.noteEditText
        val name = binding.name
        val firstname = binding.firstname


        bindAvatar(avatar,pictureUri)


      //  pictureUri.let {
       //     avatar.setImageURI(it)
       // }


        val backBar = binding.back

        name.text = nameText

        firstname.text = firstnameText

        note.text = noteText

        backBar.setOnClickListener {

            findNavController().navigate(R.id.action_detail_to_home)

        }


        //  userId.let {
        //      viewModel.detailUser(it)
        //     fetchDetail()
        //  }


        wageText.text = "${wage.toString()} €"

        about.text = birthday

        if (birthday.isNotEmpty()) {
            try {
                val sdf = SimpleDateFormat("dd/MM/yyyy")
                val date = sdf.parse(birthday)
                val currentDate = Calendar.getInstance()

                date?.let {
                    val birthdayCalendar = Calendar.getInstance().apply { time = it }
                    var age = currentDate.get(Calendar.YEAR) - birthdayCalendar.get(Calendar.YEAR)
                    if (currentDate.get(Calendar.DAY_OF_YEAR) < birthdayCalendar.get(Calendar.DAY_OF_YEAR)) {
                        age--
                    }
                    about.text = "$birthday $age ans"
                }
            } catch (e: Exception) {
                // Toast.makeText(requireContext(), "format de date incorrecte", Toast.LENGTH_LONG)
                //     .show()
            }
        } else {
            Toast.makeText(
                requireContext(),
                "Aucune date de naissance renseignée",
                Toast.LENGTH_LONG
            ).show()
        }


        val nameSize = name.text.length
        val firstnameSize = firstname.text.length

        if (nameSize + firstnameSize > 6) {

            name.textSize = 16f
            firstname.textSize = 16f

        }


        callButton.setOnClickListener {

            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneValue"))
            startActivity(intent)

        }

        smsButton.setOnClickListener {

            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:$phoneValue"))
            startActivity(intent)

        }

        mailButton.setOnClickListener {

            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto: $emailValue"))
            startActivity(intent)

        }




        parentFragmentManager.setFragmentResultListener("editResult", this) { _, bundle ->
            id = bundle.getLong("id")
            nameText = bundle.getString("name") ?: ""
            firstnameText = bundle.getString("firstname") ?: ""
            phoneValue = bundle.getString("phone") ?: ""
            emailValue = bundle.getString("email") ?: ""
            wage = bundle.getInt("wage")
            noteText = bundle.getString("note") ?: ""
            pictureUri = bundle.getParcelable("pics") ?: Uri.EMPTY
            birthday = bundle.getString("birthday") ?: ""
            favorite = bundle.getBoolean("favorite")


            binding.name.text = nameText
            binding.firstname.text = firstnameText
            // binding.phoneEditText.setText(phoneValue)
            //binding.mailEditText.setText(emailValue)
            binding.wage.text = "${wage.toString()} €"
            binding.noteEditText.setText(noteText)

            binding.about.text = birthday







            if (birthday.isNotEmpty()) {
                try {
                    val sdf = SimpleDateFormat("dd/MM/yyyy")
                    val date = sdf.parse(birthday)
                    val currentDate = Calendar.getInstance()

                    date?.let {
                        val birthdayCalendar = Calendar.getInstance().apply { time = it }
                        var age =
                            currentDate.get(Calendar.YEAR) - birthdayCalendar.get(Calendar.YEAR)
                        if (currentDate.get(Calendar.DAY_OF_YEAR) < birthdayCalendar.get(Calendar.DAY_OF_YEAR)) {
                            age--
                        }
                        about.text = "$birthday $age ans"
                    }
                } catch (e: Exception) {
                    //  Toast.makeText(requireContext(), "format de date incorrecte", Toast.LENGTH_LONG)
                    // .show()
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Aucune date de naissance renseignée",
                    Toast.LENGTH_LONG
                ).show()
            }
        }


    }

    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "inflater.inflate(R.menu.appbar_menu, menu)",
            "com.example.techradar.R"
        )
    )
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {


        inflater.inflate(R.menu.appbar_menu, menu)

        val favItem = menu.findItem(R.id.fav)
        updateIcon(favItem, favorite)


    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.fav -> {

                if (favorite) {
                    val bool = false
                    favorite = bool

                    updateIcon(item, favorite)
                    viewModel.updateData(id, favorite)

                } else {
                    val bool = true
                    favorite = bool
                    updateIcon(item, favorite)
                    viewModel.updateData(id, favorite)

                }


            }

            R.id.edit_button -> {
                val bundle = Bundle().apply {
                    putLong("id", id)
                    putString("name", nameText)
                    putString("firstname", firstnameText)
                    putString("phone", phoneValue)
                    putString("email", emailValue)
                    putInt("wage", wage)
                    putString("note", noteText)
                    putParcelable("pics", pictureUri)
                    putString("birthday", birthday)
                    putBoolean("favorite", favorite)
                }

                findNavController().navigate(R.id.action_detail_to_edit, bundle)
            }

            R.id.supp -> {


                context?.let {
                    MaterialAlertDialogBuilder(it)
                        .setTitle("Suppression")
                        .setMessage("Etes-vous certain de vouloir supprimer ce candidat ? Cette action est irréversible")
                        .setNegativeButton("Annuler") { dialog, _ ->
                            dialog.cancel()
                        }
                        .setPositiveButton("Confirmer") { dialog_, _ ->
                            dialog_.apply {
                                viewModel.deleteCandidate(id)
                                Log.d("error", "onOptionsItemSelected:$id ")
                                findNavController().navigate(R.id.action_detail_to_home)
                            }

                        }
                }
                    ?.show()
            }
        }

        return true
    }



    private fun updateIcon(menuItem: MenuItem, isFavorite: Boolean) {
        if ((isFavorite)) {
            menuItem.setIcon(R.drawable.baseline_star_24)

        } else {

            menuItem.setIcon(R.drawable.favorite_foreground)

        }


    }

  private  fun bindAvatar(avatar: ImageView, uri:Uri) {
        Glide.with(avatar.context)
            .load(uri)
            .into(avatar)
    }






    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}