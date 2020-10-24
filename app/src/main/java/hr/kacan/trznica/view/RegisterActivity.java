package hr.kacan.trznica.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import hr.kacan.trznica.R;

import hr.kacan.trznica.conf.Constants;
import hr.kacan.trznica.models.Korisnik;
import hr.kacan.trznica.models.ResponseKorisnik;
import hr.kacan.trznica.view.login.LoginActivity;
import hr.kacan.trznica.view.login.LoginFormState;
import hr.kacan.trznica.view.login.LoginViewModel;
import hr.kacan.trznica.view.login.LoginViewModelFactory;
import hr.kacan.trznica.viewmodel.PonudaViewModel;
import hr.kacan.trznica.viewmodel.RegisterViewModel;

public class RegisterActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ProgressBar loadingProgressBar;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText password2EditText;
    private EditText nameEditText;
    private EditText surnameEditText;
    private EditText addressEditText;
    private EditText townEditText;
    private EditText telEditText;
    private Button registerButton;
    private RegisterViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        model = new ViewModelProvider(this).get(RegisterViewModel.class);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory()).get(LoginViewModel.class);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        password2EditText = findViewById(R.id.password2);
        nameEditText = findViewById(R.id.ime);
        surnameEditText = findViewById(R.id.prezime);
        townEditText = findViewById(R.id.grad);
        addressEditText = findViewById(R.id.adresa);
        telEditText = findViewById(R.id.tel);
        registerButton = findViewById(R.id.register);
        loadingProgressBar = findViewById(R.id.loading);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                registerButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
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
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(), passwordEditText.getText().toString());
                if (!usernameEditText.getText().toString().isEmpty() &
                        !nameEditText.getText().toString().isEmpty() &
                        !surnameEditText.getText().toString().isEmpty() &
                        !townEditText.getText().toString().isEmpty() &
                        !addressEditText.getText().toString().isEmpty() &
                        !telEditText.getText().toString().isEmpty() &
                        !password2EditText.getText().toString().isEmpty() &
                        !password2EditText.getText().toString().isEmpty() &
                        loginViewModel.isUserNameValid(usernameEditText.getText().toString()) &
                        passwordEditText.getText().toString().equals(password2EditText.getText().toString()) &
                        passwordEditText.getText().toString().length() > 5

                ) {
                    registerButton.setEnabled(true);
                } else {
                    registerButton.setEnabled(false);
                }
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        password2EditText.addTextChangedListener(afterTextChangedListener);
        nameEditText.addTextChangedListener(afterTextChangedListener);
        surnameEditText.addTextChangedListener(afterTextChangedListener);
        townEditText.addTextChangedListener(afterTextChangedListener);
        addressEditText.addTextChangedListener(afterTextChangedListener);
        telEditText.addTextChangedListener(afterTextChangedListener);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                Korisnik korisnik = new Korisnik(nameEditText.getText().toString(), surnameEditText.getText().toString(), townEditText.getText().toString(), addressEditText.getText().toString(), telEditText.getText().toString(), usernameEditText.getText().toString(), passwordEditText.getText().toString());
                model.registerKorisnik(korisnik).observe(RegisterActivity.this, new Observer<ResponseKorisnik>() {

                    @Override
                    public void onChanged(ResponseKorisnik responseKorisnik) {

                        if (responseKorisnik.getResponse() != null){
                            if (responseKorisnik.getResponse().equals(Constants.RESPONSE_SUCCESS)){
                                Toast.makeText(getApplicationContext(),  getString(R.string.register_success), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else if (responseKorisnik.getResponse().equals(Constants.RESPONSE_EXIST)){
                                Toast.makeText(getApplicationContext(),  getString(R.string.register_exist), Toast.LENGTH_LONG).show();
                            } else if (responseKorisnik.getResponse().equals(Constants.RESPONSE_FAIL)){
                                Toast.makeText(getApplicationContext(),  getString(R.string.register_failed), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),  getString(R.string.register_failed), Toast.LENGTH_LONG).show();
                        }

                        loadingProgressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
    }
}