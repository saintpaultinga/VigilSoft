package com.pindelia.android.vigilsoft;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.edlogin)
    EditText mUsername_ed;
    @BindView(R.id.edPassword)
    EditText mPassword_ed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
    }

    /**
     * Use to validate the login and the password the user
     * entered
     * @return a boolean value of True or False
     */
    private boolean isCredentialValid() {
        if (TextUtils.isEmpty(mUsername_ed.getText())) {
            Toast.makeText(this,getResources().getString(R.string.enter_valid_username), Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(mPassword_ed.getText())) {
            Toast.makeText(this, getResources().getString(R.string.enter_valid_password), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    public void onButtonClicked(View view) {
        Intent intent = new Intent(this, ChoiceActivity.class);
        startActivity(intent);
    }
}
