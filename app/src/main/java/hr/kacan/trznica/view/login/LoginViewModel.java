package hr.kacan.trznica.view.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;
import hr.kacan.trznica.repository.LoginRepository;
import hr.kacan.trznica.data.Result;
import hr.kacan.trznica.models.Korisnik;
import hr.kacan.trznica.R;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    public LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    MutableLiveData<LoginResult> getLoginResult() {
        return loginResult;
    }


    public LiveData<Result<Korisnik>> login(String username, String password) {
        // can be launched in a separate asynchronous job


        //LoginTask loginTask = new LoginTask();
        //loginTask.execute(username, password);
        MutableLiveData<Result<Korisnik>> mutableLiveData = new MutableLiveData<>();
        mutableLiveData = loginRepository.login(username, password);
        /*Result<Korisnik> result =mutableLiveData.getValue();
       //Result<Korisnik> result = loginRepository.login(username, password);

       if (result instanceof Result.Success) {
            Korisnik data = ((Result.Success<Korisnik>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getIme())));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }*/

       return mutableLiveData;
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }


    // A placeholder username validation check
    public boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return false; //!username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }


   
}