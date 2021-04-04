package com.psauprojects.riyadhmetroticket;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class PassViewStationsActivity extends AppCompatActivity implements OnMapReadyCallback {
    Intent intent;
    String mobile;
    ImageView imageView_close,imageView_info,imageView_ticket;
    GoogleMap mMap;
    GoogleMap.OnCameraIdleListener onCameraIdleListener;
    JSONArray jsonArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_view_stations);

        intent = getIntent();
        mobile = intent.getExtras().getString("mobile");

        imageView_close = (ImageView) findViewById(R.id.imageView_close);
        imageView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(PassViewStationsActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imageView_info = (ImageView) findViewById(R.id.imageView_info);
        imageView_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(PassViewStationsActivity.this,PassAccountActivity.class);
                intent.putExtra("mobile",mobile);
                startActivity(intent);
                finish();
            }
        });
        imageView_ticket = (ImageView) findViewById(R.id.imageView_ticket);
        imageView_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(PassViewStationsActivity.this,PassTicketsActivity.class);
                intent.putExtra("mobile",mobile);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(PassViewStationsActivity.this,R.style.MyProgessDailog);
        progressDialog.setMessage("Get all stations");
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Information");

        AndroidNetworking.upload("http://psauprojects.com/ksatrain/get_stations.php")
                .addMultipartParameter("temp","temp")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        progressDialog.show();
                    }
                })
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        if (response == null) {
                            return;
                        }
                        try {
                            jsonArray = new JSONArray(response);
                            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                    .findFragmentById(R.id.map);
                            mapFragment.getMapAsync(PassViewStationsActivity.this);
                            configureCameraIdle();
                        }catch (JSONException ex){
                            if(ex.getMessage().contains("null")){
                                Toast.makeText(PassViewStationsActivity.this, "No stations loaded.",Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(PassViewStationsActivity.this,"Cannot make connection",Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnCameraIdleListener(onCameraIdleListener);

        try {
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                LatLng place = new LatLng(jsonObject.getDouble("lat"),jsonObject.getDouble("lng"));
                mMap.addMarker(new MarkerOptions().position(place).title(jsonObject.getString("station_name")+" Station")
                        .snippet("use direction bottom below to reach to this station"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(place));
                CameraPosition googlePlex = CameraPosition.builder()
                        .target(new LatLng(place.latitude,place.longitude))
                        .zoom(5)
                        .bearing(360)
                        .tilt(60)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex));
            }
        }catch (JSONException  ex){

        }
    }

    private  void configureCameraIdle(){

        onCameraIdleListener = new GoogleMap.OnCameraIdleListener(){
            @Override
            public void onCameraIdle() {
                //LatLng latLng = mMap.getCameraPosition().target;
                //Geocoder geocoder = new Geocoder(PassViewStationsActivity.this);
                /*try {
                    List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 2);
                    if (addressList != null && addressList.size() > 0) {
                        //address = addressList.get(0);
                        //textView_address.setText(address.getLocality());
                    }

                }catch(IOException e){
                    e.printStackTrace();
                }*/
            }
        };
    }
}
