package com.workspacewizards.app.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.workspacewizards.app.data.model.Invoice;
import com.workspacewizards.app.data.model.InvoiceLineItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Locale;

public class InvoicePdfGenerator {

    // Standard A4 dimensions in points at 72 DPI (595 x 842)
    public static final int PAGE_WIDTH = 595;
    public static final int PAGE_HEIGHT = 842;

    public static Uri generateAndSaveInvoicePdf(Context context, Invoice invoice) {
        if (invoice == null || context == null) return null;

        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        // Colors matching design image
        int darkNavy = Color.parseColor("#0F2C59");
        int lightGrey = Color.parseColor("#F2F4F7");
        int borderColor = Color.parseColor("#D0D5DD");
        int darkText = Color.parseColor("#1A1A1A");
        int mutedText = Color.parseColor("#666666");

        // 1. Draw Corner Frame Accents (Matching image outer frame corners)
        paint.setColor(darkNavy);
        paint.setStrokeWidth(2f);
        paint.setStyle(Paint.Style.STROKE);

        // Top-left corner
        canvas.drawLine(15, 15, 35, 15, paint);
        canvas.drawLine(15, 15, 15, 35, paint);

        // Top-right corner
        canvas.drawLine(PAGE_WIDTH - 15, 15, PAGE_WIDTH - 35, 15, paint);
        canvas.drawLine(PAGE_WIDTH - 15, 15, PAGE_WIDTH - 15, 35, paint);

        // Bottom-left corner
        canvas.drawLine(15, PAGE_HEIGHT - 15, 35, PAGE_HEIGHT - 15, paint);
        canvas.drawLine(15, PAGE_HEIGHT - 15, 15, PAGE_HEIGHT - 35, paint);

        // Bottom-right corner
        canvas.drawLine(PAGE_WIDTH - 15, PAGE_HEIGHT - 15, PAGE_WIDTH - 35, PAGE_HEIGHT - 15, paint);
        canvas.drawLine(PAGE_WIDTH - 15, PAGE_HEIGHT - 15, PAGE_WIDTH - 15, PAGE_HEIGHT - 35, paint);

        // 2. Top Header Left (Logo Box & Title)
        float startX = 35;
        float currentY = 40;

        // Logo Box "WW"
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2f);
        paint.setColor(darkNavy);
        canvas.drawRect(startX, currentY, startX + 45, currentY + 45, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setTextSize(18);
        paint.setColor(darkNavy);
        canvas.drawText("WW", startX + 8, currentY + 28, paint);

        // Company Name & Subtitle
        float titleX = startX + 55;
        paint.setTextSize(18);
        canvas.drawText(invoice.getCompanyName(), titleX, currentY + 20, paint);

        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        paint.setTextSize(8);
        paint.setColor(mutedText);
        canvas.drawText("FREELANCE SOFTWARE ENGINEERING", titleX, currentY + 34, paint);

        // 3. Top Header Right (INVOICE & Dates)
        float rightAlignX = PAGE_WIDTH - 35;
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setTextSize(26);
        paint.setColor(darkNavy);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("INVOICE", rightAlignX, currentY + 24, paint);

        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        paint.setTextSize(9);
        paint.setColor(darkText);

        float dateY = currentY + 45;
        canvas.drawText("Invoice Number: " + invoice.getInvoiceNumber(), rightAlignX, dateY, paint);
        dateY += 13;
        canvas.drawText("Issue Date: " + invoice.getIssueDate(), rightAlignX, dateY, paint);
        dateY += 13;
        canvas.drawText("Due Date: " + invoice.getDueDate(), rightAlignX, dateY, paint);

        paint.setTextAlign(Paint.Align.LEFT);

        // 4. Billed To & Payable To Section
        currentY = 125;
        float boxWidth = (PAGE_WIDTH - 80) / 2f;
        float boxHeight = 90;

        // Billed To Box
        float billedX = 35;
        drawInfoBox(canvas, paint, billedX, currentY, boxWidth, boxHeight,
                "BILLED TO",
                invoice.getClientName(),
                "Contact: " + invoice.getClientContactName(),
                "Email: " + invoice.getClientEmail(),
                "Address: " + invoice.getClientAddress(),
                lightGrey, borderColor, darkNavy, darkText);

        // Payable To Box
        float payableX = billedX + boxWidth + 10;
        drawInfoBox(canvas, paint, payableX, currentY, boxWidth, boxHeight,
                "PAYABLE TO",
                invoice.getCompanyName(),
                "Contact: " + invoice.getCompanyContactName(),
                "Email: " + invoice.getCompanyEmail(),
                "Address: " + invoice.getCompanyAddress(),
                lightGrey, borderColor, darkNavy, darkText);

        // 5. Line Items Table
        currentY += boxHeight + 20;

        float colDescX = 35;
        float colQtyX = 340;
        float colRateX = 440;
        float colTotalX = PAGE_WIDTH - 35;

        // Table Header
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(darkNavy);
        canvas.drawRect(35, currentY, PAGE_WIDTH - 35, currentY + 22, paint);

        paint.setColor(Color.WHITE);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setTextSize(9);

        canvas.drawText("Item Description", colDescX + 8, currentY + 14, paint);

        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Hours/Quantity", colQtyX, currentY + 14, paint);

        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("Rate", colRateX - 8, currentY + 14, paint);
        canvas.drawText("Total Amount", colTotalX - 8, currentY + 14, paint);

        paint.setTextAlign(Paint.Align.LEFT);

        currentY += 22;

        // Table Content Rows
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        paint.setColor(darkText);
        paint.setTextSize(9);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(0.5f);
        paint.setColor(borderColor);

        for (InvoiceLineItem item : invoice.getLineItems()) {
            float rowHeight = 24;
            canvas.drawRect(35, currentY, PAGE_WIDTH - 35, currentY + rowHeight, paint);

            paint.setStyle(Paint.Style.FILL);
            paint.setColor(darkText);

            // Description
            canvas.drawText(item.getDescription(), colDescX + 8, currentY + 15, paint);

            // Quantity
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(String.format(Locale.US, "%.1f", item.getQuantity()), colQtyX, currentY + 15, paint);

            // Rate
            paint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(String.format(Locale.US, "R %.2f", item.getRate()), colRateX - 8, currentY + 15, paint);

            // Total
            canvas.drawText(String.format(Locale.US, "R %.2f", item.getLineTotal()), colTotalX - 8, currentY + 15, paint);

            paint.setTextAlign(Paint.Align.LEFT);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(borderColor);

            currentY += rowHeight;
        }

        // 6. Summary & Payment Details Section
        currentY += 20;

        // Payment Details Box (Bottom Left)
        float payBoxWidth = 270;
        float payBoxHeight = 110;
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(lightGrey);
        canvas.drawRoundRect(new RectF(35, currentY, 35 + payBoxWidth, currentY + payBoxHeight), 6, 6, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(0.8f);
        paint.setColor(borderColor);
        canvas.drawRoundRect(new RectF(35, currentY, 35 + payBoxWidth, currentY + payBoxHeight), 6, 6, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setTextSize(9);
        paint.setColor(darkNavy);
        canvas.drawText("PAYMENT DETAILS", 45, currentY + 18, paint);

        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        paint.setTextSize(8);
        paint.setColor(darkText);

        float payTextY = currentY + 34;
        canvas.drawText("Bank Name: " + invoice.getBankName(), 45, payTextY, paint);
        payTextY += 12;
        canvas.drawText("Account Name: " + invoice.getAccountName(), 45, payTextY, paint);
        payTextY += 12;
        canvas.drawText("Account Number: " + invoice.getAccountNumber(), 45, payTextY, paint);
        payTextY += 12;
        canvas.drawText("Sort Code / Routing Number: " + invoice.getSortCode(), 45, payTextY, paint);
        payTextY += 12;
        canvas.drawText("IBAN / SWIFT: " + invoice.getIbanSwift(), 45, payTextY, paint);
        payTextY += 12;
        canvas.drawText("Reference: " + invoice.getPaymentReference(), 45, payTextY, paint);

        // Summary Table (Bottom Right)
        float summaryX = PAGE_WIDTH - 35 - 180;
        float summaryWidth = 180;

        // Subtotal Row
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(borderColor);
        canvas.drawRect(summaryX, currentY, summaryX + summaryWidth, currentY + 22, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(darkText);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        paint.setTextSize(9);
        canvas.drawText("Subtotal", summaryX + 8, currentY + 14, paint);

        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(String.format(Locale.US, "R %.2f", invoice.getSubtotal()), summaryX + summaryWidth - 8, currentY + 14, paint);
        paint.setTextAlign(Paint.Align.LEFT);

        currentY += 22;

        // Tax Row
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(borderColor);
        canvas.drawRect(summaryX, currentY, summaryX + summaryWidth, currentY + 22, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(darkText);
        canvas.drawText(String.format(Locale.US, "Tax/VAT (%.0f%%)", invoice.getTaxRatePercent()), summaryX + 8, currentY + 14, paint);

        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(String.format(Locale.US, "R %.2f", invoice.getTaxAmount()), summaryX + summaryWidth - 8, currentY + 14, paint);
        paint.setTextAlign(Paint.Align.LEFT);

        currentY += 22;

        // Total Due Row (Highlighted Light Grey)
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(lightGrey);
        canvas.drawRect(summaryX, currentY, summaryX + summaryWidth, currentY + 26, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(borderColor);
        canvas.drawRect(summaryX, currentY, summaryX + summaryWidth, currentY + 26, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setColor(darkNavy);
        paint.setTextSize(10);
        canvas.drawText("Total Due", summaryX + 8, currentY + 17, paint);

        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(String.format(Locale.US, "R %.2f", invoice.getTotalDue()), summaryX + summaryWidth - 8, currentY + 17, paint);
        paint.setTextAlign(Paint.Align.LEFT);

        // 7. Footer
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setColor(darkNavy);
        paint.setTextSize(10);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Thank you for your business.", PAGE_WIDTH / 2f, PAGE_HEIGHT - 35, paint);

        pdfDocument.finishPage(page);

        // Save PDF
        return savePdfToStorage(context, pdfDocument, invoice.getInvoiceNumber());
    }

    private static void drawInfoBox(Canvas canvas, Paint paint, float x, float y, float width, float height,
                                   String title, String name, String contact, String email, String address,
                                   int headerBg, int borderColor, int titleColor, int textColor) {
        // Outer Box
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(headerBg);
        canvas.drawRect(x, y, x + width, y + 20, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(0.8f);
        paint.setColor(borderColor);
        canvas.drawRect(x, y, x + width, y + height, paint);

        // Header Title
        paint.setStyle(Paint.Style.FILL);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setTextSize(9);
        paint.setColor(titleColor);
        canvas.drawText(title, x + 8, y + 14, paint);

        // Body Content
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        paint.setTextSize(8);
        paint.setColor(textColor);

        float lineY = y + 33;
        canvas.drawText(name, x + 8, lineY, paint);
        lineY += 12;
        canvas.drawText(contact, x + 8, lineY, paint);
        lineY += 12;
        canvas.drawText(email, x + 8, lineY, paint);
        lineY += 12;
        canvas.drawText(address, x + 8, lineY, paint);
    }

    private static Uri savePdfToStorage(Context context, PdfDocument pdfDocument, String invoiceNumber) {
        String fileName = "Invoice_" + invoiceNumber + ".pdf";

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

                Uri uri = context.getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);
                if (uri != null) {
                    OutputStream outputStream = context.getContentResolver().openOutputStream(uri);
                    if (outputStream != null) {
                        pdfDocument.writeTo(outputStream);
                        outputStream.close();
                    }
                    pdfDocument.close();
                    Toast.makeText(context, "Saved to Downloads: " + fileName, Toast.LENGTH_LONG).show();
                    return uri;
                }
            } else {
                File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                if (!downloadsDir.exists()) {
                    downloadsDir.mkdirs();
                }
                File file = new File(downloadsDir, fileName);
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                pdfDocument.writeTo(fileOutputStream);
                fileOutputStream.close();
                pdfDocument.close();

                Toast.makeText(context, "Saved to Downloads: " + fileName, Toast.LENGTH_LONG).show();
                return FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error saving PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                pdfDocument.close();
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    public static void shareInvoicePdf(Context context, Uri pdfUri) {
        if (context == null || pdfUri == null) return;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_STREAM, pdfUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(intent, "Share Invoice PDF"));
    }
}