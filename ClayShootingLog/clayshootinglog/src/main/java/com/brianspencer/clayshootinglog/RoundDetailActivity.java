package com.brianspencer.clayshootinglog;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RoundDetailActivity extends FragmentActivity implements RoundDetailFragment.DetailCallbacks {

    private DatePickerFragment datePicker;
    private RoundDetailFragment fragment;
    private String mRoundItem_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_detail);

        // Show the Up button in the action bar.
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            mRoundItem_ID = getIntent().getStringExtra(RoundDetailFragment.ARG_ITEM_ID);
            arguments.putString(RoundDetailFragment.ARG_ITEM_ID, mRoundItem_ID);
            fragment = new RoundDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.round_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        if(mRoundItem_ID==null)
            inflater.inflate(R.menu.actionbar_savecancel,menu);
        else
            inflater.inflate(R.menu.actionbar_savecanceldelete,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onDateSelected(Date roundDate) {
        datePicker = new DatePickerFragment();
        final Calendar calender = Calendar.getInstance();
        calender.setTime(roundDate);
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        datePicker.setArguments(args);
        datePicker.setCallBack(ondate);
        datePicker.show(getSupportFragmentManager(), "Date Picker");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                closeScreen();
                return true;
            case R.id.action_add:
                addData();
                return true;
            case R.id.action_save:
                saveData();
                return true;
            case R.id.action_cancel:
                closeScreen();
                return true;
            case R.id.action_delete:
                deleteData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteData(){
        RoundContent.deleteItem(fragment.mItem);
        closeScreen();
    }

    private void addData(){
        //fragment.mItem
        //fragment.mRoundItem_ID
        //Add record
        int nextID = RoundContent.currentID+1;

        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date roundDate = new Date();
        try {
            roundDate = dateFormat.parse(fragment.mRoundDate.getText().toString());
        }
        catch(ParseException e){
        }

        int roundType = 1;
        if(fragment.rdoSkeet.isChecked())
            roundType = 1;
        if(fragment.rdoSport.isChecked())
            roundType = 2;
        if(fragment.rdoTrap.isChecked())
            roundType = 3;


        RoundContent.addItem(new RoundContent.RoundItem(String.valueOf(nextID),
                roundDate,
                fragment.npRoundNum.getValue(),
                fragment.npHits.getValue(),
                roundType));

        closeScreen();

    }

    private void saveData(){

        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date roundDate = new Date();
        try {
            roundDate = dateFormat.parse(fragment.mRoundDate.getText().toString());
        }
        catch(ParseException e){
        }
        fragment.mItem.shootDate = roundDate;
        fragment.mItem.roundNum = fragment.npRoundNum.getValue();
        fragment.mItem.hits = fragment.npHits.getValue();

        if(fragment.rdoSkeet.isChecked())
            fragment.mItem.type = 1;
        if(fragment.rdoSport.isChecked())
            fragment.mItem.type = 2;
        if(fragment.rdoTrap.isChecked())
            fragment.mItem.type = 3;

        RoundContent.saveItems();

        closeScreen();
    }

    private void closeScreen(){
        NavUtils.navigateUpTo(this, new Intent(this, RoundListActivity.class));
    }


    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month,
                              int day) {
            Calendar cal = Calendar.getInstance();
            cal.set(year,month,day);
            fragment.setRoundDate(cal.getTime());
        }
    };
}
