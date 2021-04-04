package com.psauprojects.riyadhmetroticket;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class AdminAddStationActivity extends AppCompatActivity implements OnMapReadyCallback {

    Intent intent;
    String mobile;
    Address address;
    GoogleMap mMap;
    GoogleMap.OnCameraIdleListener onCameraIdleListener;
    ImageView imageView_close,imageView_info,imageView_add_train;
    Button button_add_station;
    TextView textView_address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_station);

        intent = getIntent();
        if (intent.hasExtra("mobile")){
            mobile = intent.getExtras().getString("mobile");
        }

        textView_address = (TextView) findViewById(R.id.textView_address);

        imageView_close = (ImageView) findViewById(R.id.imageView_close);
        imageView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(AdminAddStationActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imageView_info = (ImageView) findViewById(R.id.imageView_info);
        imageView_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(AdminAddStationActivity.this,AdminAccountActivity.class);
                intent.putExtra("mobile",mobile);
                startActivity(intent);
            }
        });

        imageView_add_train =(ImageView) findViewById(R.id.imageView_add_train);
        imageView_add_train.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(AdminAddStationActivity.this,AdminAddTrainActivity.class);
                intent.putExtra("mobile",mobile);
                startActivity(intent);
                finish();
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        configureCameraIdle();

        button_add_station = (Button) findViewById(R.id.button_add_station);
        button_add_station.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityName = textView_address.getText().toString();
                double lat = address.getLatitude();
                double lng = address.getLongitude();
                if (cityName.isEmpty()){
                    Toast.makeText(AdminAddStationActivity.this, R.string.no_city,Toast.LENGTH_LONG).show();
                    return;
                }
                final ProgressDialog progressDialog;
                progressDialog = new ProgressDialog(AdminAddStationActivity.this,R.style.MyProgessDailog);
                progressDialog.setMessage("Add station to database");
                progressDialog.setCancelable(false);
                progressDialog.setTitle("Information");


                AndroidNetworking.upload("http://psauprojects.com/ksatrain/add_station.php")
                        .addMultipartParameter("city_name",cityName)
                        .addMultipartParameter("lat",String.valueOf(lat))
                        .addMultipartParameter("lng",String.valueOf(lng))
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
                                Toast.makeText(AdminAddStationActivity.this, R.string.station_add,Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onError(ANError anError) {
                                progressDialog.dismiss();
                                Toast.makeText(AdminAddStationActivity.this, R.string.station_not_add,Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnCameraIdleListener(onCameraIdleListener);

        LatLng place = new LatLng(24.774265,46.738586);
        mMap.addMarker(new MarkerOptions().position(place).title("Station on Riydah")
                .snippet("Move screen to another Satation"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(place));
        CameraPosition googlePlex = CameraPosition.builder()
                .target(new LatLng(place.latitude,place.longitude))
                .zoom(7)
                .bearing(360)
                .tilt(60)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex));

    }

    private  void configureCameraIdle(){

        onCameraIdleListener = new GoogleMap.OnCameraIdleListener(){
            @Override
            public void onCameraIdle() {
                LatLng latLng = mMap.getCameraPosition().target;
                Geocoder geocoder = new Geocoder(AdminAddStationActivity.this);
                try {
                    List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 2);
                    if (addressList != null && addressList.size() > 0) {
                        address = addressList.get(0);
                        textView_address.setText(address.getLocality());
                    }

                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        };


    }



}
