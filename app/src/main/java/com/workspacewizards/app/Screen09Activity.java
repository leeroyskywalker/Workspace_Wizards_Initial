package com.workspacewizards.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Screen09Activity extends AppCompatActivity {

    public static final String EXTRA_JOB_AMOUNT = "EXTRA_JOB_AMOUNT";
    public static final String EXTRA_WORK_ORDER_ID = "EXTRA_WORK_ORDER_ID";

    private static final String PAYFAST_MERCHANT_ID = "10049480";
    private static final String PAYFAST_MERCHANT_KEY = "vxc57o2bufshh";
    private static final String PAYFAST_PROCESS_URL = "https://sandbox.payfast.co.za/eng/process";
    private static final String PAYFAST_RETURN_URL = "https://sandbox.payfast.co.za/return";
    private static final String PAYFAST_CANCEL_URL = "https://sandbox.payfast.co.za/cancel";

    private FirebaseFirestore db;

    private ImageButton btnBack;
    private MaterialButton btnPayWithPayFast;
    private TextView tvPaymentAmountZar;
    private View layoutPaymentDetails;
    private WebView webViewPayFast;

    private String workOrderId = "WO-1004";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen09);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Bind Views
        btnBack = findViewById(R.id.btnBack);
        btnPayWithPayFast = findViewById(R.id.btnPayWithPayFast);
        tvPaymentAmountZar = findViewById(R.id.tvPaymentAmountZar);
        layoutPaymentDetails = findViewById(R.id.layoutPaymentDetails);
        webViewPayFast = findViewById(R.id.webViewPayFast);

        // Handle Intent Extras if passed
        if (getIntent() != null) {
            if (getIntent().hasExtra(EXTRA_JOB_AMOUNT)) {
                String amountStr = getIntent().getStringExtra(EXTRA_JOB_AMOUNT);
                if (amountStr != null && !amountStr.isEmpty()) {
                    tvPaymentAmountZar.setText(amountStr);
                }
            }
            if (getIntent().hasExtra(EXTRA_WORK_ORDER_ID)) {
                String woId = getIntent().getStringExtra(EXTRA_WORK_ORDER_ID);
                if (woId != null && !woId.isEmpty()) {
                    workOrderId = woId;
                }
            }
        }

        // Setup Listeners
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> handleBackNavigation());
        }

        if (btnPayWithPayFast != null) {
            btnPayWithPayFast.setOnClickListener(v -> initiatePayFastPayment());
        }

        // Back press dispatcher handling
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (webViewPayFast != null && webViewPayFast.getVisibility() == View.VISIBLE) {
                    handleBackNavigation();
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }

    private void initiatePayFastPayment() {
        String rawAmount = tvPaymentAmountZar != null && tvPaymentAmountZar.getText() != null
                ? tvPaymentAmountZar.getText().toString()
                : "2450.00";
        String amountFormatted = formatAmountForPayFast(rawAmount);

        StringBuilder postDataBuilder = new StringBuilder();
        try {
            String charset = StandardCharsets.UTF_8.name();
            postDataBuilder.append("merchant_id=").append(URLEncoder.encode(PAYFAST_MERCHANT_ID, charset));
            postDataBuilder.append("&merchant_key=").append(URLEncoder.encode(PAYFAST_MERCHANT_KEY, charset));
            postDataBuilder.append("&return_url=").append(URLEncoder.encode(PAYFAST_RETURN_URL, charset));
            postDataBuilder.append("&cancel_url=").append(URLEncoder.encode(PAYFAST_CANCEL_URL, charset));
            postDataBuilder.append("&amount=").append(URLEncoder.encode(amountFormatted, charset));
            postDataBuilder.append("&item_name=").append(URLEncoder.encode("Workspace Wizards Work Order", charset));
        } catch (Exception ignored) {
        }

        byte[] postDataBytes = postDataBuilder.toString().getBytes(StandardCharsets.UTF_8);

        // Hide payment details summary layout, show WebView
        if (layoutPaymentDetails != null) {
            layoutPaymentDetails.setVisibility(View.GONE);
        }
        if (webViewPayFast != null) {
            webViewPayFast.setVisibility(View.VISIBLE);

            WebSettings webSettings = webViewPayFast.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);

            webViewPayFast.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    if (request != null && request.getUrl() != null) {
                        return handleUrlLoading(request.getUrl().toString());
                    }
                    return false;
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return handleUrlLoading(url);
                }
            });

            webViewPayFast.postUrl(PAYFAST_PROCESS_URL, postDataBytes);
        }
    }

    private boolean handleUrlLoading(String url) {
        if (url != null && url.startsWith(PAYFAST_RETURN_URL)) {
            recordPaymentAndFinish();
            return true;
        } else if (url != null && url.startsWith(PAYFAST_CANCEL_URL)) {
            restoreInitialView();
            Toast.makeText(Screen09Activity.this, R.string.screen09_payfast_cancel_toast, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private String formatAmountForPayFast(String rawAmount) {
        if (rawAmount == null) return "2450.00";
        String cleaned = rawAmount.replaceAll("[^0-9.]", "");
        if (cleaned.isEmpty()) return "2450.00";
        try {
            double val = Double.parseDouble(cleaned);
            return String.format(Locale.US, "%.2f", val);
        } catch (Exception e) {
            return "2450.00";
        }
    }

    private void recordPaymentAndFinish() {
        String amountStr = tvPaymentAmountZar.getText() != null ? tvPaymentAmountZar.getText().toString() : "R 2,450.00";

        Map<String, Object> paymentDocument = new HashMap<>();
        paymentDocument.put("amount", amountStr);
        paymentDocument.put("paymentMethod", "PayFast Sandbox");
        paymentDocument.put("status", "COMPLETED");
        paymentDocument.put("timestamp", Timestamp.now());
        paymentDocument.put("workOrderId", workOrderId);

        db.collection("payments")
                .add(paymentDocument)
                .addOnSuccessListener(documentReference -> updateWorkOrderStatusAndFinish())
                .addOnFailureListener(e -> {
                    Toast.makeText(Screen09Activity.this, "Payment record failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    launchSuccessScreen();
                });
    }

    private void updateWorkOrderStatusAndFinish() {
        Map<String, Object> workOrderUpdate = new HashMap<>();
        workOrderUpdate.put("status", "COMPLETED");
        workOrderUpdate.put("updatedAt", FieldValue.serverTimestamp());

        db.collection("workOrders")
                .document(workOrderId)
                .set(workOrderUpdate, SetOptions.merge())
                .addOnCompleteListener(task -> {
                    Toast.makeText(Screen09Activity.this, R.string.screen09_payfast_success_toast, Toast.LENGTH_SHORT).show();
                    launchSuccessScreen();
                });
    }

    private void restoreInitialView() {
        if (webViewPayFast != null) {
            webViewPayFast.stopLoading();
            webViewPayFast.setVisibility(View.GONE);
        }
        if (layoutPaymentDetails != null) {
            layoutPaymentDetails.setVisibility(View.VISIBLE);
        }
    }

    private void handleBackNavigation() {
        if (webViewPayFast != null && webViewPayFast.getVisibility() == View.VISIBLE) {
            if (webViewPayFast.canGoBack()) {
                webViewPayFast.goBack();
            } else {
                restoreInitialView();
            }
        } else {
            finish();
        }
    }

    private void launchSuccessScreen() {
        Intent intent = new Intent(Screen09Activity.this, Screen10Activity.class);
        intent.putExtra(Screen10Activity.EXTRA_WORK_ORDER_ID, workOrderId);
        startActivity(intent);
    }
}