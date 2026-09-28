package com.workspacewizards.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;
import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    public static final String EXTRA_USER_EMAIL = "EXTRA_USER_EMAIL";
    public static final String EXTRA_EXPECTED_OTP = "EXTRA_EXPECTED_OTP";
    public static final String EXTRA_EXPIRATION_TIMESTAMP = "EXTRA_EXPIRATION_TIMESTAMP";

    private FirebaseAuth mAuth;

    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;
    private EditText etEmail;
    private EditText etPassword;
    private Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ensure Firebase App is initialized before obtaining Auth instance
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this);
        }
        mAuth = FirebaseAuth.getInstance();

        // Bind Views
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSignIn = findViewById(R.id.btnSignIn);

        if (btnSignIn != null) {
            btnSignIn.setOnClickListener(v -> performFirebaseLogin());
        }
    }

    private void performFirebaseLogin() {
        if (tilEmail != null) tilEmail.setError(null);
        if (tilPassword != null) tilPassword.setError(null);

        String email = etEmail != null && etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String password = etPassword != null && etPassword.getText() != null ? etPassword.getText().toString() : "";

        boolean isValid = true;

        if (email.isEmpty()) {
            if (tilEmail != null) tilEmail.setError("Email address is required");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            if (tilEmail != null) tilEmail.setError("Enter a valid email address");
            isValid = false;
        }

        if (password.isEmpty()) {
            if (tilPassword != null) tilPassword.setError("Password is required");
            isValid = false;
        }

        if (!isValid) return;

        btnSignIn.setEnabled(false);

        // Authenticate against Firebase Auth
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    btnSignIn.setEnabled(true);
                    if (task.isSuccessful() && task.getResult() != null) {
                        // Success -> Generate 6-digit OTP & 5-minute expiration timestamp
                        String otpCode = String.format(Locale.US, "%06d", new Random().nextInt(1000000));
                        long expirationTime = System.currentTimeMillis() + (5 * 60 * 1000); // 5 minutes

                        Toast.makeText(LoginActivity.this, "Authentication successful. Verification code sent to email.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginActivity.this, MfaActivity.class);
                        intent.putExtra(EXTRA_USER_EMAIL, email);
                        intent.putExtra(EXTRA_EXPECTED_OTP, otpCode);
                        intent.putExtra(EXTRA_EXPIRATION_TIMESTAMP, expirationTime);
                        startActivity(intent);
                    } else {
                        String errorMsg = "Invalid email or password";
                        if (task.getException() != null && task.getException().getMessage() != null) {
                            errorMsg = task.getException().getMessage();
                        }
                        if (tilPassword != null) {
                            tilPassword.setError(errorMsg);
                        }
                        Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    }
                });
    }
}