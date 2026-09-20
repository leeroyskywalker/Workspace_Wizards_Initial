package com.workspacewizards.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Screen09Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen09);

        ImageButton btnBack = findViewById(R.id.btnBack);
        Button btnNextPreview = findViewById(R.id.btnNextPreview);
        Button btnProcessPayment = findViewById(R.id.btnProcessPayment);
        RadioGroup rgPaymentMethod = findViewById(R.id.rgPaymentMethod);
        LinearLayout llCardForm = findViewById(R.id.llCardForm);

        btnBack.setOnClickListener(v -> finish());

        btnNextPreview.setOnClickListener(v -> launchSuccessScreen());

        btnProcessPayment.setOnClickListener(v -> {
            Toast.makeText(Screen09Activity.this, "Sandbox Payment Processed Successfully", Toast.LENGTH_SHORT).show();
            launchSuccessScreen();
        });

        rgPaymentMethod.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbSandboxCard) {
                llCardForm.setVisibility(View.VISIBLE);
            } else {
                llCardForm.setVisibility(View.GONE);
            }
        });
    }

    private void launchSuccessScreen() {
        Intent intent = new Intent(Screen09Activity.this, Screen10Activity.class);
        startActivity(intent);
    }
}
