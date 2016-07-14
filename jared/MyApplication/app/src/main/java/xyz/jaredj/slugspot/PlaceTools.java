package xyz.jaredj.slugspot;

import android.app.Activity;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

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

    public static class Place {
        String name;
        LatLng coordinates;
        String description;
        Place(String name, LatLng coordinates, String description) {
            this.name = name;
            this.coordinates = coordinates;
            this.description = description;
        }
    }
}
