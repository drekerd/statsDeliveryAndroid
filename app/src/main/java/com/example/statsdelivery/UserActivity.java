package com.example.statsdelivery;

import android.os.Bundle;

import com.example.statsdelivery.infrastructure.GetDivisionJsonData;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UserActivity extends AppCompatActivity implements GetDivisionJsonData.OnDownloadAvailable{

    private static final String TAG = "UserActivity";

    private User mUser;
    private TextView mUserNameTextView;
    private TextView mTimePlayedView;
    private TextView mCurrentSpecializationView;
    private TextView mHighestCharLevelView;
    private Button mSearchButton;
    private EditText mSearchEditText;
    private String mUserNameToSearch;
    private String mTempBaseUrl = "http://192.168.1.2:8080/division2/xbox/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mUserNameTextView = findViewById(R.id.user_name_textView);
        mTimePlayedView = findViewById(R.id.user_time_played_TextView);
        mCurrentSpecializationView = findViewById(R.id.current_specialization_TextView);
        mHighestCharLevelView = findViewById(R.id.current_level_textView);
        mSearchButton = findViewById(R.id.search_user_name_button);
        mSearchEditText = findViewById(R.id.search_user_name_editText);

        String userName = getIntent().getExtras().getString("userName");
        String timePlayed = getIntent().getExtras().getString("timePlayed");
        String currentSpecialization = getIntent().getExtras().getString("currentSpecialization");
        String highestCharLevel = getIntent().getExtras().getString("highestCharLevel");

        this.mUser = new User(userName, timePlayed, currentSpecialization, highestCharLevel);

        setUserToView(this.mUser);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserNameToSearch();
                searchNewUser();
            }
        });
    }

    private void searchNewUser(){
        Log.d(TAG, "onResume: starts");
        super.onResume();
        GetDivisionJsonData getDivisionJsonData = new GetDivisionJsonData(this, mTempBaseUrl);
        getDivisionJsonData.execute(mUserNameToSearch);

        Log.d(TAG, "onResume: ends");
    }


    @Override
    public void onDownloadAvailable(User user, ApiRequestStatus.Status status) {
        if (status == ApiRequestStatus.Status.OK) {
            Log.d(TAG, "onDownloadAvailable: data is : " + user);
            setUserToView(user);
            this.mUser = user;
        } else {
            Log.d(TAG, "onDownloadAvailable: failed with status " + status);
        }
    }

    private void setUserToView(User user){
        mUserNameTextView.setText(user.getUserName());
        mTimePlayedView.setText(user.getTimePlayed());
        mCurrentSpecializationView.setText(user.getCurrentSpecialization());
        mHighestCharLevelView.setText(user.getHighestCharLevel());
    }

    private void getUserNameToSearch() {
        this.mUserNameToSearch = mSearchEditText.getText().toString();
    }
}
