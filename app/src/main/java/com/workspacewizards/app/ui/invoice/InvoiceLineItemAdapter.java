package com.workspacewizards.app.ui.invoice;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.workspacewizards.app.R;
import com.workspacewizards.app.data.model.InvoiceLineItem;

import java.util.List;
import java.util.Locale;

public class InvoiceLineItemAdapter extends RecyclerView.Adapter<InvoiceLineItemAdapter.ViewHolder> {

    public interface OnLineItemChangeListener {
        void onLineItemChanged();
    }

    private final List<InvoiceLineItem> items;
    private final OnLineItemChangeListener listener;

    public InvoiceLineItemAdapter(List<InvoiceLineItem> items, OnLineItemChangeListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invoice_line, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InvoiceLineItem item = items.get(position);

        holder.etDescription.setText(item.getDescription());
        holder.etQuantity.setText(String.format(Locale.US, "%.1f", item.getQuantity()));
        holder.etRate.setText(String.format(Locale.US, "%.2f", item.getRate()));
        holder.tvTotal.setText(String.format(Locale.US, "R %.2f", item.getLineTotal()));

        holder.etDescription.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                int adapterPos = holder.getAdapterPosition();
                if (adapterPos != RecyclerView.NO_POSITION && adapterPos < items.size()) {
                    items.get(adapterPos).setDescription(s.toString());
                }
            }
        });

        holder.etQuantity.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                int adapterPos = holder.getAdapterPosition();
                if (adapterPos != RecyclerView.NO_POSITION && adapterPos < items.size()) {
                    try {
                        double qty = Double.parseDouble(s.toString());
                        items.get(adapterPos).setQuantity(qty);
                    } catch (Exception e) {
                        items.get(adapterPos).setQuantity(0.0);
                    }
                    holder.tvTotal.setText(String.format(Locale.US, "R %.2f", items.get(adapterPos).getLineTotal()));
                    if (listener != null) listener.onLineItemChanged();
                }
            }
        });

        holder.etRate.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                int adapterPos = holder.getAdapterPosition();
                if (adapterPos != RecyclerView.NO_POSITION && adapterPos < items.size()) {
                    try {
                        double rate = Double.parseDouble(s.toString());
                        items.get(adapterPos).setRate(rate);
                    } catch (Exception e) {
                        items.get(adapterPos).setRate(0.0);
                    }
                    holder.tvTotal.setText(String.format(Locale.US, "R %.2f", items.get(adapterPos).getLineTotal()));
                    if (listener != null) listener.onLineItemChanged();
                }
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            int adapterPos = holder.getAdapterPosition();
            if (adapterPos != RecyclerView.NO_POSITION && adapterPos < items.size()) {
                items.remove(adapterPos);
                notifyItemRemoved(adapterPos);
                notifyItemRangeChanged(adapterPos, items.size());
                if (listener != null) listener.onLineItemChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        EditText etDescription, etQuantity, etRate;
        TextView tvTotal;
        ImageButton btnDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            etDescription = itemView.findViewById(R.id.etLineDescription);
            etQuantity = itemView.findViewById(R.id.etLineQuantity);
            etRate = itemView.findViewById(R.id.etLineRate);
            tvTotal = itemView.findViewById(R.id.tvLineTotal);
            btnDelete = itemView.findViewById(R.id.btnDeleteLine);
        }
    }
}