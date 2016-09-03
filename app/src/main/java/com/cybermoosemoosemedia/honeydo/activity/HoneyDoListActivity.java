package com.cybermoosemoosemedia.honeydo.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cybermoosemoosemedia.honeydo.R;
import com.cybermoosemoosemedia.honeydo.db.HoneyDoCursorAdapter;
import com.cybermoosemoosemedia.honeydo.db.HoneyDoDataModel;
import com.cybermoosemoosemedia.honeydo.db.HoneyDoRemindersDbAdapter;

public class HoneyDoListActivity extends FragmentActivity implements DatePickerDialog.OnDateSetListener {
    private ListView mListView;
    private HoneyDoRemindersDbAdapter mDbAdapter;
    private HoneyDoCursorAdapter mCursorAdapter;
    DatePickerFragment newFragment = new DatePickerFragment();

    int nId;
    Integer rDay; Integer rMonth; Integer rYear;
    HoneyDoDataModel honeyDoDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_honey_do_list);

        mListView = (ListView) findViewById(R.id.reminders_list_view);
        mDbAdapter = new HoneyDoRemindersDbAdapter(this);
        mDbAdapter.open();

        Cursor cursor = mDbAdapter.fetchAllReminders();
        //from columns defined in the db
        String[] from = new String[]{HoneyDoRemindersDbAdapter.COL_CONTENT,HoneyDoRemindersDbAdapter.COL_YEAR};
        //to the ids of views in the layout
        int[] to = new int[]{R.id.row_text,R.id.date_text};

        mCursorAdapter = new HoneyDoCursorAdapter(
                //context
                HoneyDoListActivity.this,
                //the layout of the row
                R.layout.list_row,
                //cursor
                cursor,
                //from columns defined in the db
                from,
                //to the ids of views in the layout
                to,
                //flag - not used
                0);

        mListView.setAdapter(mCursorAdapter);

        //when we click an individual item in the listview
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int masterListPosition, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HoneyDoListActivity.this);
                ListView modeListView = new ListView(HoneyDoListActivity.this);
                String[] modes = new String[]{"Edit Entry", "Delete Entry", "Add Due Date"};
                ArrayAdapter<String> modeAdapter = new ArrayAdapter<>(HoneyDoListActivity.this,
                        android.R.layout.simple_list_item_1, android.R.id.text1, modes);
                modeListView.setAdapter(modeAdapter);
                builder.setView(modeListView);
                final Dialog dialog = builder.create();
                dialog.show();
                modeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //edit reminder

                        nId = getIdFromPosition(masterListPosition);
                        honeyDoDataModel = mDbAdapter.fetchReminderById(nId);

                        if (position == 0) {
                            fireCustomDialog(honeyDoDataModel);
                        //delete honeyDoDataModel
                        } else if (position == 1){
                            mDbAdapter.deleteReminderById(getIdFromPosition(masterListPosition));
                            mCursorAdapter.changeCursor(mDbAdapter.fetchAllReminders());
                        //Datepicker Fragment
                        } else {
                            newFragment.show(getFragmentManager(), "datePicker");

                        }
                        dialog.dismiss();
                    }
                });
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            mListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean
                        checked) {
                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.cam_menu, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_item_delete_reminder:
                            for (int nC = mCursorAdapter.getCount() - 1; nC >= 0; nC--) {
                                if (mListView.isItemChecked(nC)) {
                                    mDbAdapter.deleteReminderById(getIdFromPosition(nC));
                                }
                            }
                            mode.finish();
                            mCursorAdapter.changeCursor(mDbAdapter.fetchAllReminders());
                            return true;
                    }
                    return false;
                }
                @Override
                public void onDestroyActionMode(ActionMode mode) {
                }
            });
        }
    }

    private int getIdFromPosition(int nC) {
        return (int) mCursorAdapter.getItemId(nC);
    }
    private void fireCustomDialog(final HoneyDoDataModel reminder) {


        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_custom);
        TextView titleView = (TextView) dialog.findViewById(R.id.custom_title);
        final EditText editCustom = (EditText) dialog.findViewById(R.id.custom_edit_reminder);
        Button commitButton = (Button) dialog.findViewById(R.id.custom_button_commit);
        final CheckBox checkBox = (CheckBox) dialog.findViewById(R.id.custom_check_box);
        LinearLayout rootLayout = (LinearLayout) dialog.findViewById(R.id.custom_root_layout);
        final boolean isEditOperation = (reminder != null);
        //this is for an edit
        if (isEditOperation) {
            titleView.setText("Edit Item");
            checkBox.setChecked(reminder.getImportant() == 1);
            editCustom.setText(reminder.getContent());
            rootLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        }


        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reminderText = editCustom.getText().toString();
                if (isEditOperation) {
                    HoneyDoDataModel reminderEdited = new HoneyDoDataModel(reminder.getId(),
                            reminderText, checkBox.isChecked() ? 1 : 0,reminder.getDay(),reminder.getMonth(),reminder.getYear());
                    mDbAdapter.updateReminder(reminderEdited);
                    //findViewById(R.id.date_text).setVisibility(View.INVISIBLE);
                    //this is for new reminder
                } else {
                    mDbAdapter.createReminder(reminderText, checkBox.isChecked(), 0,0,0);
                }
                mCursorAdapter.changeCursor(mDbAdapter.fetchAllReminders());
                dialog.dismiss();
            }
        });


        Button buttonCancel = (Button) dialog.findViewById(R.id.custom_button_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.editText);
        String itemText = etNewItem.getText().toString();
        mDbAdapter.createReminder(itemText, true, null,null,null);
        mCursorAdapter.changeCursor(mDbAdapter.fetchAllReminders());
        etNewItem.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reminders, menu);
        return true;
    }


    public void onDateSet(DatePicker view, int year, int month, int day) {
        //save day, month, year
        rDay = day;
        rMonth = month;
        rYear = year;

        //set date
        fireDate(honeyDoDataModel, nId);
    }



    private void fireDate(final HoneyDoDataModel reminder, final int masterListPosition) {
                    //add date to record
                    HoneyDoDataModel reminderEdited = new HoneyDoDataModel(reminder.getId(),
                            reminder.getContent(), reminder.getImportant(), rDay, rMonth, rYear);
                    //update record
                    mDbAdapter.updateReminder(reminderEdited);
                    mCursorAdapter.changeCursor(mDbAdapter.fetchAllReminders());
                    //clear date
                    rDay = 0; rMonth = 0; rYear =0;
            }
}
