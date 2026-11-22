package com.example.plantcare;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;

// Confirms deletion before changing persistent data.
public class DeleteDialog extends DialogFragment {
    @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete plant?")
                .setMessage("This plant will be removed from your garden.")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        if (getActivity() instanceof DetailsActivity) {
                            ((DetailsActivity) getActivity()).deletePlant();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) { }
                });
        return builder.create();
    }
}
