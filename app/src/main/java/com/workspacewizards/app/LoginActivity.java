package com.workspacewizards.app;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.button.MaterialButton;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        MaterialButton btnNextPreview = findViewById(R.id.btnNextPreview);
        btnNextPreview.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, MfaActivity.class);
            startActivity(intent);
        });
    }
}