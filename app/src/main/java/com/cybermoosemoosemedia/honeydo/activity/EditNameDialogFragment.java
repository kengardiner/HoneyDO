package com.cybermoosemoosemedia.honeydo.activity;

/**
 * Created by ken_g on 8/29/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.cybermoosemoosemedia.honeydo.R;
import com.cybermoosemoosemedia.honeydo.db.HoneyDoDataModel;
// ...

public class EditNameDialogFragment extends DialogFragment implements View.OnClickListener {

    EditText mCustomEditReminder;
    CheckBox mCustomCheckBox;
    Button mCustomButtonCancel, mCustomButtonCommit;
    Communicator communicator;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        communicator = (Communicator) context;
    }

    public EditNameDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static EditNameDialogFragment newInstance(int nId,HoneyDoDataModel reminder, String title) {
        EditNameDialogFragment frag = new EditNameDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("reminder", reminder.getContent());
        args.putInt("checked", reminder.getImportant());
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_custom, null);

        mCustomButtonCommit = (Button) view.findViewById(R.id.custom_button_commit);
        mCustomButtonCancel = (Button) view.findViewById(R.id.custom_button_cancel);

        mCustomButtonCommit.setOnClickListener(this);
        mCustomButtonCancel.setOnClickListener(this);

        setCancelable(false);
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.custom_button_commit)
        {
            communicator.onDialogMessage(mCustomEditReminder.getText().toString(), mCustomCheckBox.isChecked() ? 1 : 0);
            dismiss();
        }
        else
        {
            dismiss();
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        mCustomEditReminder = (EditText) view.findViewById(R.id.custom_edit_reminder);
        mCustomCheckBox = (CheckBox) view.findViewById(R.id.custom_check_box);
        // Fetch arguments from bundle and set content
        String content = getArguments().getString("reminder");
        mCustomEditReminder.setText(content);
        //Fetch arguments and set important
        Boolean important;

        important = getArguments().getInt("checked") == 1;

        mCustomCheckBox.setChecked(important);
        // Show soft keyboard automatically and request focus to field
        mCustomEditReminder.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    interface Communicator
    {
        void onDialogMessage(String message, int checkBox);
    }
}