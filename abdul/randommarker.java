import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.Random;
    
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        ArrayList markers = new ArrayList<Marker>();
        LatLng sydney = new LatLng(-34, 151);
        LatLng sanfran = new LatLng(38, -122);
        LatLng texas = new LatLng(32, -100);
        Marker sydneymarker = mMap.addMarker(new MarkerOptions().position(sydney).title("Sydney"));
        markers.add(sydneymarker);
        Marker sfmarker = mMap.addMarker(new MarkerOptions().position(sanfran).title("San Fran"));
        markers.add(sfmarker);
        Marker texasmarker = mMap.addMarker(new MarkerOptions().position(texas).title("Texas"));
        markers.add(texasmarker);
        Random mrand = new Random();
        int index = mrand.nextInt(markers.size());
        Marker found = (Marker) markers.get(index);
        LatLng fpos = found.getPosition();
        found.setTitle("This is the randomly chosen marker.");
        mMap.moveCamera(CameraUpdateFactory.newLatLng(fpos));
    }
