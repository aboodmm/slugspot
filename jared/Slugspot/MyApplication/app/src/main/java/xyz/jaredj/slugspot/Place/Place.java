package xyz.jaredj.slugspot.Place;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Keeps track of things related to a place
 */
public class Place {
    public String name;
    public LatLng coordinates;
    public String description;
    public ArrayList<String> categories;

    public Place(String name, LatLng coordinates, String description) {
        this(name, coordinates, description, new ArrayList<String>());
    }

    public Place(String name, LatLng coordinates, String description, ArrayList<String> categories) {
        this.name = name;
        this.coordinates = coordinates;
        this.description = description;
        this.categories = categories;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Name: " + name + " ");
        str.append("Lat: " + coordinates.latitude + " ");
        str.append("Lon: " + coordinates.longitude + " ");
        return str.toString();
    }
}