package edu.orangecoastcollege.cs273.balbert.cs273superheroes;

/**
 * Created by brendantyleralbert on 10/17/17.
 */

public class Superhero {

    private String mName;
    private String mUserName;
    private String mSuperpower;
    private String mOneThing;
    private String mFileName;

    public String getName() {
        return mName;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getSuperpower() {
        return mSuperpower;
    }

    public String getOneThing() {
        return mOneThing;
    }

    public String getFileName() {
        return mFileName;
    }

    public Superhero(String name, String userName, String superpower, String oneThing) {
        mName = name;
        mUserName = userName;
        mSuperpower = superpower;
        mOneThing = oneThing;
        mFileName = mUserName + ".png";

    }
}
