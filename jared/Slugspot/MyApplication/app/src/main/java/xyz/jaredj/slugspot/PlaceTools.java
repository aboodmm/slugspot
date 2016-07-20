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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by jaredjensen on 7/14/16.
 */
public class PlaceTools {

    public class Place {
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



    /**
     * A list of places with some useful features
     */
    public class PlaceList extends ArrayList<Place> {

        public PlaceList() {
            super();
        }

        public PlaceList(InputStream in) {
            super();
            String str;
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String currentName = "";
                double latitude = 0;
                double longitude = 0;
                String description = "";
                ArrayList<String> categories = new ArrayList<String>();
                while ((str = reader.readLine()) != null) {
                    String[] words = str.split(" ", 2);
                    switch (words[0]) {
                        case "Name:":
                            currentName = words[1];
                            break;
                        case "Latitude:":
                            latitude = Double.parseDouble(words[1]);
                            break;
                        case "Longitude:":
                            longitude = Double.parseDouble(words[1]);
                            break;
                        case "Description:":
                            description = words[1];
                            break;
                        case "Categories:":
                            categories.addAll(Arrays.asList(words[1].split(", ")));
                            break;
                        case "":
                            this.add(new Place(currentName, new LatLng(latitude, longitude), description));
                            currentName = "";
                            latitude = 0;
                            longitude = 0;
                            description = "";
                            categories = new ArrayList<String>();
                    }
                    //Get rid of empty line
                    LatLng ll = new LatLng(latitude, longitude);
                    Place x = new Place(currentName, ll, description);
                    this.add(new Place(currentName, ll, description));
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Gives you a place given the name of the place
         * @param name the name of the place
         * @return the place if it exists, null otherwise
         */
        public Place getPlace(String name) throws PlaceNotFoundException {
            for (Place place : this) {
                if (place.name == name) {
                    return place;
                }
            }
            throw new PlaceNotFoundException(name);
        }

        /**
         * Gives the categories contained in the places inside the list
         * @return list of categories
         */
        public ArrayList<String> getCategories() {
            Set<String> categories = new HashSet<String>();
            for (Place place : this) {
                categories.addAll(place.categories);
            }
            return new ArrayList<String>(categories);
        }

        public PlaceList search(String term) {
            PlaceList results = new PlaceList();
            for (Place place : this) {
                if (place.name.contains(term) || place.description.contains(term))
                    results.add(place);
                else
                    for (String category : place.categories)
                        if (category.contains(term))
                            results.add(place);
            }
            return results;
        }
    }

    public class PlaceNotFoundException extends Exception {
        public String name;

        public PlaceNotFoundException(String name) {
            this.name = name;
        }

        @Override
        public String getMessage() {
            return "Could not find " + name;
        }
    };
}
