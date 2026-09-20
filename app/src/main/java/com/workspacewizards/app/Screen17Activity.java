package com.workspacewizards.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class Screen17Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen17);

        ImageButton btnBack = findViewById(R.id.btnBack);
        Button btnNextPreview = findViewById(R.id.btnNextPreview);
        ImageButton btnConfigureMfa = findViewById(R.id.btnConfigureMfa);
        Button btnReconfigureMfa = findViewById(R.id.btnReconfigureMfa);
        Button btnLogout = findViewById(R.id.btnLogout);

        btnBack.setOnClickListener(v -> finish());

        btnNextPreview.setOnClickListener(v -> {
            Intent intent = new Intent(Screen17Activity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        btnConfigureMfa.setOnClickListener(v -> {
            Intent intent = new Intent(Screen17Activity.this, Screen16Activity.class);
            startActivity(intent);
        });

        btnReconfigureMfa.setOnClickListener(v -> {
            Intent intent = new Intent(Screen17Activity.this, Screen16Activity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(Screen17Activity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
