package com.workspacewizards.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class Screen15Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen15);

        ImageButton btnBack = findViewById(R.id.btnBack);
        ImageButton btnReturnHome = findViewById(R.id.btnReturnHome);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        }

        if (btnReturnHome != null) {
            btnReturnHome.setOnClickListener(v -> {
                Intent intent = new Intent(Screen15Activity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            });
        }
    }
}