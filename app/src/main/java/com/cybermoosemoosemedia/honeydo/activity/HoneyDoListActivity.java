package com.cybermoosemoosemedia.honeydo.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import com.cybermoosemoosemedia.honeydo.R;
import com.cybermoosemoosemedia.honeydo.adapter.HoneyDoCursorAdapter;
import com.cybermoosemoosemedia.honeydo.adapter.HoneyDoRemindersDbAdapter;
import com.cybermoosemoosemedia.honeydo.fragment.DatePickerFragment;
import com.cybermoosemoosemedia.honeydo.fragment.EditNameDialogFragment;
import com.cybermoosemoosemedia.honeydo.model.HoneyDoDataModel;

import java.util.Calendar;

public class HoneyDoListActivity extends FragmentActivity implements DatePickerDialog.OnDateSetListener, EditNameDialogFragment.Communicator {
    DatePickerFragment newFragment = new DatePickerFragment();
    int nId;
    HoneyDoDataModel honeyDoDataModel;
    private HoneyDoRemindersDbAdapter mDbAdapter;
    private HoneyDoCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_honey_do_list);

        ListView mListView = (ListView) findViewById(R.id.reminders_list_view);
        mDbAdapter = new HoneyDoRemindersDbAdapter(this);
        mDbAdapter.open();

        Cursor cursor = mDbAdapter.fetchAllReminders();
        //from columns defined in the model
        String[] from = new String[]{HoneyDoRemindersDbAdapter.COL_CONTENT, HoneyDoRemindersDbAdapter.COL_MONTH, HoneyDoRemindersDbAdapter.COL_DAY, HoneyDoRemindersDbAdapter.COL_YEAR};
        //to the ids of views in the layout
        int[] to = new int[]{R.id.row_text, R.id.month, R.id.day, R.id.year};

        mCursorAdapter = new HoneyDoCursorAdapter(
                //context
                HoneyDoListActivity.this,
                //the layout of the row
                R.layout.list_row,
                //cursor
                cursor,
                //from columns defined in the model
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
                String[] modes = new String[]{"Edit Entry", "Delete Entry", "Add/Edit Due Date"};
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
                        Calendar dueCal = Calendar.getInstance();
                        dueCal.set(honeyDoDataModel.getYear(), honeyDoDataModel.getMonth() - 1, honeyDoDataModel.getDay());
                        Bundle dueDate = new Bundle();
                        dueDate.putLong("dueDate", dueCal.getTimeInMillis());

                        if (position == 0) {
                            FragmentManager fm = getSupportFragmentManager();
                            EditNameDialogFragment editNameDialogFragment = EditNameDialogFragment.newInstance(nId, honeyDoDataModel);
                            editNameDialogFragment.show(fm, "fragment_edit");
                        } else if (position == 1) {
                            mDbAdapter.deleteReminderById(getIdFromPosition(masterListPosition));
                            mCursorAdapter.changeCursor(mDbAdapter.fetchAllReminders());
                            //Datepicker Fragment
                        } else {
                            newFragment.setArguments(dueDate);
                            newFragment.show(getFragmentManager(), "datePicker");
                        }
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    //Returns ID of Item
    private int getIdFromPosition(int nC) {
        return (int) mCursorAdapter.getItemId(nC);
    }

    //Onlick listener from Add Item Button
    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.editText);
        String itemText = etNewItem.getText().toString();
        mDbAdapter.createReminder(itemText, true, null,null,null);
        mCursorAdapter.changeCursor(mDbAdapter.fetchAllReminders());
        etNewItem.setText("");
    }


    //Returned from DatePickerFragment
    public void onDateSet(DatePicker view, int year, int month, int day) {
        //set date
        fireDate(honeyDoDataModel, day, month + 1, year);
    }


    //Change date on record
    private void fireDate(final HoneyDoDataModel reminder, int day, int month, int year) {
                    //add date to record
                    HoneyDoDataModel reminderEdited = new HoneyDoDataModel(reminder.getId(),
                            reminder.getContent(), reminder.getImportant(), day, month, year);
                    //update record
                    mDbAdapter.updateReminder(reminderEdited);
                    mCursorAdapter.changeCursor(mDbAdapter.fetchAllReminders());
            }

    //Returned from Dialog Fragment
    @Override
    public void onDialogMessage(String message, int checked) {
        // Toast.makeText(this, message + " " + checked, Toast.LENGTH_SHORT).show();
        fireEdited(honeyDoDataModel,checked, message);
    }

    //Edit record after returning from Dialog Fragment
    private void fireEdited (final HoneyDoDataModel reminder, int checked, String itemText) {
        Integer day, month, year;

        //Issue with 0s showing up in edited records. Resolved by setting nulls.
        if (reminder.getDay()==0)
            {day = null;}
        else
            {day = reminder.getDay();}

        if (reminder.getMonth()==0)
            {month = null;}
        else
            {month= reminder.getMonth();}

        if (reminder.getYear()==0)
            {year = null;}
        else
            {year = reminder.getYear();}

        HoneyDoDataModel reminderEdited = new HoneyDoDataModel(reminder.getId(),
                itemText, checked, day, month, year);
        mDbAdapter.updateReminder(reminderEdited);
        mCursorAdapter.changeCursor(mDbAdapter.fetchAllReminders());
    }
}
