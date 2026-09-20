package com.workspacewizards.app;

import android.content.Intent;
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
        Button btnNextPreview = findViewById(R.id.btnNextPreview);
        Button btnAddClient = findViewById(R.id.btnAddClient);

        btnBack.setOnClickListener(v -> finish());

        btnNextPreview.setOnClickListener(v -> {
            Intent intent = new Intent(Screen11Activity.this, Screen12Activity.class);
            startActivity(intent);
        });

        btnAddClient.setOnClickListener(v -> Toast.makeText(this, "Placeholder: Add Client flow", Toast.LENGTH_SHORT).show());
    }
}
