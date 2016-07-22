package xyz.slugspot.slugspot;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;

import xyz.slugspot.slugspot.Place.PlaceList;
import xyz.slugspot.slugspot.Place.PlaceTools;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    PlaceList places;
    ArrayList<String> categories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Open and read places file
        try {
            places = new PlaceList(getAssets().open("places.txt"));
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        System.out.println(places);
        mMap = googleMap;
        PlaceTools.displayOnMap(places, mMap);

        //Get display size for padding
        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();

        //Fit map display to points
        PlaceTools.fitMapToPoints(places, mMap, metrics);
    }
}
