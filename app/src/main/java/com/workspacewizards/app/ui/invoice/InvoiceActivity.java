package com.workspacewizards.app.ui.invoice;

import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.workspacewizards.app.R;
import com.workspacewizards.app.data.model.Invoice;
import com.workspacewizards.app.data.model.InvoiceLineItem;
import com.workspacewizards.app.utils.InvoicePdfGenerator;

import java.util.Locale;

public class InvoiceActivity extends AppCompatActivity implements InvoiceLineItemAdapter.OnLineItemChangeListener {

    private Invoice invoice;
    private InvoiceLineItemAdapter adapter;
    private Uri generatedPdfUri;

    // View References
    private ImageButton btnBack;
    private Button btnSaveInvoice;

    private EditText etInvoiceNumber, etIssueDate, etDueDate;
    private EditText etClientName, etClientContactName, etClientEmail, etClientAddress;
    private EditText etCompanyName, etCompanyContactName, etCompanyEmail, etCompanyAddress;
    private EditText etBankName, etAccountName, etAccountNumber, etSortCode, etIbanSwift, etPaymentReference;

    private RecyclerView rvLineItems;
    private MaterialButton btnAddLineItem;

    private TextView tvSubtotalAmount, tvTaxAmount, tvTotalDueAmount;
    private EditText etTaxRate;

