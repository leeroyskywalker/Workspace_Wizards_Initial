package com.workspacewizards.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class Screen10Activity extends AppCompatActivity {

    private static final String TAG = "Screen10Activity";
    public static final String EXTRA_WORK_ORDER_ID = "EXTRA_WORK_ORDER_ID";

    private FirebaseFirestore db;
    private TextView tvCompletedOrderRef;
    private MaterialButton btnReturnHome;
    private Button btnViewJobs;

    private String workOrderId = "WO-1004";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen10);

        // Initialize Firestore safely
        db = FirebaseFirestore.getInstance();

        tvCompletedOrderRef = findViewById(R.id.tvCompletedOrderRef);
        btnReturnHome = findViewById(R.id.btnReturnHome);
        btnViewJobs = findViewById(R.id.btnViewJobs);

        // Safely retrieve workOrderId from Intent extras
        if (getIntent() != null && getIntent().hasExtra(EXTRA_WORK_ORDER_ID)) {
            String passedId = getIntent().getStringExtra(EXTRA_WORK_ORDER_ID);
            if (passedId != null && !passedId.trim().isEmpty()) {
                workOrderId = passedId.trim();
            }
        }

        if (tvCompletedOrderRef != null) {
            tvCompletedOrderRef.setText(getString(R.string.screen10_order_ref_format, workOrderId));
        }

        // Update work order status in Firestore
        updateWorkOrderStatusInFirestore(workOrderId);

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
    }

    private void updateWorkOrderStatusInFirestore(String targetWorkOrderId) {
        String finalWorkOrderId = (targetWorkOrderId != null && !targetWorkOrderId.trim().isEmpty())
                ? targetWorkOrderId.trim()
                : "WO-1004";

        Map<String, Object> updates = new HashMap<>();
        updates.put("status", "COMPLETED");
        updates.put("updatedAt", FieldValue.serverTimestamp());

        if (db != null) {
            db.collection("workOrders")
                    .document(finalWorkOrderId)
                    .set(updates, SetOptions.merge())
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "Work order status successfully updated to COMPLETED for ID: " + finalWorkOrderId);
                        Toast.makeText(Screen10Activity.this, "Work order status updated to COMPLETED", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Failed to update work order status in Firestore: " + e.getMessage(), e);
                    });
        }
    }
}