package it.unimib.sportq.ui.welcome;

import static it.unimib.sportq.util.Constants.INVALID_CREDENTIALS_ERROR;
import static it.unimib.sportq.util.Constants.INVALID_USER_ERROR;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.apache.commons.validator.routines.EmailValidator;

import it.unimib.sportq.R;
import it.unimib.sportq.data.repository.user.IUserRepository;
import it.unimib.sportq.model.Result;
import it.unimib.sportq.ui.main.MainActivity;
import it.unimib.sportq.util.ServiceLocator;

public class LoginPage extends AppCompatActivity {
    private TextInputLayout emailTextInputLayout;
    private TextInputLayout passwordTextInputLayout;
    Button signUpButton;
    Button loginButton;
    Button forgotPasswordButton;
    UserViewModel userViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);


        // Inizializzazione di userViewModel
        IUserRepository userRepository = ServiceLocator.getInstance().
                getUserRepository(this.getApplication());
        userViewModel = new ViewModelProvider(
                this,
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);

        emailTextInputLayout = findViewById(R.id.text_input_layout_email);
        passwordTextInputLayout = findViewById(R.id.text_input_layout_password);
        signUpButton = findViewById(R.id.button_sign_up);
        loginButton = findViewById(R.id.button_login);
        forgotPasswordButton = findViewById(R.id.button_forgot_password);



        // GESTIONE CLICK PASSWORD DIMENTICATA

        forgotPasswordButton.setOnClickListener(item -> {
            String email = emailTextInputLayout.getEditText().getText().toString();
            if (isEmailCorrect(email)) {
                userViewModel.resetPassword(email);
                Snackbar.make(
                        findViewById(android.R.id.content),
                        getString(R.string.email_sent),
                        Snackbar.LENGTH_SHORT).show();
            }

        });

        // CLICCANDO SU IL PULSANTE DI SIGN UP SI PASSA ALLA SCHERMATA DI SIGN UP
        signUpButton.setOnClickListener(item -> {
            Intent signUp = new Intent(getApplicationContext(), RegistrationPage.class);
            startActivity(signUp);
            finish();
        });

        // INIZIO GESTIONE PULSANTE DI LOGIN
        loginButton.setOnClickListener(item -> {
            // Log.d(TAG, "Button Clicked");
            String email = emailTextInputLayout.getEditText().getText().toString();
            String password = passwordTextInputLayout.getEditText().getText().toString();

            if (isEmailCorrect(email) && isPasswordCorrect(password)) {
                if (!userViewModel.isAuthenticationError()) {
                    userViewModel.getUserMutableLiveData(email, password, true).observe(
                            this, result -> {
                                if (result.isSuccess()) {
                                    userViewModel.setAuthenticationError(false);
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    userViewModel.setAuthenticationError(true);
                                    Snackbar.make(findViewById(android.R.id.content),
                                            getErrorMessage(((Result.Error) result).getMessage()),
                                            Snackbar.LENGTH_SHORT).show();
                                }
                            });

                } else {
                    userViewModel.getUser(email, password, true);
                }
            } else {
                Snackbar.make(
                        findViewById(android.R.id.content),
                        getString(R.string.error_data),
                        Snackbar.LENGTH_SHORT).show();
            }
        }); // FINE GESTIONE PULSANTE LOGIN
    }
    private String getErrorMessage(String errorType) {
        switch (errorType) {
            case INVALID_CREDENTIALS_ERROR:
                return getString(R.string.error_login_password_message);
            case INVALID_USER_ERROR:
                return getString(R.string.error_login_user_message);
            default:
                return getString(R.string.unexpected_error);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


    // FUNZIONI PER VERIFICA DELLA CORREZIONE MAIL E PASSWORD
    private boolean isEmailCorrect(String email) {
        boolean result = EmailValidator.getInstance().isValid(email);
        if (!result) {
            emailTextInputLayout.setError("Email is not correct");
        } else {
            emailTextInputLayout.setError(null);
        }
        return result;
    }
    private boolean isPasswordCorrect(String password) {
        boolean result = password != null && password.length() >= 8;
        if (!result) {
            passwordTextInputLayout.setError("Password is not correct");
        } else {
            passwordTextInputLayout.setError(null);
        }
        return result;
    }
}