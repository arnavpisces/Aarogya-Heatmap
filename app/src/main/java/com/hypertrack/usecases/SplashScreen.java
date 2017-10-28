package com.hypertrack.usecases;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.view.Window;

import com.hypertrack.usecases.locationBasedAssignment.LocationBasedAssignment;
import com.hypertrack.usecases.mileageTracking.MileageTracking;
import com.hypertrack.usecases.orderTracking.OrderTracking;
import com.hypertrack.usecases.util.BaseActivity;
import com.hypertrack.usecases.util.SharedPreferenceStore;
import com.hypertrack.usecases.workforceMonitoring.WorkforceMonitoring;


/**
 * Created by piyush on 21/03/17.
 */

public class SplashScreen extends BaseActivity {

    //    private static int USE_CASE_ORDER_TRACKING = 1;
    private static int USE_CASE_MILEAGE_TRACKING = 2;
//    private static int USE_CASE_LOCATION_BASED_ASSIGNMENT = 3;
//    private static int USE_CASE_WORKFORCE_MONITORING = 4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Check if Driver/Sales Person/Delivery Boy is logged in currently
        String driverId = SharedPreferenceStore.getDriverId(this);
        if (TextUtils.isEmpty(driverId)) {

            // Initiate Driver / Sales Person / Delivery Boy Login by starting LoginActivity
            TaskStackBuilder.create(SplashScreen.this)
                    .addNextIntentWithParentStack(new Intent(SplashScreen.this, UseCaseActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    .startActivities();
            finish();

        } else {
//            if (useCase == USE_CASE_MILEAGE_TRACKING) {
            Intent  intent = new Intent(SplashScreen.this, MileageTracking.class);
            startActivity(intent);
            finish();
        }
    }
}
