package com.brianspencer.clayshootinglog;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by bspencer on 2/18/14.
 */
public class DatePickerFragment extends DialogFragment {
    OnDateSetListener ondateSet;

    private int mYear;
    private int mMonth;
    private int mDay;

    public DatePickerFragment(){

    }

    public void setCallBack(OnDateSetListener ondate){
        ondateSet = ondate;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        mYear = args.getInt("year");
        mMonth = args.getInt("month");
        mDay = args.getInt("day");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DatePickerDialog datePicker = new DatePickerDialog(getActivity(), ondateSet, mYear, mMonth, mDay);
        datePicker.getDatePicker().setCalendarViewShown(true);
        datePicker.getDatePicker().setSpinnersShown(false);
        return datePicker;
    }
}
