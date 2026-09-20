package com.workspacewizards.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Screen04Activity extends AppCompatActivity {

    private ImageButton btnBack;
    private Button btnNextPreview;
    private Button btnDispatchJob;
    private AutoCompleteTextView actvClientSelect, actvServiceType, actvAssignCrew;
    private EditText etJobTitle, etJobNotes, etScheduledDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen04);

        // Binding views
        btnBack = findViewById(R.id.btnBack);
        btnNextPreview = findViewById(R.id.btnNextPreview);
        btnDispatchJob = findViewById(R.id.btnDispatchJob);
        
        actvClientSelect = findViewById(R.id.actvClientSelect);
        actvServiceType = findViewById(R.id.actvServiceType);
        actvAssignCrew = findViewById(R.id.actvAssignCrew);
        
        etJobTitle = findViewById(R.id.etJobTitle);
        etJobNotes = findViewById(R.id.etJobNotes);
        etScheduledDate = findViewById(R.id.etScheduledDate);

        // Back button finish activity
        btnBack.setOnClickListener(v -> finish());

        // Next preview launches Screen 05
        btnNextPreview.setOnClickListener(v -> {
            Intent intent = new Intent(Screen04Activity.this, Screen05Activity.class);
            startActivity(intent);
        });

        // Dispatch button logic
        btnDispatchJob.setOnClickListener(v -> {
            if (validateForm()) {
                Toast.makeText(this, "Work Order Created", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateForm() {
        if (etJobTitle.getText().toString().trim().isEmpty()) {
            etJobTitle.setError("Required");
            return false;
        }
        return true;
    }
}
