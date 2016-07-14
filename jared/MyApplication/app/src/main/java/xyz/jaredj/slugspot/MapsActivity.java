package xyz.jaredj.slugspot;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<Place> places;
    BufferedReader reader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        places = (new PlaceReader()).BuildPlaces("places.txt");


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
        LatLng sydney = new LatLng(-34, 151);
        for(int i = 0; i < places.size(); i++) {
            mMap.addMarker(new MarkerOptions().position(places.get(i).coordinates).title(places.get(i).name));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(places.get(1).coordinates));

    }


    public class Place {
        String name;
        LatLng coordinates;
        String description;
        Place(String name, LatLng coordinates, String description) {
            this.name = name;
            this.coordinates = coordinates;
            this.description = description;
        }
    }

    public class PlaceReader {
        public ArrayList<Place> BuildPlaces(String fileName) {
            ArrayList<Place> places = new ArrayList<Place>();
            String str;
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open(fileName)));
                String currentName;
                double latitude;
                double longitude;
                String description;
                while((str = reader.readLine()) != null && str.length() != 0) {
                    currentName = str.replaceFirst("Name: ", "");
                    latitude = Double.parseDouble(reader.readLine().replaceFirst("Latitude: ", ""));
                    longitude  = Double.parseDouble(reader.readLine().replaceFirst("Longitude: ", ""));
                    description = reader.readLine().replaceFirst("Description: ", "");
                    //Get rid of empty line
                    reader.readLine();
                    places.add(new Place(currentName, new LatLng(latitude, longitude), description));
                }
            } catch(java.io.IOException e) {
                e.printStackTrace();
            }
            return places;
        }
    }

}
