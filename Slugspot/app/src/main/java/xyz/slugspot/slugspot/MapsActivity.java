package xyz.slugspot.slugspot;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.SearchView;

import xyz.slugspot.slugspot.CategoriesPage;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Random;

import xyz.slugspot.slugspot.Place.Place;
import xyz.slugspot.slugspot.Place.PlaceList;
import xyz.slugspot.slugspot.Place.PlaceTools;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    PlaceList places;
    DisplayMetrics display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Get display for padding
        display = getApplicationContext().getResources().getDisplayMetrics();

        Button button1= (Button) findViewById(R.id.button1);
        Button about_button= (Button) findViewById(R.id.button2);
        Button random_button= (Button) findViewById(R.id.button3);
        Button category_button= (Button) findViewById(R.id.button4);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Button about_button= (Button) findViewById(R.id.button2);
                Button random_button= (Button) findViewById(R.id.button3);
                Button category_button= (Button) findViewById(R.id.button4);
                if(about_button.getVisibility()==View.GONE){
                    about_button.setVisibility(View.VISIBLE);
                    random_button.setVisibility(View.VISIBLE);
                    category_button.setVisibility(View.VISIBLE);
                }
                else{
                    about_button.setVisibility(View.GONE);
                    random_button.setVisibility(View.GONE);
                    category_button.setVisibility(View.GONE);
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
        mMap = googleMap;
        PlaceTools.displayOnMap(places, mMap);

        //Fit map display to points
        PlaceTools.fitMapToPoints(places, mMap, display);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 0) {
            String category = data.getStringExtra("Category Chosen");
            PlaceList categoryList = places.getCategory(category);
            PlaceTools.displayOnMap(categoryList, mMap);
            PlaceTools.fitMapToPoints(categoryList, mMap, display);
        }
    }
}
