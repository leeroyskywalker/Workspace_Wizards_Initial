package com.workspacewizards.app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Screen12Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen12);

        ImageButton btnBack = findViewById(R.id.btnBack);
        Button btnAddContact = findViewById(R.id.btnAddContact);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        }

        if (btnAddContact != null) {
            btnAddContact.setOnClickListener(v -> Toast.makeText(this, "Placeholder: Add Contact flow", Toast.LENGTH_SHORT).show());
        }
    }
}