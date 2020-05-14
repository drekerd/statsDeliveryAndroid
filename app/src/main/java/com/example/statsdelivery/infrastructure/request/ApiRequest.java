package com.example.statsdelivery.infrastructure.request;

import android.os.AsyncTask;
import android.util.Log;

import com.example.statsdelivery.ApiRequestStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ApiRequest extends AsyncTask<String, Void, String> {

    private static final String TAG = "ApiRequest";

    private ApiRequestStatus.Status mRequestStatus;
    private final OnDownloadComplete mMainActivityCallBack;

    public interface OnDownloadComplete {
        void onDownloadComplete(String data, ApiRequestStatus.Status status);
    }

    public ApiRequest(OnDownloadComplete callBack) {
        this.mRequestStatus = ApiRequestStatus.Status.IDLE;
        mMainActivityCallBack = callBack;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "onPostExecute: starts");
        if (mMainActivityCallBack != null) {
            mMainActivityCallBack.onDownloadComplete(s, mRequestStatus);
        }
        Log.d(TAG, "onPostExecute: ends");
    }

    public void runInSameThread(String s) {
        Log.d(TAG, "runInSameThread: starts");

        if (mMainActivityCallBack != null) {
            mMainActivityCallBack.onDownloadComplete(doInBackground(s), mRequestStatus);
        }

        Log.d(TAG, "runInSameThread: ends");
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        if (strings == null) {
            mRequestStatus = ApiRequestStatus.Status.NOT_INITIALIZED;
            Log.d(TAG, "doInBackground: RequestStatus : " + mRequestStatus);
            return null;
        }

        try {
            //@TODO replace this with clean code GSON, retro
            mRequestStatus = ApiRequestStatus.Status.PROCESSING;
            Log.d(TAG, "doInBackground: RequestStatus : " + mRequestStatus);
            URL url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int response = connection.getResponseCode();
            Log.d(TAG, "doInBackground: The response code was " + response);

            StringBuilder result = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while (null != (line = reader.readLine())) {
                result.append(line).append("\n");
            }

            mRequestStatus = ApiRequestStatus.Status.OK;
            Log.d(TAG, "doInBackground: RequestStatus : " + mRequestStatus);
            Log.d(TAG, "doInBackground: responseBody : " + result);
            return result.toString();

        } catch (MalformedURLException e) {
            Log.e(TAG, "doInBackground: Invalid URL: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: IO Exception reading data: " + e.getMessage());
        } catch (SecurityException e) {
            Log.e(TAG, "doInBackground: Security Exception. Needs permission? " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.d(TAG, "doInBackground: Error closing Stream " + e.getMessage());
                }
            }
        }
        mRequestStatus = ApiRequestStatus.Status.FAILED_OR_EMPTY;
        return null;
    }
}
