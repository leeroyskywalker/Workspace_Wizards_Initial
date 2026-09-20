package com.workspacewizards.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class Screen15Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen15);

        ImageButton btnBack = findViewById(R.id.btnBack);
        ImageButton btnReturnHome = findViewById(R.id.btnReturnHome);
        Button btnNextPreview = findViewById(R.id.btnNextPreview);

        btnBack.setOnClickListener(v -> finish());

        btnNextPreview.setOnClickListener(v -> {
            Intent intent = new Intent(Screen15Activity.this, Screen16Activity.class);
            startActivity(intent);
        });

        btnReturnHome.setOnClickListener(v -> {
            Intent intent = new Intent(Screen15Activity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }
}
