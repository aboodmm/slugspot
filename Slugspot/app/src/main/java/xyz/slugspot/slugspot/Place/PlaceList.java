package xyz.slugspot.slugspot.Place;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * A list of places with some useful features
 */
public class PlaceList extends ArrayList<Place> {

    /**
     * Makes an empty list of places
     */
    public PlaceList() {
        super();
    }

    /**
     * Makes a list of places from a text file
     *
     * Text file should be formatted as such:
     *
     * Name: Foo
     * Latitude: 23.9
     * Longitude: -94.3
     * Description: Bar Qux Corge Grault
     * Categories: Grault, Garply, Waldo, Fred
     *
     * Name: Bar
     * ...
     *
     *
     * @param dataFile an inputStream of the text to be read
     */
    public PlaceList(InputStream dataFile) {
        super();
        String str;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(dataFile));
            String currentName = "";
            double latitude = 0;
            double longitude = 0;
            String description = "";
            ArrayList<String> categories = new ArrayList<String>();
            while ((str = reader.readLine()) != null) {
                String[] words = str.split(" ", 2);
                //System.out.println("");
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
                        List<String> x = Arrays.asList(words[1].split(", "));
                        categories.addAll(Arrays.asList(words[1].split(", ")));
                        break;
                    case "":
                        this.add(new Place(currentName, new LatLng(latitude, longitude), description, categories));
                        currentName = "";
                        latitude = 0;
                        longitude = 0;
                        description = "";
                        categories = new ArrayList<String>();
                }
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

    /**
     *
     * @param category the category you would like to search for
     * @return A list of places matching that category
     */
    public PlaceList getCategory(String category) {
        PlaceList list = new PlaceList();
        for (Place place : this) {
            if (place.categories.contains(category)) {
                list.add(place);
            }
        }
        return list;
    }

    /**
     * Searches through the place list, finds name, description, or category associated with the term
     * @param term The term to search for
     * @return A list of places matching the term
     */
    public PlaceList search(String term) {
        if (term == "")
            return this;
        PlaceList results = new PlaceList();
        term = term.toLowerCase();
        for (Place place : this) {
            if (place.name.toLowerCase().contains(term) || place.description.toLowerCase().contains(term))
                results.add(place);
            else
                for (String category : place.categories)
                    if (category.toLowerCase().contains(term))
                        results.add(place);
        }

        return results;
    }
}