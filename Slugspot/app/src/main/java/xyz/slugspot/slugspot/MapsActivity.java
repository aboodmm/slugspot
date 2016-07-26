package xyz.slugspot.slugspot;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.SearchView;

import xyz.slugspot.slugspot.CategoriesPage;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import xyz.slugspot.slugspot.Place.Place;
import xyz.slugspot.slugspot.Place.PlaceList;
import xyz.slugspot.slugspot.Place.PlaceTools;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    PlaceList places;
    DisplayMetrics display;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Get display for padding
        display = getApplicationContext().getResources().getDisplayMetrics();

        final Button button1 = (Button) findViewById(R.id.button1);
        final Button about_button = (Button) findViewById(R.id.button2);
        final Button random_button = (Button) findViewById(R.id.button3);
        final Button category_button = (Button) findViewById(R.id.button4);
        final Button all_button = (Button) findViewById(R.id.button5);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (about_button.getVisibility() == View.GONE) {
                    about_button.setVisibility(View.VISIBLE);
                    random_button.setVisibility(View.VISIBLE);
                    category_button.setVisibility(View.VISIBLE);
                    all_button.setVisibility(View.VISIBLE);
                } else {
                    about_button.setVisibility(View.GONE);
                    random_button.setVisibility(View.GONE);
                    category_button.setVisibility(View.GONE);
                    all_button.setVisibility(View.GONE);
                }

            }
        });

        SearchView searchBar = (SearchView) findViewById(R.id.searchView);
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                PlaceList searchedPlaces = places.search(s);
                System.out.println(searchedPlaces);
                PlaceTools.displayOnMap(searchedPlaces, mMap);
                System.out.println("Displayed on map");
                PlaceTools.fitMapToPoints(searchedPlaces, mMap, display);
                System.out.println("Fit to points");
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return true;
            }
        });

        //ALL BUTTON
        all_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaceTools.displayOnMap(places, mMap);
                PlaceTools.fitMapToPoints(places, mMap, display);
            }
        });

        //ABOUT BUTTON
        about_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, about.class);
                startActivity(intent);

            }
        });

        //CATEGORIES BUTTON
        category_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, CategoriesPage.class);
                String[] categories = places.getCategories().toArray(new String[places.getCategories().size()]);
                intent.putExtra("Categories", categories);
                startActivityForResult(intent, 0);
            }
        });

        //RANDOM BUTTON
        random_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random mrand = new Random();
                int index = mrand.nextInt(places.size());
                Place found = places.get(index);
                LatLng fpos = found.coordinates;
                PlaceTools.displayOnMap(found, mMap);
                mMap.animateCamera(CameraUpdateFactory.newLatLng(fpos));
            }
        });

        //Open and read places file
        try {
            places = new PlaceList(getAssets().open("places.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
        mMap = googleMap;
        PlaceTools.displayOnMap(places, mMap);

        //Fit map display to points
        PlaceTools.fitMapToPoints(places, mMap, display);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0) {
            String category = data.getStringExtra("Category Chosen");
            PlaceList categoryList = places.getCategory(category);
            PlaceTools.displayOnMap(categoryList, mMap);
            PlaceTools.fitMapToPoints(categoryList, mMap, display);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://xyz.slugspot.slugspot/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://xyz.slugspot.slugspot/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
