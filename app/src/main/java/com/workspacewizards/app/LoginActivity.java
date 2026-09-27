package com.workspacewizards.app;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ensure Firebase App is initialized before obtaining Auth instance
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this);
        }
        mAuth = FirebaseAuth.getInstance();

        // Bind next/preview button
        MaterialButton btnNextPreview = findViewById(R.id.btnNextPreview);

        if (btnNextPreview != null) {
            btnNextPreview.setOnClickListener(v -> {
                Intent intent = new Intent(LoginActivity.this, MfaActivity.class);
                startActivity(intent);
            });
        }
    }
}