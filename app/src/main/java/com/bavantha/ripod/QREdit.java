package com.bavantha.ripod;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

public class QREdit {

    private static QREdit instance;
    MaterialAlertDialogBuilder builder;

    private Context context;

    public static QREdit getInstance() {
        if (instance == null) {
            instance = new QREdit();
        }
        return instance;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void alertQREdit(Context context) {

        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.qr_code, null);


        new MaterialAlertDialogBuilder(context)
                .setView(promptsView)
                .setTitle("Change QR Code URL")
                .setPositiveButton("Confirm",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final TextInputLayout userInput =  promptsView.findViewById(R.id.qr_url);
                        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("URL", userInput.getEditText().getText().toString()).commit();
                        dialog.cancel();
                    }
                })

                .setNegativeButton("Abort", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }
}
