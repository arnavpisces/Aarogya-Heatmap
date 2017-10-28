package com.hypertrack.usecases.locationBasedAssignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.hypertrack.lib.HyperTrack;
import com.hypertrack.usecases.LoginActivity;
import com.hypertrack.usecases.R;
import com.hypertrack.usecases.util.BaseActivity;
import com.hypertrack.usecases.util.SharedPreferenceStore;

public class LocationBasedAssignment extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workforce_monitoring);

        SharedPreferenceStore.setUseCase(this,3);
        // Initialize UI Views
        initUIViews();
        /**
         * @IMPORTANT:
         * Implement Network call to fetch ORDERS/TRANSACTIONS for the
         * Driver/Sales Person/Delivery Boy here.
         * Once the list of orders/transactions have been fetched, implement
         * assignAction and completeAction calls either with or without user interaction
         * depending on the specific requirements in the workflow of your business and your app.
         */
    }

    private void initUIViews() {

        //Set toolbar title
        initToolbar(getString(R.string.location_based_assignment), false);
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
        Toast.makeText(LocationBasedAssignment.this, R.string.main_logout_success_msg, Toast.LENGTH_SHORT).show();

        // Stop mock tracking session of a user
        //Refer here for more detail https://docs.hypertrack.com/sdks/android/reference/hypertrack.html#void-stopmocktracking
        HyperTrack.stopMockTracking();

        // Clear Driver/Sales Person/Delivery Boy Session here
        SharedPreferenceStore.clearIDs(this);

        // Proceed to LoginActivity for a fresh Login
        Intent loginIntent = new Intent(this, LoginActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        loginIntent.putExtra("use_case",3);
        startActivity(loginIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
