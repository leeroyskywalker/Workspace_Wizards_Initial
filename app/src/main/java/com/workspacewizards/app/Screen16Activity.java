package com.workspacewizards.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class Screen16Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen16);

        ImageButton btnBack = findViewById(R.id.btnBack);
        Button btnVerifyAndEnable = findViewById(R.id.btnVerifyAndEnable);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        }

        if (btnVerifyAndEnable != null) {
            btnVerifyAndEnable.setOnClickListener(v -> {
                Intent intent = new Intent(Screen16Activity.this, MfaActivity.class);
                startActivity(intent);
            });
        }
    }
}