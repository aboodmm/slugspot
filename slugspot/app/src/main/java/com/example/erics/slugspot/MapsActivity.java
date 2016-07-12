package com.example.erics.slugspot;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    //Testing 123
    //TestingAgain



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


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

        about_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, about.class);
                startActivity(intent);

            }
        });

        category_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, categories.class);
                startActivity(intent);

            }
        });
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng UCSC = new LatLng(36.997793, -122.057309);
        mMap.addMarker(new MarkerOptions().position(UCSC).title("UCSC").snippet("This is a cool university!"));
        float zoomlevel = 16;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UCSC, zoomlevel));
    }


}




