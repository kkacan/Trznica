package hr.kacan.trznica.repository

import androidx.lifecycle.MutableLiveData
import hr.kacan.trznica.models.Korisnik
import hr.kacan.trznica.view.login.LoginDataSource
import hr.kacan.trznica.view.login.Result

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
class LoginRepository
private constructor(private val dataSource: LoginDataSource) {
    private var user: Korisnik? = null
    fun isLoggedIn(): Boolean {
        return user != null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    private fun setLoggedInUser(user: Korisnik?) {
        this.user = user
    }

    fun login(username: String, password: String): MutableLiveData<Result<Korisnik>> {
        // handle login
        val result = dataSource.login(username, password)
        if (result.value is Result.Success<*>) {
            setLoggedInUser((result.value as Result.Success<*>).getData())
        }
        return result
    }

    companion object {
        @Volatile
        private var instance: LoginRepository? = null
        fun getInstance(dataSource: LoginDataSource): LoginRepository? {
            if (instance == null) {
                instance = LoginRepository(dataSource)
            }
            return instance
        }
    }
}