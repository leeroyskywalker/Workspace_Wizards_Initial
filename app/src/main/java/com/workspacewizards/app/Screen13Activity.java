package com.workspacewizards.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class Screen13Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen13);

        Button btnNextPreview = findViewById(R.id.btnNextPreview);
        btnNextPreview.setOnClickListener(v -> {
            Intent intent = new Intent(Screen13Activity.this, Screen14Activity.class);
            startActivity(intent);
        });
    }
}
