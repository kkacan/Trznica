package hr.kacan.trznica.view

import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.credentials.*
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.rilixtech.widget.countrycodepicker.CountryCodePicker
import hr.kacan.trznica.R
import hr.kacan.trznica.conf.Constants
import hr.kacan.trznica.models.Korisnik
import hr.kacan.trznica.view.login.LoginActivity
import hr.kacan.trznica.view.login.LoginViewModel
import hr.kacan.trznica.view.login.LoginViewModelFactory
import hr.kacan.trznica.viewmodel.RegisterViewModel
import java.util.*
import java.util.concurrent.TimeUnit


class RegisterActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var password2EditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var surnameEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var townEditText: EditText
    private lateinit var telEditText: EditText
    private lateinit var ccp: CountryCodePicker
    private lateinit var registerButton: Button
    private lateinit var model: RegisterViewModel
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var verId: String
    private var showHelper: Boolean = true
    private var phoneValid: Boolean = false
    private lateinit var dialogValidate: AlertDialog
    private lateinit var countDownTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        model = ViewModelProvider(this).get(RegisterViewModel::class.java)
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory()).get(LoginViewModel::class.java)
        usernameEditText = findViewById(R.id.username)
        passwordEditText = findViewById(R.id.password)
        password2EditText = findViewById(R.id.password2)
        nameEditText = findViewById(R.id.ime)
        surnameEditText = findViewById(R.id.prezime)
        townEditText = findViewById(R.id.grad)
        addressEditText = findViewById(R.id.adresa)
        telEditText = findViewById(R.id.tel)
        registerButton = findViewById(R.id.register)
        loadingProgressBar = findViewById(R.id.loading)
        ccp = findViewById(R.id.ccp)

        ccp.registerPhoneNumberTextView(telEditText)

        loginViewModel.getLoginFormState().observe(this, Observer { loginFormState ->
            if (loginFormState == null) {
                return@Observer
            }
            registerButton.isEnabled = loginFormState.isDataValid

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
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                loginViewModel.loginDataChanged(usernameEditText.text.toString(), passwordEditText.text.toString())
                registerButton.isEnabled = usernameEditText.text.toString().isNotEmpty() and
                        nameEditText.text.toString().isNotEmpty() and
                        surnameEditText.text.toString().isNotEmpty() and
                        townEditText.text.toString().isNotEmpty() and
                        addressEditText.text.toString().isNotEmpty() and
                        telEditText.text.toString().isNotEmpty() and
                        password2EditText.text.toString().isNotEmpty() and
                        password2EditText.text.toString().isNotEmpty() and
                        loginViewModel.isUserNameValid(usernameEditText.text.toString()) and (passwordEditText.text.toString() == password2EditText.text.toString()) and (
                        passwordEditText.text.toString().length > 5)
            }
        }
        usernameEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)
        password2EditText.addTextChangedListener(afterTextChangedListener)
        nameEditText.addTextChangedListener(afterTextChangedListener)
        surnameEditText.addTextChangedListener(afterTextChangedListener)
        townEditText.addTextChangedListener(afterTextChangedListener)
        addressEditText.addTextChangedListener(afterTextChangedListener)
        telEditText.addTextChangedListener(afterTextChangedListener)

        nameEditText.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                try {
                    if (showHelper) requestHint()
                } catch (e: Exception) {
                }
            }
        }
        registerButton.setOnClickListener {
            validateUserNumber(ccp.fullNumberWithPlus)
        }
    }

    private fun validateUserNumber(tel: String): Boolean {
        loadingProgressBar.visibility = View.VISIBLE
        registerFirebaseCallbacks()

        val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(tel)
                .setTimeout(120L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(callbacks)
                .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

        return false;
    }

    private fun registerFirebaseCallbacks() {

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                registerUser()
                if (dialogValidate.isShowing)  dialogValidate.dismiss()
            }

            override fun onVerificationFailed(e: FirebaseException) {

                if (e is FirebaseAuthInvalidCredentialsException) {

                } else if (e is FirebaseTooManyRequestsException) {

                }
                loadingProgressBar.visibility = View.INVISIBLE
                showDialog(getString(R.string.phone_error))
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                verId = verificationId
                validateDialog()
                loadingProgressBar.visibility = View.INVISIBLE

            }
        }
    }

    private fun createCredentials(verificationId: String, code: String): PhoneAuthCredential{
        return PhoneAuthProvider.getCredential(verificationId, code)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        phoneValid = true
                         registerUser()
                    } else {
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            showDialog(getString(R.string.code_error))
                        }
                    }
                    loadingProgressBar.visibility = View.INVISIBLE
                }
    }

    private fun registerUser() {
        loadingProgressBar.visibility = View.VISIBLE
        val korisnik = Korisnik(0, nameEditText.text.toString().trim(), surnameEditText.text.toString().trim(), townEditText.text.toString().trim(), addressEditText.text.toString().trim(), ccp.fullNumberWithPlus.trim(), usernameEditText.text.toString().trim(), passwordEditText.text.toString().trim())
        model.registerKorisnik(korisnik).observe(this@RegisterActivity, Observer { responseKorisnik ->
            if (responseKorisnik.response == Constants.RESPONSE_SUCCESS) {
                Toast.makeText(applicationContext, getString(R.string.register_success), Toast.LENGTH_LONG).show()
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else if (responseKorisnik.response == Constants.RESPONSE_EXIST) {
                Toast.makeText(applicationContext, getString(R.string.register_exist), Toast.LENGTH_LONG).show()
            } else if (responseKorisnik.response == Constants.RESPONSE_FAIL) {
                Toast.makeText(applicationContext, getString(R.string.register_failed), Toast.LENGTH_LONG).show()
            }
            loadingProgressBar.visibility = View.INVISIBLE
        })
    }

    private fun requestHint() {
        val mGoogleApiClient = GoogleApiClient.Builder(this)
                .addApi(Auth.CREDENTIALS_API)
                .build()
        val hintRequest: HintRequest = HintRequest.Builder()
                .setEmailAddressIdentifierSupported(true)
                .build()
        val intent: PendingIntent = Auth.CredentialsApi.getHintPickerIntent(
                mGoogleApiClient, hintRequest)
        startIntentSenderForResult(intent.intentSender,
                222, null, 0, 0, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 222) {
            if (resultCode == RESULT_OK) {
                val cred: Credential? = data?.getParcelableExtra(Credential.EXTRA_KEY)
                nameEditText.setText(cred?.givenName)
                surnameEditText.setText(cred?.familyName)
                usernameEditText.setText(cred?.id)
                loginViewModel.loginDataChanged(usernameEditText.text.toString(), "")
                showHelper = false
            }
        }
    }

    private fun validateDialog(){
        val layoutInflater = LayoutInflater.from(this@RegisterActivity)
        val promptView: View = layoutInflater.inflate(R.layout.validation_dialog, null)
        dialogValidate = AlertDialog.Builder(this).create()
        val btnValidate = promptView.findViewById<View>(R.id.button) as Button
        val editText = promptView.findViewById<EditText>(R.id.editTextNumber)
        val phoneNo = promptView.findViewById<EditText>(R.id.phone_no)
        val countdown = promptView.findViewById<TextView>(R.id.countdown)
        phoneNo.setText(ccp.fullNumberWithPlus)
        btnValidate.setOnClickListener {
            signInWithPhoneAuthCredential(createCredentials(verId, editText.text.toString()))
            dialogValidate.dismiss()
        }
        dialogValidate.setView(promptView)
        dialogValidate.show()
        countDownTimer(countdown)
    }

    private fun countDownTimer(countdown : TextView) {
        countDownTimer = object : CountDownTimer(1000 * 60 * 2, 1000) {
            override fun onTick(l: Long) {
                val text = String.format(Locale.getDefault(), "%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(l) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(l) % 60)
                countdown.text = text
            }

            override fun onFinish() {
                countdown.text = "00:00"
            }
        }
        countDownTimer.start()
    }

    private fun showDialog(msg: String){
        val alertDialog = AlertDialog.Builder(this@RegisterActivity).create()
        alertDialog.setTitle(msg)
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok)
        ) { dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.show()
    }

}
