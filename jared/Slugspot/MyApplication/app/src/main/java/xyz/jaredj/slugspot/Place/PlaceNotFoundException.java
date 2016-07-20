package xyz.jaredj.slugspot.Place;

/**
 * Created by jaredjensen on 7/19/16.
 */
public class PlaceNotFoundException extends Exception {
    public String name;

    public PlaceNotFoundException(String name) {
        this.name = name;
    }

    @Override
    public String getMessage() {
        return "Could not find " + name;
    }
}
