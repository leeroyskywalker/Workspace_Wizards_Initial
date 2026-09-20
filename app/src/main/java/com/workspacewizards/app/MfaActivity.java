package com.workspacewizards.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MfaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mfa);

        Button btnNextPreview = findViewById(R.id.btnNextPreview);
        btnNextPreview.setOnClickListener(v -> {
            Intent intent = new Intent(MfaActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}
