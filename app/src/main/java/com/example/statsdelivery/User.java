package com.example.statsdelivery;

/**
 * Created by drekerd (Mário Silva) or 10/05/2020
 */
public class User {

    private String mUserName;
    private String mTimePlayed;
    private String mCurrentSpecialization;
    private String mHighestCharLevel;

    public User(String userName, String timePlayed, String currentSpecialization, String highestCharLevel) {
        mUserName = userName;
        mTimePlayed = timePlayed;
        mCurrentSpecialization = currentSpecialization;
        mHighestCharLevel = highestCharLevel;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getTimePlayed() {
        return mTimePlayed;
    }

    public String getCurrentSpecialization() {
        return mCurrentSpecialization;
    }

    public String getHighestCharLevel() {
        return mHighestCharLevel;
    }

    @Override
    public String toString() {
        return "User{" +
                "mUserName='" + mUserName + '\'' +
                ", mTimePlayed='" + mTimePlayed + '\'' +
                ", mCurrentSpecialization='" + mCurrentSpecialization + '\'' +
                ", mHighestCharLevel='" + mHighestCharLevel + '\'' +
                '}';
    }
}
