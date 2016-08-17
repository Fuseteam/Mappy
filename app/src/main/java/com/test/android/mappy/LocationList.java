package com.test.android.mappy;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;

import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


import java.util.ArrayList;

/**
 * Created by 64 bit win on 11/22/2015.
 */
public class LocationList implements RoutingListener {// extends OfflineData{
    private String Name;
    private LatLng LatitudeLongitude;
    private String Category;
    private String District;
    private String Address;
    private Double LocationLatitude;
    private Double LocationLongitude;
    private SQLiteDatabase Place = null;
    private Cursor Location;
    private final ArrayList<LocationList> PlaceList = new ArrayList<>();
    private final String[] M_COLUMNS = OfflineData.M_COLUMNS;
    private final String LOCATION_TABLE_NAME = OfflineData.LOCATION_TABLE_NAME;
    private LatLng mCurrLocation=null;
    private GoogleMap mMap;

    LocationList(Context context) {
        OfflineData Places = new OfflineData(context);
        Place = Places.getReadableDatabase();
    }

    private LocationList(Cursor Location) {
        //private void getInformationAbout(Cursor Location){
        this.Name = Location.getString(Location.getColumnIndex(M_COLUMNS[0]));
        LocationLatitude = Location.getDouble(Location.getColumnIndex(M_COLUMNS[1]));
        LocationLongitude = Location.getDouble(Location.getColumnIndex(M_COLUMNS[2]));
        this.Address = Location.getString(Location.getColumnIndex(M_COLUMNS[3]));
        this.Category = Location.getString(Location.getColumnIndex(M_COLUMNS[4]));
        this.District = Location.getString(Location.getColumnIndex(M_COLUMNS[5]));
        this.LatitudeLongitude = new LatLng(LocationLatitude, LocationLongitude);//create an LatLng object for the Location
    }

    protected void findClosestResources(LatLng mCurrentLocation, GoogleMap map) {
        mCurrLocation=mCurrentLocation;
        Double currentLatitude = mCurrentLocation.latitude;
        Double currentLongitude = mCurrentLocation.longitude;
        String selection = Main_Activity.CategorySelect;
        Double longitudeDifferenceCorrection = 1 / Math.cos(currentLatitude);
        Location = Place.query(LOCATION_TABLE_NAME, M_COLUMNS, selection, null, M_COLUMNS[4]
                , "MIN((" + M_COLUMNS[1] + " - " + currentLatitude + ") * (" + M_COLUMNS[1] + " - " + currentLatitude +
                ") + ((" + M_COLUMNS[2] + " - " + currentLongitude + ") * " + longitudeDifferenceCorrection +
                ") * ((" + M_COLUMNS[2] + " - " + currentLongitude + ") * " + longitudeDifferenceCorrection + "))", null);
        Location.moveToFirst();
        Populate(map, selection);
    }

    protected void DrawRoute(LatLng mCurrentLocation, LatLng resource) {
        Routing routing = new Routing.Builder()
                .travelMode(Routing.TravelMode.WALKING)
                .withListener(this)
                .waypoints(mCurrentLocation, resource)
                .build();
        routing.execute();
    }

    private void getInformationAbout(Cursor Location, GoogleMap map) {
        Name = Location.getString(Location.getColumnIndex(M_COLUMNS[0]));
        LocationLatitude = Location.getDouble(Location.getColumnIndex(M_COLUMNS[1]));
        LocationLongitude = Location.getDouble(Location.getColumnIndex(M_COLUMNS[2]));
        Address = Location.getString(Location.getColumnIndex(M_COLUMNS[3]));
        Category = Location.getString(Location.getColumnIndex(M_COLUMNS[4])).replace(" ", "_");
        LatitudeLongitude = new LatLng(LocationLatitude, LocationLongitude);//create an LatLng object for the Location
        mMap=map;
        PlaceMakerOn(mMap);
    }

    protected void Populate(GoogleMap map, String selection) {
        if (Location == null)
            Location = Place.query(LOCATION_TABLE_NAME, M_COLUMNS, selection, null, null, null, null);
        if (Location.moveToFirst()) {//select the first row in the cursor object
            while (!Location.isAfterLast()) {//verifies if it is the last row
                LocationList thisLocation = new LocationList(Location);
                getInformationAbout(Location,map);
                if(Main_Activity.SelectedArea.equals("Near My Location"))
                    DrawRoute(mCurrLocation,LatitudeLongitude);
                PlaceList.add(thisLocation);
                Location.moveToNext();//select the next row
            }
            Location.close();//close the connection with database
        }
    }

    private void PlaceMakerOn(GoogleMap map) {
        MarkerOptions Options = new MarkerOptions()
                .position(LatitudeLongitude)//place a marker at the location
                .title(Name)//name above th marker
                .snippet(Address)//information below title
                .draggable(true);//allows marker to be dragged
        if (Name.contains("Hakrinbank"))
            map.addMarker(Options.icon(BitmapDescriptorFactory.fromAsset("mappin_Hakrinbank_" + Category + ".png")));
        else if (Name.contains("DSBank"))
            map.addMarker(Options.icon(BitmapDescriptorFactory.fromAsset("mappin_DSBank_" + Category + ".png")));
        else
            map.addMarker(Options.icon(BitmapDescriptorFactory.fromAsset("mappin_" + Category + ".png")));
    }

    protected void getCenterLocation(GoogleMap map, String selection) {
        Double CenterLocationLatitude;
        Double CenterLocationLongitude;
        Location = Place.rawQuery("SELECT AVG(" + M_COLUMNS[1] + ") " +
                "FROM " + LOCATION_TABLE_NAME + " WHERE " + selection, null);
        Location.moveToFirst();
        CenterLocationLatitude = Location.getDouble(0);
        Location = Place.rawQuery("SELECT AVG(" + M_COLUMNS[2] + ") " +
                "FROM " + LOCATION_TABLE_NAME + " WHERE " + selection, null);
        Location.moveToFirst();
        CenterLocationLongitude = Location.getDouble(0);
        Location.close();
        LatLng DistrictCenter = new LatLng(CenterLocationLatitude, CenterLocationLongitude);
        map.moveCamera(CameraUpdateFactory.newLatLng(DistrictCenter));
    }

    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> arrayList, int i) {
        //add route to the map.
        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.color(Color.GREEN);
        polyOptions.width(10);
        polyOptions.addAll(arrayList.get(i).getPoints());
        if(mMap!=null)
            Maps_Activity.pCurrent=mMap.addPolyline(polyOptions);
    }
    @Override
    public void onRoutingCancelled() {

    }
}
