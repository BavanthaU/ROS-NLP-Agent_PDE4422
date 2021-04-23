package com.bavantha.ripod;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

public class QRChange {

    private static QRChange instance;
    MaterialAlertDialogBuilder builder;

    private Context context;

    public static QRChange getInstance() {
        if (instance == null) {
            instance = new QRChange();
        }
        return instance;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void alertQRChange(Context context) {

        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.close_alert, null);


        new MaterialAlertDialogBuilder(context)
                .setView(promptsView)
                .setTitle("Change QR Code URL")
                .setPositiveButton("Confirm",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final TextInputLayout userInput =  promptsView.findViewById(R.id.password);
                        if(userInput.getEditText().getText().toString().equals("admin")){
                            QREdit.getInstance().alertQREdit(context);
                        }else{
                            Toast.makeText(context, "Wrong Password, Please contact Developer",Toast.LENGTH_SHORT).show();
                        }
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
