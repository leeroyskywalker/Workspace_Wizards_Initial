package com.workspacewizards.app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Screen11Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen11);

        ImageButton btnBack = findViewById(R.id.btnBack);
        Button btnAddClient = findViewById(R.id.btnAddClient);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        }

        if (btnAddClient != null) {
            btnAddClient.setOnClickListener(v -> Toast.makeText(this, "Placeholder: Add Client flow", Toast.LENGTH_SHORT).show());
        }
    }
}