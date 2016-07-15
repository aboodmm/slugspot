package xyz.jaredj.slugspot;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by jaredjensen on 7/14/16.
 */
public class PlaceTools {
    public static ArrayList<Place> buildPlaces(InputStream in) {
        ArrayList<Place> places = new ArrayList<Place>();
        String str;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String currentName;
            double latitude;
            double longitude;
            String description;
            while ((str = reader.readLine()) != null && str.length() != 0) {
                currentName = str.replaceFirst("Name: ", "");
                latitude = Double.parseDouble(reader.readLine().replaceFirst("Latitude: ", ""));
                longitude = Double.parseDouble(reader.readLine().replaceFirst("Longitude: ", ""));
                description = reader.readLine().replaceFirst("Description: ", "");
                //Get rid of empty line
                reader.readLine();
                LatLng ll = new LatLng(latitude, longitude);
                places.add(new Place(currentName, ll, description));
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        return places;

    }

    public static ArrayList<Place> getPlacesWithCategory(ArrayList<Place> places, String category) {
        ArrayList<Place> placesWithCategory = new ArrayList<Place>();
        for (Place place : places) {
            if (place.categories.contains(category)) {
                placesWithCategory.add(place);
            }
        }
        return placesWithCategory;
    }

    public static ArrayList<String> getCategories(ArrayList<Place> places) {
        Set<String> categories = new HashSet<String>();
        for (Place place : places) {
            categories.addAll(place.categories);
        }
        return new ArrayList<String>(categories);
    }

    public static void fitMapToPoints(ArrayList<Place> places, GoogleMap map) {
        fitMapToPoints(places, map, 0);
    }

    public static void fitMapToPoints(ArrayList<Place> places, GoogleMap map, int padding) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Place place : places) {
            builder.include(place.coordinates);
        }
        LatLngBounds bounds = builder.build();

        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
    }

    /**
     * This is necessary when initiating the map the first time.
     * @param places list of places
     * @param map the map to update
     * @param width width of the phone screen
     * @param height height of the phne screen
     * @param padding padding (in pixels)
     */
    public static void fitMapToPoints(ArrayList<Place> places, GoogleMap map, int width, int height, int padding) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Place place : places) {
            builder.include(place.coordinates);
        }
        LatLngBounds bounds = builder.build();
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));
    }

    public static void displayOnMap(ArrayList<Place> places, GoogleMap map) {
        map.clear();
        for (Place place : places) {
            map.addMarker(new MarkerOptions().position(place.coordinates).title(place.name).snippet(place.description));
        }
    }

    public static class Place {
        String name;
        LatLng coordinates;
        String description;
        ArrayList<String> categories;
        Place(String name, LatLng coordinates, String description) {
            this(name, coordinates, description, new ArrayList<String>());
        }

        Place(String name, LatLng coordinates, String description, ArrayList<String> categories) {
            this.name = name;
            this.coordinates = coordinates;
            this.description = description;
            this.categories = categories;
        }
    }
}
