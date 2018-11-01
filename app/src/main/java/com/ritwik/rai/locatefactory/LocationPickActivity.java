package com.ritwik.rai.locatefactory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.ritwik.rai.locatefactory.volley.ServerResponseCallback;
import com.ritwik.rai.locatefactory.volley.VolleyTaskManager;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class LocationPickActivity extends AppCompatActivity implements LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ServerResponseCallback {

    private Context mContext;
    // Location Components
    private ProgressDialog mProgressDialog;
    private Location mCurrentLocation;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderApi fusedLocationProviderApi;
    private GoogleApiClient mGoogleApiClient;
    private AlertDialog systemAlertDialog;
    private String predictedBatchQuantity = "";
    private EditText et_factoryName;
    private TextView tv_latitude;
    private TextView tv_longitude;
    private VolleyTaskManager volleyTaskManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiyt_pick_location);
        mContext = LocationPickActivity.this;
        et_factoryName = (EditText) findViewById(R.id.et_factoryName);
        tv_latitude = (TextView) findViewById(R.id.tv_latitude);
        tv_longitude = (TextView) findViewById(R.id.tv_longitude);
        mProgressDialog = new ProgressDialog(LocationPickActivity.this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        volleyTaskManager = new VolleyTaskManager(mContext);
    }

    // ==== LOCATION ====
    public void onLocationClicked(View view) {
        locationUpdateCount = 0;
        mCurrentLocation = null; // Setting present location null, so that new
        // location value can be fetched on each
        // button click.
        checkingLocation();

    }
