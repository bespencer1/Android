package com.brianspencer.clayshootinglog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.EditText;
import android.widget.Toast;

import java.text.*;
import java.util.Calendar;
import java.util.Date;

/**
 * A fragment representing a single Round detail screen.
 * This fragment is either contained in a {@link RoundListActivity}
 * in two-pane mode (on tablets) or a {@link RoundDetailActivity}
 * on handsets.
 */
public class RoundDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";
    public RoundContent.RoundItem mItem;
    public String mRoundItem_ID;
    private DetailCallbacks mCallbacks = sDetailCallbacks;
    public EditText mRoundDate;
    public NumberPicker npRoundNum;
    public NumberPicker npHits;
    public NumberPicker npMiss;
    public RadioButton rdoTrap;
    public RadioButton rdoSkeet;
    public RadioButton rdoSport;
    private boolean mAddData = false;

    public RoundDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mRoundItem_ID = getArguments().getString(ARG_ITEM_ID);
            mItem = RoundContent.ITEM_MAP.get(mRoundItem_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_round_detail, container, false);

        mRoundDate = (EditText)rootView.findViewById(R.id.editRoundDate);
        mRoundDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                Date roundDate = new Date();
                try {
                    roundDate = dateFormat.parse(mRoundDate.getText().toString());
                }
                catch(ParseException e){
                }
                mCallbacks.onDateSelected(roundDate);
            }
        });

        npRoundNum = (NumberPicker) rootView.findViewById(R.id.npRoundNum);
        npRoundNum.setMaxValue(50);
        npRoundNum.setMinValue(1);

        npHits = (NumberPicker) rootView.findViewById(R.id.npHits);
        npHits.setMaxValue(25);
        npHits.setMinValue(0);
        npHits.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                npMiss.setValue(25-newVal);
            }
        });

        npMiss = (NumberPicker) rootView.findViewById(R.id.npMiss);
        npMiss.setMaxValue(25);
        npMiss.setMinValue(0);
        npMiss.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                npHits.setValue(25-newVal);
            }
        });

        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        rdoTrap = ((RadioButton) rootView.findViewById(R.id.rdoTrap));
        rdoSport = ((RadioButton) rootView.findViewById(R.id.rdoSport));
        rdoSkeet = ((RadioButton) rootView.findViewById(R.id.rdoSkeet));

        if (mItem != null) {

            mRoundDate.setText(dateFormat.format(mItem.shootDate));
            npRoundNum.setValue(mItem.roundNum);
            npHits.setValue(mItem.hits);
            npMiss.setValue(25-mItem.hits);

            if(mItem.type == 3)
                rdoTrap.setChecked(true);
            if(mItem.type == 2)
                rdoSport.setChecked(true);
            if(mItem.type == 1)
               rdoSkeet.setChecked(true);
        }
        else
        {
            mAddData = true;
            Date currentDate = new Date();
            mRoundDate.setText(dateFormat.format(currentDate));
            npRoundNum.setValue(1);
            npHits.setValue(0);
            npMiss.setValue(0);
            rdoTrap.setChecked(true);
            rdoSport.setChecked(false);
            rdoSkeet.setChecked(false);
        }

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof DetailCallbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (DetailCallbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the implementation.
        mCallbacks = sDetailCallbacks;
    }

    public void setRoundDate(Date date){
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        mRoundDate.setText(dateFormat.format(date));
    }

    public interface DetailCallbacks {
        public void onDateSelected(Date roundDate);
    }

    private static DetailCallbacks sDetailCallbacks = new DetailCallbacks() {
        @Override
        public void onDateSelected(Date roundDate) {
        }
    };
}
