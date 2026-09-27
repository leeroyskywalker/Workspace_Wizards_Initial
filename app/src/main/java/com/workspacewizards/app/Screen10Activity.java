package com.workspacewizards.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class Screen10Activity extends AppCompatActivity {

    public static final String EXTRA_WORK_ORDER_ID = "EXTRA_WORK_ORDER_ID";

    private TextView tvCompletedOrderRef;
    private MaterialButton btnReturnHome;
    private Button btnViewJobs;
    private Button btnNextPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen10);

        tvCompletedOrderRef = findViewById(R.id.tvCompletedOrderRef);
        btnReturnHome = findViewById(R.id.btnReturnHome);
        btnViewJobs = findViewById(R.id.btnViewJobs);
        btnNextPreview = findViewById(R.id.btnNextPreview);

        if (getIntent() != null && getIntent().hasExtra(EXTRA_WORK_ORDER_ID)) {
            String woId = getIntent().getStringExtra(EXTRA_WORK_ORDER_ID);
            if (woId != null && !woId.isEmpty()) {
                tvCompletedOrderRef.setText(getString(R.string.screen10_order_ref_format, woId));
            }
        }

        if (btnReturnHome != null) {
            btnReturnHome.setOnClickListener(v -> {
                Intent intent = new Intent(Screen10Activity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            });
        }

        if (btnViewJobs != null) {
            btnViewJobs.setOnClickListener(v -> {
                Intent intent = new Intent(Screen10Activity.this, Screen04Activity.class);
                startActivity(intent);
            });
        }

        if (btnNextPreview != null) {
            btnNextPreview.setOnClickListener(v -> {
                Intent intent = new Intent(Screen10Activity.this, Screen11Activity.class);
                startActivity(intent);
            });
        }
    }
}