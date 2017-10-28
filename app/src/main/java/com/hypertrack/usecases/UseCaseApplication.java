package com.hypertrack.usecases;

import android.app.Application;
import android.util.Log;

import com.hypertrack.lib.HyperTrack;

/**
 * Created by Aman Jain on 15/05/17.
 */

public class UseCaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize HyperTrack SDK with the Publishable Key
        // Refer to documentation at https://docs.hypertrack.com/gettingstarted/authentication.html#publishable-key
        // @NOTE: Add **YOUR_PUBLISHABLE_KEY** here for SDK to be authenticated with HyperTrack Server
        HyperTrack.initialize(this, "pk_test_8f1fd6b7d4ba368c46b1a41148e955affc76a35b");

        // Enable HyperTrack SDK Debug Logging, if required
        // Refer to the details
        HyperTrack.enableDebugLogging(Log.VERBOSE);
    }
}
