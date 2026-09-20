package com.workspacewizards.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Screen08Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen08);

        ImageButton btnBack = findViewById(R.id.btnBack);
        Button btnNextPreview = findViewById(R.id.btnNextPreview);
        Button btnDownloadPdf = findViewById(R.id.btnDownloadPdf);
        Button btnShareInvoice = findViewById(R.id.btnShareInvoice);

        btnBack.setOnClickListener(v -> finish());

        btnNextPreview.setOnClickListener(v -> {
            Intent intent = new Intent(Screen08Activity.this, Screen09Activity.class);
            startActivity(intent);
        });

        btnDownloadPdf.setOnClickListener(v -> 
            Toast.makeText(Screen08Activity.this, "Downloading PDF to storage...", Toast.LENGTH_SHORT).show()
        );

        btnShareInvoice.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("application/pdf");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Invoice for Work Order");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Please find attached the invoice for services rendered.");
            startActivity(Intent.createChooser(shareIntent, "Share Invoice via"));
        });
    }
}
