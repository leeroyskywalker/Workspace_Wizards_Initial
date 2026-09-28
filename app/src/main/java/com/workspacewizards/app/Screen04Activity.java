package com.workspacewizards.app;

import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Screen04Activity extends AppCompatActivity {

    private ImageButton btnBack;
    private Button btnDispatchJob;
    private AutoCompleteTextView actvClientSelect, actvServiceType, actvAssignCrew;
    private EditText etJobTitle, etJobNotes, etScheduledDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen04);

        // Binding views
        btnBack = findViewById(R.id.btnBack);
        btnDispatchJob = findViewById(R.id.btnDispatchJob);
        
        actvClientSelect = findViewById(R.id.actvClientSelect);
        actvServiceType = findViewById(R.id.actvServiceType);
        actvAssignCrew = findViewById(R.id.actvAssignCrew);
        
        etJobTitle = findViewById(R.id.etJobTitle);
        etJobNotes = findViewById(R.id.etJobNotes);
        etScheduledDate = findViewById(R.id.etScheduledDate);

        // Back button triggers standard back navigation
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        }

        // Dispatch button logic
        if (btnDispatchJob != null) {
            btnDispatchJob.setOnClickListener(v -> {
                if (validateForm()) {
                    Toast.makeText(this, "Work Order Created", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }

    private boolean validateForm() {
        if (etJobTitle != null && etJobTitle.getText().toString().trim().isEmpty()) {
            etJobTitle.setError("Required");
            return false;
        }
        return true;
    }
}