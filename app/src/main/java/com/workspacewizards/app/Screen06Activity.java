package com.workspacewizards.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Screen06Activity extends AppCompatActivity {

    private ImageButton btnBack;
    private Button btnNextPreview;
    private ImageView ivPhotoPreview, ivSignaturePad;
    private Button btnCapturePhoto, btnClearSignature, btnCompleteJob;
    private EditText etCompletionNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen06);

        // Binding views
        btnBack = findViewById(R.id.btnBack);
        btnNextPreview = findViewById(R.id.btnNextPreview);
        ivPhotoPreview = findViewById(R.id.ivPhotoPreview);
        ivSignaturePad = findViewById(R.id.ivSignaturePad);
        btnCapturePhoto = findViewById(R.id.btnCapturePhoto);
        btnClearSignature = findViewById(R.id.btnClearSignature);
        btnCompleteJob = findViewById(R.id.btnCompleteJob);
        etCompletionNotes = findViewById(R.id.etCompletionNotes);

        btnBack.setOnClickListener(v -> finish());

        // Prototype flow navigation
        btnNextPreview.setOnClickListener(v -> {
            Intent intent = new Intent(Screen06Activity.this, Screen07Activity.class);
            startActivity(intent);
        });

        btnCompleteJob.setOnClickListener(v -> {
            if (validateExecution()) {
                Toast.makeText(this, "Job Completed & Sent for Invoicing", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Screen06Activity.this, Screen07Activity.class);
                startActivity(intent);
            }
        });

        btnCapturePhoto.setOnClickListener(v -> Toast.makeText(this, "Launching Camera...", Toast.LENGTH_SHORT).show());

        btnClearSignature.setOnClickListener(v -> {
            ivSignaturePad.setImageDrawable(null);
            Toast.makeText(this, "Signature Cleared", Toast.LENGTH_SHORT).show();
        });
    }

    private boolean validateExecution() {
        return true;
    }
}
