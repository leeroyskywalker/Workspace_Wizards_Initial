package com.workspacewizards.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class Screen10Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen10);

        Button btnNextPreview = findViewById(R.id.btnNextPreview);
        Button btnReturnHome = findViewById(R.id.btnReturnHome);
        Button btnViewJobs = findViewById(R.id.btnViewJobs);

        btnNextPreview.setOnClickListener(v -> {
            Intent intent = new Intent(Screen10Activity.this, Screen11Activity.class);
            startActivity(intent);
        });

        btnReturnHome.setOnClickListener(v -> {
            Intent intent = new Intent(Screen10Activity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        btnViewJobs.setOnClickListener(v -> {
            Intent intent = new Intent(Screen10Activity.this, Screen04Activity.class);
            startActivity(intent);
        });
    }
}
