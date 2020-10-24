package hr.kacan.trznica.view.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import hr.kacan.trznica.R;
import hr.kacan.trznica.data.Result;
import hr.kacan.trznica.models.Korisnik;
import hr.kacan.trznica.view.MainActivity;
import hr.kacan.trznica.view.MenuActivity;
import hr.kacan.trznica.view.RegisterActivity;


public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ProgressBar loadingProgressBar;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button registerButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory()).get(LoginViewModel.class);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        registerButton = findViewById(R.id.register);
        loadingProgressBar = findViewById(R.id.loading);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });


        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {

                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                //finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    loginViewModel.login(usernameEditText.getText().toString(), passwordEditText.getText().toString())
                            .observe(LoginActivity.this, new Observer<Result<Korisnik>>() {

                        @Override
                        public void onChanged(Result<Korisnik> result) {
                            if (result instanceof Result.Success) {

                                Korisnik data = ((Result.Success<Korisnik>) result).getData();
                                loginViewModel.getLoginResult().setValue(new LoginResult(new LoggedInUserView(data.getIme())));
                            } else {
                                loginViewModel.getLoginResult().setValue(new LoginResult(R.string.login_failed));
                            }
                        }
                    });
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);


                //loginViewModel.login(usernameEditText.getText().toString(), passwordEditText.getText().toString());
                loginViewModel.login(usernameEditText.getText().toString(), passwordEditText.getText().toString())
                        .observe(LoginActivity.this, new Observer<Result<Korisnik>>() {

                            @Override
                            public void onChanged(Result<Korisnik> result) {
                                if (result instanceof Result.Success) {

                                    Korisnik data = ((Result.Success<Korisnik>) result).getData();
                                    loginViewModel.getLoginResult().setValue(new LoginResult(new LoggedInUserView(data.getIme())));
                                    saveUserData(data);
                                } else {
                                    loginViewModel.getLoginResult().setValue(new LoginResult(R.string.login_failed));
                                }
                            }
                        });

            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        getUserData();
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();

        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
        passwordEditText.setText("");
        usernameEditText.requestFocus();
    }

    private void saveUserData(Korisnik data){
        SharedPreferences prefs = getSharedPreferences("UserData", 0);
        SharedPreferences.Editor edit= prefs.edit();
        edit.putString("username", data.getEmail());
        edit.apply();
    }

    private void getUserData(){
        SharedPreferences prefs = getSharedPreferences("UserData", 0);
        String userName = prefs.getString("username","");
        if (!userName.isEmpty()) usernameEditText.setText(userName);
    }


}