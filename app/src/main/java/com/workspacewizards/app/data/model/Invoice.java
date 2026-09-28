package com.workspacewizards.app.data.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Invoice implements Serializable {

    // Metadata
    private String invoiceNumber;
    private String issueDate;
    private String dueDate;

    // Billed To
    private String clientName;
    private String clientContactName;
    private String clientEmail;
    private String clientAddress;

    // Payable To
    private String companyName;
    private String companyContactName;
    private String companyEmail;
    private String companyAddress;

    // Line Items
    private List<InvoiceLineItem> lineItems;

    // Summary
    private double taxRatePercent;

    // Payment Details
    private String bankName;
    private String accountName;
    private String accountNumber;
    private String sortCode;
    private String ibanSwift;
    private String paymentReference;

    public Invoice() {
        this.invoiceNumber = "INV-1004";
        this.issueDate = "2026-09-27";
        this.dueDate = "2026-10-27";

        this.clientName = "Acme Corporation";
        this.clientContactName = "John Doe";
        this.clientEmail = "billing@acme.com";
        this.clientAddress = "123 Business St, Tech City";

        this.companyName = "Workspace Wizards";
        this.companyContactName = "Alex Field";
        this.companyEmail = "alex.field@workspacewizards.com";
        this.companyAddress = "45 Innovation Way, Cape Town";

        this.lineItems = new ArrayList<>();
        this.lineItems.add(new InvoiceLineItem("Custom Software Development", 40.0, 850.00));
        this.lineItems.add(new InvoiceLineItem("Cloud Architecture & Setup", 10.0, 1200.00));

        this.taxRatePercent = 15.0;

        this.bankName = "First National Bank";
        this.accountName = "Workspace Wizards (Pty) Ltd";
        this.accountNumber = "62839201928";
        this.sortCode = "250655";
        this.ibanSwift = "FIRNZAJJ";
        this.paymentReference = "INV-1004";
    }

    public String getInvoiceNumber() {
        return invoiceNumber != null ? invoiceNumber : "";
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getIssueDate() {
        return issueDate != null ? issueDate : "";
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getDueDate() {
        return dueDate != null ? dueDate : "";
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getClientName() {
        return clientName != null ? clientName : "";
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientContactName() {
        return clientContactName != null ? clientContactName : "";
    }

    public void setClientContactName(String clientContactName) {
        this.clientContactName = clientContactName;
    }

    public String getClientEmail() {
        return clientEmail != null ? clientEmail : "";
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getClientAddress() {
        return clientAddress != null ? clientAddress : "";
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public String getCompanyName() {
        return companyName != null ? companyName : "";
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyContactName() {
        return companyContactName != null ? companyContactName : "";
    }

    public void setCompanyContactName(String companyContactName) {
        this.companyContactName = companyContactName;
    }

    public String getCompanyEmail() {
        return companyEmail != null ? companyEmail : "";
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getCompanyAddress() {
        return companyAddress != null ? companyAddress : "";
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public List<InvoiceLineItem> getLineItems() {
        if (lineItems == null) {
            lineItems = new ArrayList<>();
        }
        return lineItems;
    }

    public void setLineItems(List<InvoiceLineItem> lineItems) {
        this.lineItems = lineItems;
    }

    public double getTaxRatePercent() {
        return taxRatePercent;
    }

    public void setTaxRatePercent(double taxRatePercent) {
        this.taxRatePercent = taxRatePercent;
    }

    public String getBankName() {
        return bankName != null ? bankName : "";
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountName() {
        return accountName != null ? accountName : "";
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber != null ? accountNumber : "";
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getSortCode() {
        return sortCode != null ? sortCode : "";
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public String getIbanSwift() {
        return ibanSwift != null ? ibanSwift : "";
    }

    public void setIbanSwift(String ibanSwift) {
        this.ibanSwift = ibanSwift;
    }

    public String getPaymentReference() {
        return paymentReference != null ? paymentReference : "";
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public double getSubtotal() {
        double subtotal = 0.0;
        if (lineItems != null) {
            for (InvoiceLineItem item : lineItems) {
                subtotal += item.getLineTotal();
            }
        }
        return subtotal;
    }

    public double getTaxAmount() {
        return getSubtotal() * (taxRatePercent / 100.0);
    }

    public double getTotalDue() {
        return getSubtotal() + getTaxAmount();
    }
}