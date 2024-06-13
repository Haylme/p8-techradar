package com.example.techradar

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.techradar.databinding.ActivityMainBinding
import com.example.techradar.ui.add.Add
import com.example.techradar.ui.detail.Detail
import com.example.techradar.ui.edit.Edit
import com.example.techradar.ui.home.Home
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        // Assuming you have a menuId defined somewhere in your code
        val menuId = R.id.home // Replace this with the actual menuId you want to use

        if (savedInstanceState == null) {
            val fragment = getFragmentById(menuId)
            if (fragment != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main, fragment)
                    .commitNow()
            }
        }
    }

    private fun getFragmentById(menuId: Int): Fragment? {
        return when (menuId) {
            R.id.add -> Add.newInstance()
            R.id.detail -> Detail.newInstance()
            R.id.edit -> Edit.newInstance()
            R.id.home -> Home.newInstance()
            else -> null
        }
    }
}
