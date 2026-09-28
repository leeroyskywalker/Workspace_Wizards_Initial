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
        ImageButton btnConfigureMfa = findViewById(R.id.btnConfigureMfa);
        Button btnReconfigureMfa = findViewById(R.id.btnReconfigureMfa);
        Button btnLogout = findViewById(R.id.btnLogout);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        }

        if (btnConfigureMfa != null) {
            btnConfigureMfa.setOnClickListener(v -> {
                Intent intent = new Intent(Screen17Activity.this, Screen16Activity.class);
                startActivity(intent);
            });
        }

        if (btnReconfigureMfa != null) {
            btnReconfigureMfa.setOnClickListener(v -> {
                Intent intent = new Intent(Screen17Activity.this, Screen16Activity.class);
                startActivity(intent);
            });
        }

        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                Intent intent = new Intent(Screen17Activity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
        }
    }
}