package hr.kacan.trznica.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import hr.kacan.trznica.App
import hr.kacan.trznica.R
import hr.kacan.trznica.conf.Constants
import hr.kacan.trznica.models.Korisnik
import hr.kacan.trznica.models.ResponseKorisnik
import hr.kacan.trznica.view.login.LoginViewModel
import hr.kacan.trznica.viewmodel.KorisnikViewModel

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private val model: KorisnikViewModel by viewModels()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        usernameEditText = findViewById(R.id.username)
        passwordEditText = findViewById(R.id.password)
        //passwordEditText.setText("lozinka")
        loginButton = findViewById(R.id.login)
        registerButton = findViewById(R.id.register)
        loadingProgressBar = findViewById(R.id.loading)

        loginViewModel.getLoginFormState().observe(this, Observer { loginFormState ->
            if (loginFormState == null) {
                return@Observer
            }
            //loginButton.isEnabled = loginFormState.isDataValid

            if (loginFormState.usernameError != 0) {
                usernameEditText.error = getString(loginFormState.usernameError)
            } else {
                usernameEditText.error = null
            }
            if (loginFormState.passwordError != 0) {
                passwordEditText.error = getString(loginFormState.passwordError)
            } else {
                passwordEditText.error = null
            }
        })

        val afterTextChangedListener: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable?) {
                loginViewModel.loginDataChanged(usernameEditText.text.toString(),
                        passwordEditText.text.toString())
            }
        }
        usernameEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                model.loginKorisnik(Korisnik(usernameEditText.text.toString(), passwordEditText.text.toString()))
                loginObserver()
            }
            false
        }
        loginButton.setOnClickListener {
            loadingProgressBar.visibility = View.VISIBLE
            model.loginKorisnik(Korisnik(usernameEditText.text.toString(), passwordEditText.text.toString()))
            loginObserver()
        }
        registerButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
        getUserData()
    }

    private fun updateUiWithUser(responseKorisnik: ResponseKorisnik) {
        val welcome = getString(R.string.welcome) + (responseKorisnik.korisnik.ime)
        Toast.makeText(applicationContext, welcome, Toast.LENGTH_LONG).show()
        val intent = Intent(this@LoginActivity, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
        passwordEditText.setText("")
        usernameEditText.requestFocus()
    }

    private fun saveUserData(korisnik: Korisnik) {
        val prefs = getSharedPreferences("UserData", 0)
        val edit = prefs.edit()
        edit.putString("username", korisnik.email)
        edit.apply()
    }

    private fun getUserData() {
        val prefs = getSharedPreferences("UserData", 0)
        val userName = prefs.getString("username", "")
        if (userName?.isNotEmpty() == true) usernameEditText.setText(userName)
    }

    private fun loginObserver(){
        model.responseKorisnik.observe(this@LoginActivity, { responseKorisnik ->
            loadingProgressBar.visibility = View.GONE
            println(Gson().toJson(responseKorisnik))
            if (responseKorisnik.response == Constants.RESPONSE_FAIL_USN) {
                showLoginFailed(R.string.login_failed_usn)
            } else if (responseKorisnik.response == Constants.RESPONSE_FAIL_PWD) {
                showLoginFailed(R.string.login_failed_pwd)
            }
            if (responseKorisnik.response == Constants.RESPONSE_SUCCESS) {
                App.KORISNIK = responseKorisnik.korisnik
                updateUiWithUser(responseKorisnik)
                saveUserData(responseKorisnik.korisnik)
            }
        })
    }
}