// ========== Location Related implemented methods ========== -->

    private void checkingLocation() {

        // if (Util.checkConnectivity(EntryFormActivity.this)) {

        if (!isGooglePlayServicesAvailable()) {
            int requestCode = 10;
            int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(LocationPickActivity.this);
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, LocationPickActivity.this, requestCode);
            dialog.show();
        } else {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                Log.e("EntryFormActivity", "NO LOCATION FOUND!");
                // Util.buildAlertMessageNoGps( StartJourneyActivity.this );
                if (systemAlertDialog == null) {
                    systemAlertDialog = Util.showSettingsAlert(getApplicationContext(), systemAlertDialog);
                } else if (!systemAlertDialog.isShowing()) {
                    systemAlertDialog = Util.showSettingsAlert(getApplicationContext(), systemAlertDialog);
                }
            } else {
                Log.v("GPS Connection Found:", "true");
                if (mCurrentLocation == null) {
                    mProgressDialog.setMessage("Fetching present location...");
                    mProgressDialog.setCancelable(true);
                    mProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            if (mGoogleApiClient != null)
                                mGoogleApiClient.disconnect();
                        }
                    });
                    showProgressDialog();
                    createLocationRequest();
                } else {
                    // Toast.makeText(EntryFormActivity.this,
                    // "Location already found.", Toast.LENGTH_SHORT).show();
                }
            }
        }
        // } else {
        // // Util.showMessageWithOk(EntryFormActivity.this,
        // getString(R.string.no_internet));
        // Log.v("Connection Status:", "No internet");
        // }

    }

    protected void createLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setNumUpdates(6); // TODO New line added
        mLocationRequest.setInterval(5000); // TODO New line added
        mLocationRequest.setFastestInterval(1000); // TODO New line added
        fusedLocationProviderApi = LocationServices.FusedLocationApi;
        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("EntryFormActivity", "Connection failed: " + connectionResult.toString());
        Toast.makeText(LocationPickActivity.this, "Connection failed: " + connectionResult.toString(), Toast.LENGTH_LONG)
                .show();
        hideProgressDialog();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("EntryFormActivity", "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        // PendingResult<Status> pendingResult =
        // LocationServices.FusedLocationApi.requestLocationUpdates(
        // mGoogleApiClient, mLocationRequest, this);
        fusedLocationProviderApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        Log.d("EntryFormActivity", "Location update started ..............: ");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private static int locationUpdateCount = 0;

    @Override
    public void onLocationChanged(Location location) {
        Log.d("EntryFormActivity", "Firing onLocationChanged..............................................");
        locationUpdateCount = locationUpdateCount + 1;
        mCurrentLocation = location;
        int threshholdAccuracy = 15; // adjust your need
        if (location.hasAccuracy() && location.getAccuracy() <= threshholdAccuracy) {
            // This is your most accurate location.
            hideProgressDialog();
            updateUI();
            mGoogleApiClient.disconnect();
        }
        if (locationUpdateCount > 5) {
            locationUpdateCount = 0;
            hideProgressDialog();
            mGoogleApiClient.disconnect();
            if (location.getAccuracy() > threshholdAccuracy)
                Util.showMessageWithOk(LocationPickActivity.this, "Fetched location is not accurate enough. Please try again.");
        }
    }

    private void updateUI() {
        Log.d("EntryFormActivity", "UI update initiated .............");
        SimpleDateFormat postFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        if (null != mCurrentLocation) {
            String lat = String.valueOf(mCurrentLocation.getLatitude());
            String lng = String.valueOf(mCurrentLocation.getLongitude());
            Log.v("LOCATION", "At Time: " + postFormater.format(new Date()) + "\n" + "Latitude: " + lat + "\n"
                    + "Longitude: " + lng + "\n" + "Accuracy: " + mCurrentLocation.getAccuracy() + "\n" + "Provider: "
                    + mCurrentLocation.getProvider());

            tv_latitude.setText(lat);
            tv_longitude.setText(lng);
        } else {
            Log.d("EntryFormActivity", "location is null ...............");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCurrentLocation != null) {
            stopLocationUpdates();
        }
        Util.trimCache(LocationPickActivity.this);
    }

    protected void stopLocationUpdates() {
        if (mGoogleApiClient.isConnected())
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        Log.d("EntryFormActivity", "Location update stopped .......................");
    }

    private void showProgressDialog() {
        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    public void onSaveLocationClick(View view) {

        String factoryName = et_factoryName.getText().toString().trim();
        String latitude = tv_latitude.getText().toString();
        String longitude = tv_longitude.getText().toString();
        if (TextUtils.isEmpty(factoryName)) {
            Util.showMessageWithOk(mContext, "Enter Factory Name");
            return;
        } else if (TextUtils.isEmpty(latitude)) {
            Util.showMessageWithOk(mContext, "Enter Factory Location");
            return;
        }

        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("latitude", "" + latitude);
        requestMap.put("longitude", "" + longitude);
        requestMap.put("factoryName", "" + factoryName);
        requestMap.put("device", "" + getDeviceModel());
        volleyTaskManager.doPostFactoryLocation(requestMap, true);
    }

    @Override
    public void onSuccess(JSONObject resultJsonObject) {
        Log.e("onSuccess", "Server Response: " + resultJsonObject);
        if (resultJsonObject.optString("status").trim().equalsIgnoreCase("1")) {
            Util.showMessageWithOk(mContext, "" + resultJsonObject.optString("msg"));
            clearForm();

        } else if (resultJsonObject.optString("status").trim().equalsIgnoreCase("0")) {
            Util.showMessageWithOk(mContext, "" + resultJsonObject.optString("msg"));
        } else {
            Util.showMessageWithOk(mContext, "Something went wrong! Please try again.");
        }
    }

    private void clearForm() {
        et_factoryName.setText("");
        tv_latitude.setText("");
        tv_longitude.setText("");
    }

    @Override
    public void onError() {

    }

    private String getDeviceModel() {

        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;

    }

    private String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        //	        String phrase = "";
        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                //	                phrase += Character.toUpperCase(c);
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            //	            phrase += c;
            phrase.append(c);
        }

        return phrase.toString();
    }
}
