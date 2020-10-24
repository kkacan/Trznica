package hr.kacan.trznica.repository;

import androidx.lifecycle.MutableLiveData;

import hr.kacan.trznica.data.Result;
import hr.kacan.trznica.models.Korisnik;
import hr.kacan.trznica.data.LoginDataSource;


/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;

    private LoginDataSource dataSource;

    private Korisnik user = null;

    // private constructor : singleton access
    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;

    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }

    private void setLoggedInUser(Korisnik user) {
        this.user = user;

    }

    public MutableLiveData<Result<Korisnik>> login(String username, String password) {
        // handle login
        MutableLiveData<Result<Korisnik>> result = dataSource.login(username, password);

        if (result.getValue() instanceof Result.Success) {
            setLoggedInUser(((Result.Success<Korisnik>) result.getValue()).getData());
        }
        return result;
    }
}