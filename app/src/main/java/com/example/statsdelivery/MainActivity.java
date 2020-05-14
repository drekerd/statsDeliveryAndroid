package com.example.statsdelivery;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.statsdelivery.infrastructure.GetDivisionJsonData;
import com.example.statsdelivery.infrastructure.request.ApiRequest;


public class MainActivity extends AppCompatActivity implements GetDivisionJsonData.OnDownloadAvailable {

    private static final String TAG = "MainActivity";
    private ApiRequest apiRequest;

    private User mUserFromAsyncTask;

    private String mTempUserName = "drekerd";
    private String mTempBaseUrl = "http://192.168.1.2:8080/division2/xbox/";

    private Button mSearchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "OnCreate Starts");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSearchButton = findViewById(R.id.userSearchButton);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSearch(view);
            }
        });

        Log.d(TAG, "OnCreate ends");
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: starts");
        super.onResume();
        GetDivisionJsonData getDivisionJsonData = new GetDivisionJsonData(this, mTempBaseUrl);
        getDivisionJsonData.execute(mTempUserName);

        Log.d(TAG, "onResume: ends");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        Log.d(TAG, "onCreateOptionsMenu() returned: " + true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDownloadAvailable(User user, ApiRequestStatus.Status status) {
        if (status == ApiRequestStatus.Status.OK) {
            Log.d(TAG, "onDownloadStatus: data is : " + user);
            this.mUserFromAsyncTask = user;
        } else {
            Log.d(TAG, "onDownloadStatus: failed with status " + status);
        }
    }

    public void onClickSearch(View view) {
        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra("userName", this.mUserFromAsyncTask.getUserName());
        intent.putExtra("timePlayed", this.mUserFromAsyncTask.getTimePlayed());
        intent.putExtra("currentSpecialization", this.mUserFromAsyncTask.getCurrentSpecialization());
        intent.putExtra("highestCharLevel", this.mUserFromAsyncTask.getHighestCharLevel());
        startActivity(intent);
    }
}
