package hr.kacan.trznica.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import hr.kacan.trznica.App
import hr.kacan.trznica.R
import hr.kacan.trznica.conf.Constants
import hr.kacan.trznica.models.Korisnik
import hr.kacan.trznica.view.login.LoginViewModel
import hr.kacan.trznica.view.login.LoginViewModelFactory
import hr.kacan.trznica.viewmodel.RegisterViewModel

class UserActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var nameEditText: EditText
    private lateinit var surnameEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var townEditText: EditText
    private lateinit var telEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private lateinit var model: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        model = ViewModelProvider(this).get(RegisterViewModel::class.java)
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory()).get(LoginViewModel::class.java)
        nameEditText = findViewById(R.id.ime)
        surnameEditText = findViewById(R.id.prezime)
        townEditText = findViewById(R.id.grad)
        addressEditText = findViewById(R.id.adresa)
        telEditText = findViewById(R.id.tel)
        saveButton = findViewById(R.id.save)
        cancelButton = findViewById(R.id.cancel)
        loadingProgressBar = findViewById(R.id.loading)

        loginViewModel.getLoginFormState().observe(this, Observer { loginFormState ->
            if (loginFormState == null) {
                return@Observer
            }
            saveButton.isEnabled = loginFormState.isDataValid

        })
        val afterTextChangedListener: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                saveButton.isEnabled = nameEditText.text.toString().isNotEmpty() and
                        surnameEditText.text.toString().isNotEmpty() and
                        townEditText.text.toString().isNotEmpty() and
                        addressEditText.text.toString().isNotEmpty() and
                        telEditText.text.toString().isNotEmpty()
            }
        }
        nameEditText.addTextChangedListener(afterTextChangedListener)
        surnameEditText.addTextChangedListener(afterTextChangedListener)
        townEditText.addTextChangedListener(afterTextChangedListener)
        addressEditText.addTextChangedListener(afterTextChangedListener)
        telEditText.addTextChangedListener(afterTextChangedListener)

        saveButton.setOnClickListener {
            loadingProgressBar.visibility = View.VISIBLE
            val korisnik = Korisnik(0, nameEditText.text.toString(), surnameEditText.text.toString(), townEditText.text.toString(), addressEditText.text.toString(), telEditText.text.toString(), App.KORISNIK.email, "")
            model.editKorisnik(korisnik).observe(this@UserActivity, Observer { responseKorisnik ->
                if (responseKorisnik.response == Constants.RESPONSE_SUCCESS) {
                    Toast.makeText(applicationContext, getString(R.string.update_success), Toast.LENGTH_LONG).show()
                    finish()
                } else if (responseKorisnik.response == Constants.RESPONSE_FAIL) {
                    Toast.makeText(applicationContext, getString(R.string.add_fail_msg), Toast.LENGTH_LONG).show()
                }
                loadingProgressBar.visibility = View.INVISIBLE
            })
        }
        cancelButton.setOnClickListener { finish() }
        setData()
    }

    private fun setData() {
        nameEditText.setText(App.KORISNIK.ime)
        surnameEditText.setText(App.KORISNIK.prezime)
        townEditText.setText(App.KORISNIK.grad)
        addressEditText.setText(App.KORISNIK.adresa)
        telEditText.setText(App.KORISNIK.tel)
    }
}