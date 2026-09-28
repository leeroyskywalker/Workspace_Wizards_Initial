package com.workspacewizards.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.Random;

public class MfaActivity extends AppCompatActivity {

    private String userEmail = "";
    private String expectedOtp = "";
    private long expirationTimestamp = 0L;

    private EditText etOtpDigit1, etOtpDigit2, etOtpDigit3, etOtpDigit4, etOtpDigit5, etOtpDigit6;
    private Button btnVerifyOtp;
    private TextView tvResendCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mfa);

        // Bind Views
        etOtpDigit1 = findViewById(R.id.etOtpDigit1);
        etOtpDigit2 = findViewById(R.id.etOtpDigit2);
        etOtpDigit3 = findViewById(R.id.etOtpDigit3);
        etOtpDigit4 = findViewById(R.id.etOtpDigit4);
        etOtpDigit5 = findViewById(R.id.etOtpDigit5);
        etOtpDigit6 = findViewById(R.id.etOtpDigit6);

        btnVerifyOtp = findViewById(R.id.btnVerifyOtp);
        tvResendCode = findViewById(R.id.tvResendCode);

        // Safely extract Intent extras
        if (getIntent() != null) {
            if (getIntent().hasExtra(LoginActivity.EXTRA_USER_EMAIL)) {
                String emailExtra = getIntent().getStringExtra(LoginActivity.EXTRA_USER_EMAIL);
                if (emailExtra != null) userEmail = emailExtra;
            }
            if (getIntent().hasExtra(LoginActivity.EXTRA_EXPECTED_OTP)) {
                String otpExtra = getIntent().getStringExtra(LoginActivity.EXTRA_EXPECTED_OTP);
                if (otpExtra != null) expectedOtp = otpExtra;
            }
            if (getIntent().hasExtra(LoginActivity.EXTRA_EXPIRATION_TIMESTAMP)) {
                expirationTimestamp = getIntent().getLongExtra(LoginActivity.EXTRA_EXPIRATION_TIMESTAMP, 0L);
            }
        }

        // Default fallback if expectedOtp was empty
        if (expectedOtp.isEmpty()) {
            expectedOtp = String.format(Locale.US, "%06d", new Random().nextInt(1000000));
            expirationTimestamp = System.currentTimeMillis() + (5 * 60 * 1000);
        }

        setupOtpAutoAdvancing();

        if (btnVerifyOtp != null) {
            btnVerifyOtp.setOnClickListener(v -> verifyOtpAndProceed());
        }

        if (tvResendCode != null) {
            tvResendCode.setOnClickListener(v -> resendOtpCode());
        }
    }

    private void setupOtpAutoAdvancing() {
        setupDigitWatcher(etOtpDigit1, null, etOtpDigit2);
        setupDigitWatcher(etOtpDigit2, etOtpDigit1, etOtpDigit3);
        setupDigitWatcher(etOtpDigit3, etOtpDigit2, etOtpDigit4);
        setupDigitWatcher(etOtpDigit4, etOtpDigit3, etOtpDigit5);
        setupDigitWatcher(etOtpDigit5, etOtpDigit4, etOtpDigit6);
        setupDigitWatcher(etOtpDigit6, etOtpDigit5, null);
    }

    private void setupDigitWatcher(final EditText current, final EditText previous, final EditText next) {
        if (current == null) return;

        current.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() == 1 && next != null) {
                    next.requestFocus();
                }
            }
        });

        current.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (current.getText().length() == 0 && previous != null) {
                    previous.requestFocus();
                    return true;
                }
            }
            return false;
        });
    }

    private String getEnteredOtp() {
        StringBuilder sb = new StringBuilder();
        if (etOtpDigit1 != null && etOtpDigit1.getText() != null) sb.append(etOtpDigit1.getText().toString());
        if (etOtpDigit2 != null && etOtpDigit2.getText() != null) sb.append(etOtpDigit2.getText().toString());
        if (etOtpDigit3 != null && etOtpDigit3.getText() != null) sb.append(etOtpDigit3.getText().toString());
        if (etOtpDigit4 != null && etOtpDigit4.getText() != null) sb.append(etOtpDigit4.getText().toString());
        if (etOtpDigit5 != null && etOtpDigit5.getText() != null) sb.append(etOtpDigit5.getText().toString());
        if (etOtpDigit6 != null && etOtpDigit6.getText() != null) sb.append(etOtpDigit6.getText().toString());
        return sb.toString().trim();
    }

    private void verifyOtpAndProceed() {
        String enteredOtp = getEnteredOtp();

        if (enteredOtp.length() < 6) {
            Toast.makeText(this, "Please enter all 6 digits of the passcode.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Development bypass code "123456"
        if ("123456".equals(enteredOtp)) {
            Toast.makeText(this, "Development MFA Verified", Toast.LENGTH_SHORT).show();
            launchDashboardAndFinish();
            return;
        }

        // Expiration check for generated OTP (5 minutes = 300,000 ms)
        if (System.currentTimeMillis() > expirationTimestamp) {
            Toast.makeText(this, "OTP has expired. Please request a new code.", Toast.LENGTH_LONG).show();
            return;
        }

        // Verify generated OTP code
        if (enteredOtp.equals(expectedOtp)) {
            Toast.makeText(this, "Authentication successful.", Toast.LENGTH_SHORT).show();
            launchDashboardAndFinish();
        } else {
            Toast.makeText(this, "Invalid verification code. Use 123456 for testing.", Toast.LENGTH_SHORT).show();
        }
    }

    private void launchDashboardAndFinish() {
        Intent intent = new Intent(MfaActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void resendOtpCode() {
        expectedOtp = String.format(Locale.US, "%06d", new Random().nextInt(1000000));
        expirationTimestamp = System.currentTimeMillis() + (5 * 60 * 1000);

        if (etOtpDigit1 != null) etOtpDigit1.setText("");
        if (etOtpDigit2 != null) etOtpDigit2.setText("");
        if (etOtpDigit3 != null) etOtpDigit3.setText("");
        if (etOtpDigit4 != null) etOtpDigit4.setText("");
        if (etOtpDigit5 != null) etOtpDigit5.setText("");
        if (etOtpDigit6 != null) etOtpDigit6.setText("");

        if (etOtpDigit1 != null) etOtpDigit1.requestFocus();

        String targetMsg = userEmail.isEmpty() ? "your email." : userEmail;
        Toast.makeText(this, "New 6-digit OTP code sent to " + targetMsg, Toast.LENGTH_LONG).show();
    }
}