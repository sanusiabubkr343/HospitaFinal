package com.example.hospitalwaka.Activities;

import androidx.annotation.NonNull;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hospitalwaka.Models.Model;
import com.example.hospitalwaka.Models.myModel;
import com.example.hospitalwaka.Network.APICLient;
import com.example.hospitalwaka.Network.APIInterface;
import com.example.hospitalwaka.Network.AsynchronousResponse;
import com.example.hospitalwaka.Network.NetworkChecker;
import com.example.hospitalwaka.R;
import com.example.hospitalwaka.Sliders.sliderActivity;
import com.example.hospitalwaka.Util.Constants;
import com.example.hospitalwaka.Util.WakaApi;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    ProgressBar progressBar3;

    private ImageButton searchButton;
    BottomNavigationView bottomNavigationView;
    private CardView cardView;
    AlertDialog dialog;
    AlertDialog.Builder builder;
    private Boolean NetworkFlag;
    private BitmapDescriptor[] iconColors;
    List<myModel> arrayList;
    private final Context context = MapsActivity.this;
    private Boolean dataFlag = false;
    private List<Double> ulat;
    private List<Double> ulons;
    private int responseSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        cardView = findViewById(R.id.cardview);
        searchButton = findViewById(R.id.floating_search_button);
        arrayList = new ArrayList<>();
        Log.d("Enyy", "onCreate: "+"All is well");
        loadNearbyPlaces();


        iconColors = new BitmapDescriptor[]{


                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)};


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadProgress(view);

            }



        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.page_1:
                        if (cardView.getVisibility() == View.INVISIBLE ||
                                cardView.getVisibility() == View.GONE) {
                            cardView.setVisibility(View.VISIBLE);
                            if (dataFlag = true) {
                                loadNearbyPlaces();
                            }
                        }

                        //show the map view
                        mMap.clear();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.map, mapFragment)
                                .commit();


                        break;
                    case R.id.page_2:

                        if (!dataFlag) {
                            Toast.makeText(context, "No Search List Available , Type and Click the search Button", Toast.LENGTH_LONG).show();
                        } else {

                            selectedFragment = new FragmentList(context);
                            cardView.setVisibility(View.GONE);
                            mMap.clear();
                            WakaApi.getInstance().setMyList(arrayList);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.map, selectedFragment)
                                    .commit();
                           // Log.d("okay", "onNavigationItemSelected: " + arrayList.toString());
                            //Todo Recycler Adapter with Asynchronous CallBack Function
                        }

                        break;

                    case R.id.page_3:
                        signOut();
                        break;
                }

                return true;
            }

        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {

            @Override
            public void gotLocation(Location location) {
                float zoomLevel = 16.0f; //This goes up to 21
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), zoomLevel));
              Log.d("today", "gotLocation: "+location.getLatitude() + ","+ location.getLongitude());
            }
        };
        MyLocation myLocation = new MyLocation(context);
        myLocation.getLocation(context, locationResult); 
    }


    private void loadProgress(View view) {

        builder = new AlertDialog.Builder(this);
        View newView = getLayoutInflater().inflate(R.layout.loading_layout, null);
        progressBar3 = newView.findViewById(R.id.progressBar3);
        builder.setView(newView);
        dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);
        loadNearbyPlaces();


    }


    private void CreatePopUpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.network_err_layout, null);
        Button retryButton = view.findViewById(R.id.retryButton);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        if (NetworkFlag) {
            Toast.makeText(context, "CONNECTED", Toast.LENGTH_SHORT).show();
        } else {
            dialog.show();
            dialog.setCancelable(false);
            retryButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    startActivity(new Intent(context, sliderActivity.class));
                }
            });
        }

    }


    private void signOut() {
        AlertDialog.Builder alert = new AlertDialog.Builder(MapsActivity.this);
        alert.setTitle("Sign Out");
        alert.setMessage("Are you sure you want to Sign Out?");
        alert.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        alert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MapsActivity.this, Login_Page.class));
                dialog.dismiss();
            }
        });

        alert.show();
    }


    private void loadNearbyPlaces() {
        //Todo check for internet too at 7s
        // Log.d("myTAG", "loadNearbyPlaces: "+currentLocation.getLatitude()+ "," +currentLocation.getLongitude());
        new Thread(new Runnable() {
            @Override
            public void run() {
                new NetworkChecker().hasNetwork(new AsynchronousResponse() {
                    @Override
                    public void processFinished(boolean callBack) {
                     //   Log.d("check", "processFinished: " + callBack);
                        NetworkFlag = callBack;
                    }
                });
            }
        }).start();

        // If no network create PopUp dialog
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                {
                    if (NetworkFlag) {


                    } else {
                        CreatePopUpDialog();
                    }
                }
            }
        }, 8000);
        // Check if user already sign in and move user to mapActivity
        MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
            @Override
            public void gotLocation(Location location) {
                //Got the location!
                Log.d("myLocation", "gotLocation: " + location.getLongitude() + "," + location.getLatitude());
                String key = "AIzaSyCytHbneh1IUU_A1Nbg6zIjtBGJbfEywls";
                String currentLocation = location.getLatitude() + "," + location.getLongitude();
                int radius = 1500;

                APIInterface apiInterface = APICLient.getClient().create(APIInterface.class);
                Call<Model> call = apiInterface.getNearBy(currentLocation, radius, "Hospitals and Health", "medical", key);
                //Todo ensure to add keyword "emergency"  to it
                call.enqueue(new Callback<Model>() {
                    @Override
                    public void onResponse(Call<Model> call, Response<Model> response) {
                        progressBar3.setVisibility(View.INVISIBLE);
                        Model model = (Model) response.body();
                        assert response.body() != null;
                        responseSize = response.body().getResults().size();
                        dataFlag = true;
                      Log.d("today", "onResponse: " + response.body());

                        for (int i = 0; i < Objects.requireNonNull(model).getResults().size(); i++) {
                            myModel model1 = new myModel();
                            MarkerOptions markerOptions = new MarkerOptions();
                            double lon = model.getResults().get(i).getGeometry().getLocation().getLng();
                            double lat = model.getResults().get(i).getGeometry().getLocation().getLat();
                            markerOptions.icon(iconColors[Constants.randomInt(iconColors.length, 0)]);
                            markerOptions.title(model.getResults().get(i).getName());
                            markerOptions.position(new LatLng(model.getResults().get(i).getGeometry().getLocation().getLat(),
                                    model.getResults().get(i).getGeometry().getLocation().getLng()));
                            markerOptions.snippet(model.getResults().get(i).getVicinity());
                            model1.setHospitalName(model.getResults().get(i).getName());
                            model1.setVicinity(model.getResults().get(i).getVicinity());
                            model1.setGeographicalLocation(String.valueOf(lat + "Lat" + "," + lon + "Long"));
                            ulat = new ArrayList<>();
                            ulons = new ArrayList<>();
                            ulat.add(lat);
                            ulons.add(lon);
                            model1.setPhotoUrl(R.drawable.hospital_icon);
                            arrayList.add(model1);
                            Marker marker = mMap.addMarker(markerOptions);
                            float zoomLevel = 16.0f; //This goes up to 21
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), zoomLevel));

                        }
                        WakaApi.getInstance().setLat(ulat);
                        WakaApi.getInstance().setLons(ulons);
                        dialog.setCancelable(true);
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<Model> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Location not Found", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });
            }
        };
        MyLocation myLocation = new MyLocation(context);
        myLocation.getLocation(context, locationResult);


    }


}
