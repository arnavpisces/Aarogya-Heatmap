package com.hypertrack.usecases.mileageTracking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import java.util.Random;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.google.android.gms.maps.model.LatLng;
import com.hypertrack.lib.HyperTrack;
import com.hypertrack.lib.callbacks.HyperTrackCallback;
import com.hypertrack.lib.models.Action;
import com.hypertrack.lib.models.ActionParams;
import com.hypertrack.lib.models.ActionParamsBuilder;
import com.hypertrack.lib.models.ErrorResponse;
import com.hypertrack.lib.models.Place;
import com.hypertrack.lib.models.SuccessResponse;
import com.hypertrack.usecases.LoginActivity;
import com.hypertrack.usecases.R;
import com.hypertrack.usecases.util.BaseActivity;
import com.hypertrack.usecases.util.SharedPreferenceStore;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class MileageTracking extends BaseActivity {

    private ProgressDialog mProgressDialog;
    private View.OnClickListener createActionButtonListener, completeActionButtonListener,
            showMileageButtonListener;
    private Button createActionButton, completeActionButton, showMileageButton;
    private TextView mileageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mileage_tracking);

        SharedPreferenceStore.setUseCase(this, 2);
        // Initialize UI Views
        initUIViews();
        /**
         * @IMPORTANT:
         * Implement Network call to fetch ORDERS/TRANSACTIONS
         * for the Driver/Sales Person/Delivery Boy here.
         * Once the list of orders/transactions have been fetched, implement
         * assignAction and completeAction calls either with or without user interaction
         * depending on the specific requirements in the workflow of your business and your app.
         */
    }

    private void initUIViews() {
        // Initialize Click Listeners for Action buttons
        initClickListeners();

        // Initialize createAction Button
        createActionButton = (Button) findViewById(R.id.create_action);
        if (createActionButton != null)
            createActionButton.setOnClickListener(createActionButtonListener);

        // Initialize complete Button
        completeActionButton = (Button) findViewById(R.id.complete_action);
        if (completeActionButton != null)
            completeActionButton.setOnClickListener(completeActionButtonListener);

        showMileageButton = (Button) findViewById(R.id.show_mileage);
        showMileageButton.setVisibility(View.VISIBLE);
        if (showMileageButton != null)
            showMileageButton.setOnClickListener(showMileageButtonListener);

        mileageText = (TextView) findViewById(R.id.mileage);

        initToolbar(getString(R.string.mileage_tracking), false);


    }

    private void initClickListeners() {
        // Click Listener for create Action Button
        createActionButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Show Progress Dialog
                mProgressDialog = new ProgressDialog(MileageTracking.this);
                mProgressDialog.setCancelable(false);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.show();

                /**
                 * You can specify a lookup_id to Actions which maps to your internal id of the
                 * order that is going to be tracked. This will help you search for the order on
                 * HyperTrack dashboard, and get custom views for the specific order tracking.
                 *
                 * @NOTE: A randomly generated UUID is used as the lookup_id here.
                 * This will be the actual orderID in your case which will be fetched from
                 * either your server or generated locally.
                 */
                final String orderID = UUID.randomUUID().toString();

                /**
                 * Create Actions for current action for order tracking with a given OrderID
                 */
                createAction(orderID,null);
            }
        };

        // Click Listener for complete Action Button
        completeActionButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String actionID = SharedPreferenceStore.getActionID(MileageTracking.this);

                if (TextUtils.isEmpty(actionID)) {
                    Toast.makeText(MileageTracking.this, "Action Id is empty.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                /*
                 * Complete action using actionId for the order tracking action that you created
                 *
                 * Refer here for more detail https://docs.hypertrack.com/sdks/android/reference/action.html#complete-action
                 *
                 * */
                HyperTrack.completeAction(actionID);

                createActionButton.setEnabled(true);
                completeActionButton.setEnabled(false);

                Toast.makeText(MileageTracking.this, "Action Completed", Toast.LENGTH_SHORT).show();

            }

        };

        //Click Listener to get Mileage Tracked
        showMileageButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mProgressDialog != null) {
                    mProgressDialog.show();
                }

                String actionID = SharedPreferenceStore.getActionID(MileageTracking.this);

                if (TextUtils.isEmpty(actionID)) {
                    Toast.makeText(MileageTracking.this, "Action Id is empty.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                /*
                * To get the distance travelled by Driver/Sales Person/Delivery Boy
                * Call this method to get action detail and from action object get
                * distance travelled.
                *
                * Refer here for more detail https://docs.hypertrack.com/sdks/android/reference/action.html#get-action
                *
                * */
                HyperTrack.getAction(actionID, new HyperTrackCallback() {
                    @Override
                    public void onSuccess(@NonNull SuccessResponse successResponse) {
                        if (mProgressDialog != null) {
                            mProgressDialog.cancel();
                        }
                        Action action = (Action) successResponse.getResponseObject();
                        if (action != null)
                            showMileageText(action);

                    }

                    @Override
                    public void onError(@NonNull ErrorResponse errorResponse) {

                    }
                });
            }
        };
    }

    /**
     * This method creates and assigns Delivery type actions to the current user on
     * HyperTrack API Server for the current orderID.
     *
     * @param orderID Internal order_id which maps to the current order tracking being performed
     */
    public  void createAction(String orderID, final HyperTrackCallback callback) {
        /**
         * Construct a place object for Action's expected place
         *
         * @NOTE: Either the coordinates of the expected delivering location
         * or it's address can be used to construct the expected place for the Action
         *
         * Refer here for more info https://docs.hypertrack.com/sdks/android/reference/place.html
         *
         */

//        Place expectedPlace = new Place().setLocation(28.546324, 77.273187)
//                .setAddress("IIITD, Okhla Phase 3")
//                .setName("IIITD");
//        for(int k=0;k<5;k++){
        for(int i=0;i<1;i++) {
            Place expectedPlace = new Place().setLocation(28.688426, 77.138136) //pitampura: for demonstration purpose only
                    .setName("Pitampura");

            orderID = UUID.randomUUID().toString();

//        LatLng destinationLatLng=new LatLng(28.546324, 77.273187);
            LatLng sourceLatLng = new LatLng(28.688426, 77.138136);
//            LatLng destinationLatLng = new LatLng(28.550048, 77.235670);
            HyperTrack.startMockTracking(sourceLatLng, sourceLatLng, null);

            /**
             *
             * Create ActionParams object specifying the TYPE_VISIT Action parameters
             * including ExpectedPlace, ExpectedAt time and Lookup_id.
             *
             * Refer here for more info https://docs.hypertrack.com/sdks/android/reference/action.html#create-and-assign-action
             */
            ActionParams createActionParams = new ActionParamsBuilder()
                    .setExpectedPlace(expectedPlace)
                    .setExpectedAt(new Date())
                    .setType(Action.ACTION_TYPE_VISIT)
                    .setLookupId(orderID)
                    .build();

            /**
             * Call createAndAssignAction to assign TYPE_VISIT to the current user configured
             * in the SDK using the ActionParams created above.
             *  Refer here for more info https://docs.hypertrack.com/sdks/android/reference/action.html#create-and-assign-action
             */
            HyperTrack.createAndAssignAction(createActionParams, new HyperTrackCallback() {
                @Override
                public void onSuccess(@NonNull SuccessResponse response) {

                    if (mProgressDialog != null)
                        mProgressDialog.cancel();

                    // Handle createAndAssignAction API success here
                    Action action = (Action) response.getResponseObject();

                    /**
                     * The TYPE_VISIT Action just created has the tracking url which can be
                     * shared with your customers. This will enable the customer to live track.
                     *
                     * @NOTE You can now share this tracking_url with your customers via an SMS
                     * or via your Customer app using in-app notifications.
                     */
                    String trackingUrl = action.getTrackingURL();

                    onCreateAndAssignActionSuccess(action);
                    if (callback != null) {
                        callback.onSuccess(new SuccessResponse(action.getId()));
                    }
                    Toast.makeText(MileageTracking.this, "Disease Marked Successfully",
                            Toast.LENGTH_SHORT).show();
                    String actionID = SharedPreferenceStore.getActionID(MileageTracking.this);

                    HyperTrack.completeAction(actionID);
                }

                @Override
                public void onError(@NonNull ErrorResponse errorResponse) {
                    if (mProgressDialog != null) {
                        mProgressDialog.dismiss();
                    }

                    Toast.makeText(MileageTracking.this, "Action assingment failed: " +
                            errorResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }
//        HyperTrack.stopMockTracking();
    }

    private void onCreateAndAssignActionSuccess(Action action) {
        //Disable Create Action Button and enable complete Action Button
        if (createActionButton!=null && completeActionButton!=null && showMileageButton!=null) {
            createActionButton.setEnabled(false);
            completeActionButton.setEnabled(true);
            showMileageButton.setVisibility(View.VISIBLE);
            showMileageButton.setEnabled(true);
        }
        SharedPreferenceStore.setActionID(MileageTracking.this, action.getId());
    }

    private void showMileageText(Action action) {
        if (mileageText == null)
            return;

        /**
         * action.getDistanceInKMS method returns distance travelled
         * by Driver/Sales Person/Delivery Boy in Kilometer.
         * Developer can call action.getDistance to get distance in meters.
         */
        Double distance = action.getDistanceInKMS();

        if (distance == null)
            distance = new Double(0);

        Integer duration = getCurrentDurationInMinutes(action);

        mileageText.setText("Distance : " + String.valueOf(distance) + " KM\nDuration : " +
                String.valueOf(duration) + " min");

    }

    /**
     * Call this method to get duration of action since the action has started to current time,
     * if action has completed then duration will calculate using started time of
     * action to completed time.
     *
     * @param action
     */
    public Integer getCurrentDurationInMinutes(Action action) {
        if (action.getStartedAT() == null) {
            return null;
        }

        if (action.getDurationInMinutes() != null) {
            return action.getDurationInMinutes();
        }

        Date currentTime = Calendar.getInstance().getTime();
        long duration = currentTime.getTime() - action.getStartedAT().getTime();
        double durationInMinutes = Math.ceil((duration / (float) 1000) / (float) 60);
        return (int) durationInMinutes;
    }

    /**
     * This method is called when Driver/Delivery Boy clicks on LOGOUT button in the toolbar.
     * On Logout, HyperTrack's stopMockTracking API is called to stop mock tracking the Driver/Delivery Boy session.
     * Note that this method is linked with the menu file (menu_main.xml)
     * using this menu item's onClick attribute. So no need to invoke this
     * method or handle logout button's click listener explicitly.
     *
     * @param menuItem
     */
    public void onLogoutClicked(MenuItem menuItem) {
        Toast.makeText(MileageTracking.this, R.string.main_logout_success_msg, Toast.LENGTH_SHORT).show();

        // Stop mock tracking session of a user
        //Refer here for more detail https://docs.hypertrack.com/sdks/android/reference/hypertrack.html#void-stopmocktracking
        HyperTrack.stopMockTracking();

        // Clear Driver/Sales Person/Delivery Boy Session here
        SharedPreferenceStore.clearIDs(this);

        // Proceed to LoginActivity for a fresh Login
        Intent loginIntent = new Intent(this, LoginActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        loginIntent.putExtra("use_case", 2);
        startActivity(loginIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}

//class xcors<E, F, G> {
//    public E First;
//    public F Second;
//    public G Third;
//}