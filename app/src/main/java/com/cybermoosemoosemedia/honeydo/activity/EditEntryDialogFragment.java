package com.cybermoosemoosemedia.honeydo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.cybermoosemoosemedia.honeydo.R;
import com.cybermoosemoosemedia.honeydo.db.HoneyDoDataModel;
// ...

public class EditEntryDialogFragment extends DialogFragment implements TextView.OnEditorActionListener {

    EditText mCustomEditReminder;
    CheckBox mCustomCheckBox;
    Button mCustomButtonCancel, mCustomButtonCommit;

    public EditEntryDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static EditEntryDialogFragment newInstance(int nId, HoneyDoDataModel reminder) {
        String content;
        content = reminder.getContent();
        EditEntryDialogFragment frag = new EditEntryDialogFragment();
        Bundle args = new Bundle();
        args.putString("content", content);
        args.putInt("nId", nId);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_custom, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        mCustomEditReminder = (EditText) view.findViewById(R.id.custom_edit_reminder);
        // Fetch arguments from bundle
        String content = getArguments().getString("content", "Null");
        int nId = getArguments().getInt("nId");
        mCustomEditReminder.setText(content);


        // Show soft keyboard automatically and request focus to field

        mCustomEditReminder.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        // ...
        // 2. Setup a callback when the "Done" button is pressed on keyboard
        mCustomEditReminder.setOnEditorActionListener(this);

        mCustomButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text back to activity through the implemented listener
            EditEntryDialogListener listener = (EditEntryDialogListener) getActivity();
            listener.onFinishEditDialog(mCustomEditReminder.getText().toString());
            // Close the dialog and return back to the parent activity
            dismiss();
            return true;
        }
        return false;
    }

    // 1. Defines the listener interface with a method passing back data result.
    public interface EditEntryDialogListener {
        void onFinishEditDialog(String inputText);
    }
}
