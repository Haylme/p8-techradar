package com.example.techradar.ui.detail

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.techradar.R
import com.example.techradar.databinding.FragmentDetailBinding
import com.example.techradar.model.SimpleResponse
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.properties.Delegates


@AndroidEntryPoint
class Detail : Fragment(R.layout.fragment_detail) {

    private var _binding: FragmentDetailBinding? = null

    private val binding get() = _binding!!


    private var favorite = arguments?.getBoolean("favorite") ?: false

    private var userId by Delegates.notNull<Long>()

    private val viewModel: DetailViewModel by viewModels()

    private lateinit var name: String

    private lateinit var firstname: String

    private lateinit var phoneValue: String

    private lateinit var emailValue: String

    private lateinit var birthday: String

    private var wage by Delegates.notNull<Int>()

    private lateinit var note: String

    private lateinit var pictureUri: Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)


        (activity as AppCompatActivity).setSupportActionBar(binding.topAppbar)


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //onOptionsMenuSelected()


        val avatar = binding.avatar
        val callButton = binding.call
        val smsButton = binding.message
        val mailButton = binding.mailButton
        val about = binding.about
        val wageText = binding.wage
        val note = binding.noteEditText
        val name = binding.name
        val firstname = binding.firstname


        val backBar = binding.back



        backBar.setOnClickListener {

            findNavController().navigate(R.id.action_detail_to_home)

        }


        //val userId = arguments?.getString("id")?.toLongOrNull()

        userId.let {
            viewModel.detailUser(it)
            fetchDetail()
        }

         userId = arguments?.getLong("Id") ?: 0
        name.text = arguments?.getString("name")

        firstname.text = arguments?.getString("firstname")

        note.text = arguments?.getString("note")


        wage = arguments?.getInt("wage") ?: 0
        wageText.text = "${wage.toString()} €"

        about.text = arguments?.getString("note")


        birthday = arguments?.getString("birthday").toString()


        pictureUri = arguments?.getParcelable("pictureUri") ?: Uri.EMPTY

        pictureUri.let {
            binding.avatar.setImageURI(it)
        }


         phoneValue = arguments?.getInt("phone").toString()
         emailValue = arguments?.getString(("email")).toString()


        val nameSize = name.text.length
        val firstnameSize = firstname.text.length

        if (nameSize + firstnameSize > 6) {

            name.textSize = 16f
            firstname.textSize = 16f

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


    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.fav -> {
                if(favorite){
                    val bool = false
                    favorite = bool

                    updateIcon(item,favorite)
                    viewModel.updateData(userId, favorite)

                }else{
                    val bool = true
                    favorite = bool
                    updateIcon(item, favorite)
                    viewModel.updateData(userId, favorite)

                }


            }

            R.id.edit_button -> {
                val bundle = Bundle().apply {
                    putLong("id", userId)
                    putString("name", name)
                    putString("firstname", firstname)
                    putString("phone", phoneValue)
                    putString("email", emailValue)
                    putInt("wage", wage)
                    putString("note", note)
                    putParcelable("pics", pictureUri)
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
                                viewModel.deleteCandidate(userId)
                                Log.d("error", "onOptionsItemSelected:$userId ")
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
            menuItem.setIcon(R.drawable.star_full)

        } else {

            menuItem.setIcon(R.drawable.favorite_foreground)

        }


    }

    private fun fetchDetail() {

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
                                    Snackbar.make(
                                        binding.root,
                                        message,
                                        Snackbar.LENGTH_SHORT
                                    )
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