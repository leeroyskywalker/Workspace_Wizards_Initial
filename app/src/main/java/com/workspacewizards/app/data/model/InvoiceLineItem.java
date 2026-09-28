package com.workspacewizards.app.data.model;

import java.io.Serializable;

public class InvoiceLineItem implements Serializable {
    private String description;
    private double quantity;
    private double rate;

    public InvoiceLineItem() {
        this.description = "";
        this.quantity = 1.0;
        this.rate = 0.0;
    }

    public InvoiceLineItem(String description, double quantity, double rate) {
        this.description = description;
        this.quantity = quantity;
        this.rate = rate;
    }

    public String getDescription() {
        return description != null ? description : "";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getLineTotal() {
        return quantity * rate;
    }
}