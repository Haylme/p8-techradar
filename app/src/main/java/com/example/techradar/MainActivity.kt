package com.example.techradar

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.techradar.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)


      //  val menuId = R.id.home //

       /** if (savedInstanceState == null) {
            val fragment = getFragmentById(menuId)
            if (fragment != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main, fragment)
                    .commitNow()
            }
        }**/
    }

  /**  private fun getFragmentById(menuId: Int): Fragment? {
        return when (menuId) {
            R.id.add -> Add.newInstance()
            R.id.detail -> Detail.newInstance()
            R.id.edit -> Edit.newInstance()
            R.id.home -> Home.newInstance()
            else -> null
        }
    }**/
}
