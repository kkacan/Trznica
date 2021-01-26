package hr.kacan.trznica.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import hr.kacan.trznica.R
import hr.kacan.trznica.conf.Constants
import hr.kacan.trznica.models.Korisnik
import hr.kacan.trznica.view.login.LoggedInUser
import hr.kacan.trznica.view.login.LoginResult
import hr.kacan.trznica.view.login.LoginViewModelFactory
import hr.kacan.trznica.view.login.Result
import hr.kacan.trznica.viewmodel.LoginViewModel


class LoginActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory()).get(LoginViewModel::class.java)
        usernameEditText = findViewById(R.id.username)
        passwordEditText = findViewById(R.id.password)
        passwordEditText.setText("lozinka")
        loginButton = findViewById(R.id.login)
        registerButton = findViewById(R.id.register)
        loadingProgressBar = findViewById(R.id.loading)

        loginViewModel.getLoginResult().observe(this, Observer { loginResult ->
            if (loginResult == null) {
                return@Observer
            }
            loadingProgressBar.visibility = View.GONE

            if (loginResult.error != 0) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success.displayName != "") {
                updateUiWithUser(loginResult.success)
            }

            setResult(RESULT_OK)

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
                loginViewModel.login(usernameEditText.text.toString(), passwordEditText.text.toString())
                        .observe(this@LoginActivity, Observer { result ->
                            if (result is Result.Success<*>) {
                                val data = (result as Result.Success<*>).getData()
                                loginViewModel.getLoginResult().setValue(LoginResult(LoggedInUser(data!!.ime), 0))
                            } else {
                                when ((result as Result.Error).getError()?.message.toString()) {
                                    Constants.RESPONSE_FAIL_PWD ->
                                        loginViewModel.getLoginResult().setValue(LoginResult(LoggedInUser(""), R.string.login_failed_pwd))
                                    Constants.RESPONSE_FAIL_USN ->
                                        loginViewModel.getLoginResult().setValue(LoginResult(LoggedInUser(""), R.string.login_failed_usn))
                                    else ->
                                        loginViewModel.getLoginResult().setValue(LoginResult(LoggedInUser(""), R.string.login_failed))
                                }
                            }
                        })
            }
            false
        }
        loginButton.setOnClickListener {
            loadingProgressBar.visibility = View.VISIBLE


            loginViewModel.login(usernameEditText.text.toString(), passwordEditText.text.toString())
                    .observe(this@LoginActivity, Observer { result ->
                        if (result is Result.Success<*>) {
                            val data = (result as Result.Success<*>).getData()
                            loginViewModel.getLoginResult().value = LoginResult(LoggedInUser(data!!.ime), 0)
                            saveUserData(data)
                        } else {
                            when ((result as Result.Error).getError()?.message.toString()) {
                                Constants.RESPONSE_FAIL_PWD ->
                                    loginViewModel.getLoginResult().setValue(LoginResult(LoggedInUser(""), R.string.login_failed_pwd))
                                Constants.RESPONSE_FAIL_USN ->
                                    loginViewModel.getLoginResult().setValue(LoginResult(LoggedInUser(""), R.string.login_failed_usn))
                                else ->
                                    loginViewModel.getLoginResult().setValue(LoginResult(LoggedInUser(""), R.string.login_failed))
                            }
                        }
                    })
        }
        registerButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)

        }
        getUserData()
    }

    private fun updateUiWithUser(model: LoggedInUser?) {
        val welcome = getString(R.string.welcome) + (model?.displayName)
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

    private fun saveUserData(data: Korisnik) {
        val prefs = getSharedPreferences("UserData", 0)
        val edit = prefs.edit()
        edit.putString("username", data.email)
        edit.apply()
    }

    private fun getUserData() {
        val prefs = getSharedPreferences("UserData", 0)
        val userName = prefs.getString("username", "")
        if (userName?.isNotEmpty() == true) usernameEditText.setText(userName)
    }
}