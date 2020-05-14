package com.example.statsdelivery.infrastructure;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.statsdelivery.ApiRequestStatus;
import com.example.statsdelivery.User;
import com.example.statsdelivery.infrastructure.request.ApiRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by drekerd (Mário Silva) or 10/05/2020
 */
public class GetDivisionJsonData extends AsyncTask<String, Void, User> implements ApiRequest.OnDownloadComplete {
    private static final String TAG = "GetDivisionJsonData";

    private User mUser;
    private String mBaseUrl;
    private ApiRequestStatus.Status mDownloadStatus;
    private boolean mRunningOnTheSameThread = false;

    private final OnDownloadAvailable mCallBack;

    public interface OnDownloadAvailable {
        void onDownloadAvailable(User user, ApiRequestStatus.Status status);
    }

    @Override
    public void onDownloadComplete(String data, ApiRequestStatus.Status status) {
        Log.d(TAG, "onDownloadComplete: starts");

        if (status == ApiRequestStatus.Status.OK) {

            try {
                //TODO try with GSON Builder
                JSONObject jsonData = new JSONObject(data);
                String userName = jsonData.getString("userName");
                String timePlayed = jsonData.getString("timePlayed");
                String currentSpecialization = jsonData.getString("currentSpecialization");
                String highestCharLevel = jsonData.getString("highestCharLevel");

                //TODO Create Builder for this USER
                mUser = new User(userName, timePlayed, currentSpecialization, highestCharLevel);

                mDownloadStatus = ApiRequestStatus.Status.OK;
                Log.d(TAG, "onDownloadComplete: parsing object success " + mDownloadStatus);
            } catch (JSONException jsone) {
                mDownloadStatus = ApiRequestStatus.Status.FAILED_OR_EMPTY;
                jsone.printStackTrace();
                Log.d(TAG, "onDownloadComplete: Error Reading Json: " + mDownloadStatus + " " + jsone.getMessage());
            }
        }
        if (mRunningOnTheSameThread && mCallBack != null) {
            mCallBack.onDownloadAvailable(mUser, mDownloadStatus);
        }
        Log.d(TAG, "onDownloadComplete: ends");
    }

    public GetDivisionJsonData(OnDownloadAvailable onDownloadAvailableCallBack, String baseUrl) {
        mCallBack = onDownloadAvailableCallBack;
        this.mBaseUrl = baseUrl;
    }

    public void executeOnSameThread(String searchCriteria) {
        Log.d(TAG, "executeOnSameThread: starts");
        mRunningOnTheSameThread = true;
        String destinationUrl = createUri(searchCriteria, mBaseUrl);

        ApiRequest apiRequest = new ApiRequest(this);
        apiRequest.execute(destinationUrl);
        Log.d(TAG, "executeOnSameThread: ends");
    }

    @Override
    protected void onPostExecute(User user) {
        Log.d(TAG, "onPostExecute: starts");

        if (mCallBack != null) {
            mCallBack.onDownloadAvailable(mUser, ApiRequestStatus.Status.OK);
        }

        Log.d(TAG, "onPostExecute: ends");
    }

    @Override
    protected User doInBackground(String... strings) {
        Log.d(TAG, "doInBackground: starts");
        String destinationUrl = createUri(strings[0], mBaseUrl);

        ApiRequest apiRequest = new ApiRequest(this);
        apiRequest.runInSameThread(destinationUrl);
        Log.d(TAG, "executeOnSameThread: ends");
        Log.d(TAG, "RunningOnTheSameThread STATUS = " + mRunningOnTheSameThread) ;
        return mUser;
    }

    private String createUri(String searchCriteria, String baseUrl) {
        Log.d(TAG, "createUri");

        String uri = Uri.parse(baseUrl).buildUpon()
                .appendEncodedPath(searchCriteria)
                .build().toString();

        Log.d(TAG, "createUri: uri = " + uri);
        Log.d(TAG, "createUri: ends");
        return uri;
    }

}
