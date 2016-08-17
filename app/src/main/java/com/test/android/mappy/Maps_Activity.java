package com.test.android.mappy;



/*todo let the user's location be found using Google Play Services Location APIs
        will be returned as latitude and longitude coordinates.
  todo use user location to check how far he is from a particular location
       this can be done using the lat and long coordinates of that particular place.
       The coordinates are usually retrieved either through JAVA or javascript code into Google Maps
       These can be retrieved from the web, or from a local SQLite database.
  todo add custom markers

  todo refer https://developers.google.com/maps/?hl=en
  todo refer https://developers.google.com/maps/documentation/geocoding/intro
  geocoding key=AIzaSyCS8o88ooMW0yakjG6W3h1BeUjL-IHMHQM
  todo refer http://developer.android.com/training/basics/data-storage/databases.html#DefineContract
*/

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

public class Maps_Activity extends FragmentActivity implements OnMapReadyCallback
        , GoogleApiClient.ConnectionCallbacks
        , GoogleApiClient.OnConnectionFailedListener
        , LocationListener
        , GoogleMap.OnMarkerClickListener {
    //create an string for the data
    private GoogleApiClient mGoogleAPIClient;
    protected static final String TAG = Maps_Activity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest;
    public GoogleMap mMap;
    private String DistrictSelect = Main_Activity.DistrictSelect;
    private String SelectedArea = Main_Activity.SelectedArea;
    private LocationList Location;
    private Marker mCurrent;
    protected static Polyline pCurrent;
    private LatLng mCurrentLocation;
    private String Selection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SetUpAPIs();
    }

    private synchronized void SetUpAPIs() {
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mGoogleAPIClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)//10second in millie seconds(interval between updates)
                .setFastestInterval(1000);//1 second in milliseconds
        mCurrent = null;
        pCurrent=null;
    }

    @Override
    protected void onResume() {//is run after onCreate and whenever the activity is resumed
        super.onResume();
        mGoogleAPIClient.connect();
    }

    @Override
    protected void onPause() {// is run whenever the activity is paused
        super.onPause();
        if (mGoogleAPIClient.isConnected()) {
            LocationServices
                    .FusedLocationApi
                    .removeLocationUpdates(mGoogleAPIClient, this);
            mGoogleAPIClient.disconnect();
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {// is run when the map is ready
        // Add a marker in Paramaribo, Suriname, and move the camera.
        mMap = map;
        mMap.setOnMarkerClickListener(this);
        LatLng paramaribo = new LatLng(5.8520355, -55.2038278);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(paramaribo));
        //get the coordinates of certain addresses and add an marker at these addresses
        if (!SelectedArea.equals("Near My Location"))
            getCoordinatesAndPopulateMap();
        else
            Location=new LocationList(this);
    }

    private void getCoordinatesAndPopulateMap() {//
        String CategorySelect = Main_Activity.CategorySelect;
        Selection = null;
        Location = new LocationList(this);
        if (CategorySelect != null && DistrictSelect != null)
            Selection = CategorySelect + " AND " + DistrictSelect;
        else if (CategorySelect != null)
            Selection = CategorySelect;
        else if (DistrictSelect != null)
            Selection = DistrictSelect;
        Location.Populate(mMap, Selection);
        if (DistrictSelect != null) {
            Location.getCenterLocation(mMap, Selection);
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "Location services suspended. reconnecting....");
        mGoogleAPIClient.connect();
    }

    @Override
    public void onConnected(Bundle savedInstanceState) {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        Log.i(TAG, "Location services connected");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleAPIClient);
        if (mLastLocation != null) {//there might not be a last location saved yet
            if (SelectedArea.equals("Near My Location")) {
                mCurrentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mCurrentLocation, 15));
            }
            handleNewLocation(mLastLocation);
        }
        LocationServices
                .FusedLocationApi
                .requestLocationUpdates(mGoogleAPIClient, mLocationRequest, this);
    }

    private void handleNewLocation(Location mLastLocation) {
        if (SelectedArea.equals("Near My Location"))
            mMap.clear();
        Log.d(TAG, mLastLocation.toString());
        Log.i(TAG, "Handling new location");
        double currentLatitude = mLastLocation.getLatitude();
        double currentLongitude = mLastLocation.getLongitude();
        if (mCurrent != null)
            mCurrent.remove();
        mCurrentLocation = new LatLng(currentLatitude, currentLongitude);
        mCurrent = mMap.addMarker(new MarkerOptions().position(mCurrentLocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("You Are Here"));
        if (SelectedArea.equals("Near My Location")) {
            LocationList currentLocation = new LocationList(this);
            currentLocation.findClosestResources(mCurrentLocation, mMap);
        }
    }

    public void onConnectionFailed( @NonNull ConnectionResult Failed) {
        if (Failed.hasResolution()) {
            try {
                Failed.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + Failed.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location newLocation) {
        handleNewLocation(newLocation);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(pCurrent!=null) {
            pCurrent.remove();
            Location.DrawRoute(mCurrentLocation, marker.getPosition());
        }
        Toast.makeText(this,"Location not found (yet)",Toast.LENGTH_LONG).show();
        return false;
    }
}