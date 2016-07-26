package xyz.slugspot.slugspot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class description extends AppCompatActivity {
    double rating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.description);
        String name = getIntent().getStringExtra("Name");
        String description = getIntent().getStringExtra("Description");
        String[] pictures = getIntent().getStringArrayExtra("Pictures");
        TextView desc = (TextView) findViewById(R.id.description);
        desc.setText(description);
        TextView title = (TextView) findViewById(R.id.name);
        title.setText(name + ":");
        ImageView picture1 = (ImageView) findViewById(R.id.imageView1);
        ImageView picture2 = (ImageView) findViewById(R.id.imageView2);
        final RatingBar rate_bar = (RatingBar) findViewById(R.id.ratingBar);
        rate_bar.setIsIndicator(true);

        RequestQueue queue = Volley.newRequestQueue(this);
        // Request a string response from the provided URL.
        String URL = "http://104.198.96.100:8080/rating/";
        String reqNumber = Integer.toString(Math.abs(name.hashCode()) % 7 + 1);
        URL += reqNumber;
        System.out.println("Request number: " + reqNumber);
        System.out.println(URL);

        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        try {
                            rating = response.getDouble("rating");
                            rate_bar.setRating((float)rating);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Error.Response " + error.getLocalizedMessage());
                    }
                }
        );
        // Add the request to the RequestQueue.
        queue.add(getRequest);
    }
}