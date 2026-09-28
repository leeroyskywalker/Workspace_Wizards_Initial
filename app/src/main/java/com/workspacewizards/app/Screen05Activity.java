package com.workspacewizards.app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Screen05Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen05);

        ImageButton btnBack = findViewById(R.id.btnBack);
        Button btnCreateJob = findViewById(R.id.btnCreateJob);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        }

        if (btnCreateJob != null) {
            btnCreateJob.setOnClickListener(v -> {
                Toast.makeText(this, "Job Created & Dispatched", Toast.LENGTH_SHORT).show();
                finish();
            });
        }
    }
}