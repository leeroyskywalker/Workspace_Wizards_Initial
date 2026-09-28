package com.workspacewizards.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class Screen07Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen07);

        ImageButton btnBack = findViewById(R.id.btnBack);
        Button btnGenerateInvoice = findViewById(R.id.btnGenerateInvoice);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        }

        if (btnGenerateInvoice != null) {
            btnGenerateInvoice.setOnClickListener(v -> launchInvoiceScreen());
        }
    }

    private void launchInvoiceScreen() {
        Intent intent = new Intent(Screen07Activity.this, Screen08Activity.class);
        startActivity(intent);
    }
}