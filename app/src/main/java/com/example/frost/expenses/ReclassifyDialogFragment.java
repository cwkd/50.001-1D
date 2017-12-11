package com.example.frost.expenses;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Daniel on 11/12/2017.
 */

public class ReclassifyDialogFragment extends DialogFragment {

    public interface ReclassifyDialogListener {
        public void onReclassify(String string);
    }

    ReclassifyDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (ReclassifyDialogListener) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Category: ").setItems(R.array.categories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mListener.onReclassify(getResources().getStringArray(R.array.categories)[i]);
            }
        });
        return builder.create();
    }
}
