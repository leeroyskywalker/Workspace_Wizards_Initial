package com.workspacewizards.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class Screen08Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen08);

        ImageButton btnBack = findViewById(R.id.btnBack);
        Button btnDownloadPdf = findViewById(R.id.btnDownloadPdf);
        Button btnShareInvoice = findViewById(R.id.btnShareInvoice);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        }

        if (btnDownloadPdf != null) {
            btnDownloadPdf.setOnClickListener(v -> {
                Intent intent = new Intent(Screen08Activity.this, com.workspacewizards.app.ui.invoice.InvoiceActivity.class);
                startActivity(intent);
            });
        }

        if (btnShareInvoice != null) {
            btnShareInvoice.setOnClickListener(v -> {
                Intent intent = new Intent(Screen08Activity.this, com.workspacewizards.app.ui.invoice.InvoiceActivity.class);
                startActivity(intent);
            });
        }
    }
}