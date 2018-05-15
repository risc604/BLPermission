package com.demo.tomcat.blpermission;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

public class PopupWritePermission extends DialogFragment {


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Setting permission");
        builder.setMessage("write setting");

        builder.setPositiveButton(" OK ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                getActivity().startActivity(intent);
                dismiss();
            }
        });
        //builder.setPositiveButton(R.string.ok, (dialog, which) -> {
        //    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        //    intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
        //    getActivity().startActivity(intent);
        //    dismiss();
        //});
        //

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        return builder.create();
    }
}

