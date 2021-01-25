package hr.kacan.trznica.view.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.kacan.trznica.R
import hr.kacan.trznica.data.Result
import hr.kacan.trznica.models.Korisnik
import hr.kacan.trznica.repository.LoginRepository

class LoginViewModel internal constructor(private val loginRepository: LoginRepository?) : ViewModel() {

    private val loginFormState: MutableLiveData<LoginFormState> = MutableLiveData()
    private val loginResult: MutableLiveData<LoginResult> = MutableLiveData()

    fun getLoginFormState(): LiveData<LoginFormState> {
        return loginFormState
    }

    fun getLoginResult(): MutableLiveData<LoginResult> {
        return loginResult
    }

    fun login(username: String, password: String): LiveData<Result<Korisnik>> {
        return loginRepository!!.login(username, password)
    }

    fun loginDataChanged(username: String?, password: String?) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(LoginFormState(R.string.invalid_username, 0))
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(LoginFormState(0, R.string.invalid_password))
        } else {
            loginFormState.setValue(LoginFormState(true))
        }
    }

    fun isUserNameValid(username: String?): Boolean {
        if (username == null) {
            return false
        }
        return if (username.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            false
        }
    }

    private fun isPasswordValid(password: String?): Boolean {
        return password != null && password.trim { it <= ' ' }.length > 5
    }
}