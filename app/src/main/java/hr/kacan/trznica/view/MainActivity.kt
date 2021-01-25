package hr.kacan.trznica.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import hr.kacan.trznica.R
import hr.kacan.trznica.viewmodel.PonudaViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var model: PonudaViewModel
    var search: String = ""

    fun getModel(): PonudaViewModel {
        return model
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        model = ViewModelProvider(this).get(PonudaViewModel::class.java)
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