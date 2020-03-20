package com.baidu.speech.myapplication;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

public class MyDialog extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("dialog 1");
        builder.setMessage("please choose yes or no?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                MyInterface myInterface = (MyInterface)getActivity();
                myInterface.buttonYesClicked();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyInterface myInterface = (MyInterface)getActivity();
                myInterface.buttonNoClicked();
            }
        });


        return builder.create();
    }
}

