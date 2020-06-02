package com.example.proyekakhir_khoirulanam.SampahPintar;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyekakhir_khoirulanam.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MonitoringSampahPintar extends FragmentActivity implements OnMapReadyCallback {
    SupportMapFragment mapFragment;
    GoogleMap gMap;
    MarkerOptions markerOptions = new MarkerOptions();
    CameraPosition cameraPosition;
    LatLng center, latLng;
    String title,ket;

    public static final String ID = "id";
    public static final String TITLE = "nama";
    public static final String K = "keterangan";
    public static final String LAT = "lat";
    public static final String LNG = "lng";

    //    private String url = "http://192.168.43.229/uploadedFiles/markers.php";
    private String url = "http://192.168.43.229/relasi/public/api/monitoring";

    String tag_json_obj = "json_obj_req";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring_sampah_pintar);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng banyuwangi = new LatLng(-8.5982547, 114.0294519);
        gMap.addMarker(new MarkerOptions().position(banyuwangi).title("Pulau Merah "));
        CameraPosition home = CameraPosition.builder().target(new LatLng(-8.603255, 114.029219)).zoom(14).bearing(0).tilt(45).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(home));
        getMarkers();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        gMap.setMyLocationEnabled(true);
    }
    private void addMarker(LatLng latlng, final String title, final String keterangan) {
        markerOptions.position(latlng);
        markerOptions.title(title);
        markerOptions.title(keterangan);
        gMap.addMarker(markerOptions);

        gMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(getApplicationContext(), marker.getTitle(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    // Fungsi get JSON marker
    private void getMarkers() {
        StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("Response: ", response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    String getObject = jObj.getString("monitoring");
                    JSONArray jsonArray = new JSONArray(getObject);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        title = jsonObject.getString(TITLE);
                        ket = jsonObject.getString(K);
                        latLng = new LatLng(Double.parseDouble(jsonObject.getString(LAT)), Double.parseDouble(jsonObject.getString(LNG)));

                        // Menambah data marker untuk di tampilkan ke google map
                        addMarker(latLng, title,ket);
                    }

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", error.getMessage());
                Toast.makeText(MonitoringSampahPintar.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}