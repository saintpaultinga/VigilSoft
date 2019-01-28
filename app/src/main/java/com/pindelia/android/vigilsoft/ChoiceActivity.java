package com.pindelia.android.vigilsoft;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChoiceActivity extends AppCompatActivity {

    private static final String TAG = "ChoiceActivity";
    @BindView(R.id.hotel_selected) RadioButton hotelRadioButton;
    @BindView(R.id.sec_selected) RadioButton vigilRadioButton;
    private boolean isHotelSelected;
    private boolean isVigilSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        ButterKnife.bind(this);
    }


    public void onNextButtonClicked(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        // TODO : sent the user choice data to the next activity to be used inside
        // TODO : the logic there
        Log.d(TAG, "onNextButtonClicked: ---next button is clicked---");
        startActivity(intent);
    }

    public void onVigilSelected(View view) {
        if (!isVigilSelected) {
            vigilRadioButton.setChecked(true);
            isVigilSelected = true;
            if (isHotelSelected) {
                hotelRadioButton.setChecked(false);
                isHotelSelected = false;
            }
        } else {
            vigilRadioButton.setChecked(false);
            isVigilSelected = false;
        }
    }

    public void onHotelSelected(View view) {
        if (!isHotelSelected) {
            hotelRadioButton.setChecked(true);
            isHotelSelected = true;
            if (isVigilSelected) {
                vigilRadioButton.setChecked(false);
                isVigilSelected = false;
            }
        } else {
            hotelRadioButton.setChecked(false);
            isHotelSelected = false;
        }
    }
}
