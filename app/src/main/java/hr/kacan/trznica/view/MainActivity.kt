package hr.kacan.trznica.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import hr.kacan.trznica.R

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    var search: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        search = intent?.getStringExtra("search") ?: ""
        read()
    }

    fun read() {
        setFragment(PonudaFragment())
    }

    fun cud() {
        setFragment(CUDFragment())
    }

    private fun setFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }
}