    private MaterialButton btnExportPdf, btnSharePdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_editor);

        invoice = new Invoice();

        bindViews();
        populateFieldsFromInvoice();
        setupRecyclerView();
        setupListeners();
        recalculateSummary();
    }

    private void bindViews() {
        btnBack = findViewById(R.id.btnBack);
        btnSaveInvoice = findViewById(R.id.btnSaveInvoice);

        etInvoiceNumber = findViewById(R.id.etInvoiceNumber);
        etIssueDate = findViewById(R.id.etIssueDate);
        etDueDate = findViewById(R.id.etDueDate);

        etClientName = findViewById(R.id.etClientName);
        etClientContactName = findViewById(R.id.etClientContactName);
        etClientEmail = findViewById(R.id.etClientEmail);
        etClientAddress = findViewById(R.id.etClientAddress);

        etCompanyName = findViewById(R.id.etCompanyName);
        etCompanyContactName = findViewById(R.id.etCompanyContactName);
        etCompanyEmail = findViewById(R.id.etCompanyEmail);
        etCompanyAddress = findViewById(R.id.etCompanyAddress);

        etBankName = findViewById(R.id.etBankName);
        etAccountName = findViewById(R.id.etAccountName);
        etAccountNumber = findViewById(R.id.etAccountNumber);
        etSortCode = findViewById(R.id.etSortCode);
        etIbanSwift = findViewById(R.id.etIbanSwift);
        etPaymentReference = findViewById(R.id.etPaymentReference);

        rvLineItems = findViewById(R.id.rvLineItems);
        btnAddLineItem = findViewById(R.id.btnAddLineItem);

        tvSubtotalAmount = findViewById(R.id.tvSubtotalAmount);
        tvTaxAmount = findViewById(R.id.tvTaxAmount);
        tvTotalDueAmount = findViewById(R.id.tvTotalDueAmount);
        etTaxRate = findViewById(R.id.etTaxRate);

        btnExportPdf = findViewById(R.id.btnExportPdf);
        btnSharePdf = findViewById(R.id.btnSharePdf);
    }

    private void populateFieldsFromInvoice() {
        if (invoice == null) return;

        etInvoiceNumber.setText(invoice.getInvoiceNumber());
        etIssueDate.setText(invoice.getIssueDate());
        etDueDate.setText(invoice.getDueDate());

        etClientName.setText(invoice.getClientName());
        etClientContactName.setText(invoice.getClientContactName());
        etClientEmail.setText(invoice.getClientEmail());
        etClientAddress.setText(invoice.getClientAddress());

        etCompanyName.setText(invoice.getCompanyName());
        etCompanyContactName.setText(invoice.getCompanyContactName());
        etCompanyEmail.setText(invoice.getCompanyEmail());
        etCompanyAddress.setText(invoice.getCompanyAddress());

        etBankName.setText(invoice.getBankName());
        etAccountName.setText(invoice.getAccountName());
        etAccountNumber.setText(invoice.getAccountNumber());
        etSortCode.setText(invoice.getSortCode());
        etIbanSwift.setText(invoice.getIbanSwift());
        etPaymentReference.setText(invoice.getPaymentReference());

        etTaxRate.setText(String.format(Locale.US, "%.1f", invoice.getTaxRatePercent()));
    }

    private void setupRecyclerView() {
        rvLineItems.setLayoutManager(new LinearLayoutManager(this));
        adapter = new InvoiceLineItemAdapter(invoice.getLineItems(), this);
        rvLineItems.setAdapter(adapter);
    }

    private void setupListeners() {
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        if (btnSaveInvoice != null) {
            btnSaveInvoice.setOnClickListener(v -> {
                syncInvoiceFromViews();
                Toast.makeText(InvoiceActivity.this, "Invoice Saved Successfully", Toast.LENGTH_SHORT).show();
            });
        }

        if (btnAddLineItem != null) {
            btnAddLineItem.setOnClickListener(v -> {
                invoice.getLineItems().add(new InvoiceLineItem("New Service / Item", 1.0, 500.00));
                adapter.notifyItemInserted(invoice.getLineItems().size() - 1);
                recalculateSummary();
            });
        }

        if (etTaxRate != null) {
            etTaxRate.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        double rate = Double.parseDouble(s.toString());
                        invoice.setTaxRatePercent(rate);
                    } catch (Exception e) {
                        invoice.setTaxRatePercent(0.0);
                    }
                    recalculateSummary();
                }
            });
        }

        if (btnExportPdf != null) {
            btnExportPdf.setOnClickListener(v -> {
                syncInvoiceFromViews();
                generatedPdfUri = InvoicePdfGenerator.generateAndSaveInvoicePdf(InvoiceActivity.this, invoice);
            });
        }

        if (btnSharePdf != null) {
            btnSharePdf.setOnClickListener(v -> {
                syncInvoiceFromViews();
                if (generatedPdfUri == null) {
                    generatedPdfUri = InvoicePdfGenerator.generateAndSaveInvoicePdf(InvoiceActivity.this, invoice);
                }
                if (generatedPdfUri != null) {
                    InvoicePdfGenerator.shareInvoicePdf(InvoiceActivity.this, generatedPdfUri);
                }
            });
        }
    }

    private void syncInvoiceFromViews() {
        if (invoice == null) return;

        invoice.setInvoiceNumber(etInvoiceNumber.getText().toString());
        invoice.setIssueDate(etIssueDate.getText().toString());
        invoice.setDueDate(etDueDate.getText().toString());

        invoice.setClientName(etClientName.getText().toString());
        invoice.setClientContactName(etClientContactName.getText().toString());
        invoice.setClientEmail(etClientEmail.getText().toString());
        invoice.setClientAddress(etClientAddress.getText().toString());

        invoice.setCompanyName(etCompanyName.getText().toString());
        invoice.setCompanyContactName(etCompanyContactName.getText().toString());
        invoice.setCompanyEmail(etCompanyEmail.getText().toString());
        invoice.setCompanyAddress(etCompanyAddress.getText().toString());

        invoice.setBankName(etBankName.getText().toString());
        invoice.setAccountName(etAccountName.getText().toString());
        invoice.setAccountNumber(etAccountNumber.getText().toString());
        invoice.setSortCode(etSortCode.getText().toString());
        invoice.setIbanSwift(etIbanSwift.getText().toString());
        invoice.setPaymentReference(etPaymentReference.getText().toString());
    }

    private void recalculateSummary() {
        if (invoice == null) return;

        double subtotal = invoice.getSubtotal();
        double tax = invoice.getTaxAmount();
        double total = invoice.getTotalDue();

        if (tvSubtotalAmount != null) {
            tvSubtotalAmount.setText(String.format(Locale.US, "R %.2f", subtotal));
        }
        if (tvTaxAmount != null) {
            tvTaxAmount.setText(String.format(Locale.US, "R %.2f", tax));
        }
        if (tvTotalDueAmount != null) {
            tvTotalDueAmount.setText(String.format(Locale.US, "R %.2f", total));
        }
    }

    @Override
    public void onLineItemChanged() {
        recalculateSummary();
    }
}