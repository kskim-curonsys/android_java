package com.curonsys.android_java;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.curonsys.android_java.adapter.MyAdapter;
import com.curonsys.android_java.arcore.AugmentedImageActivity;
import com.curonsys.android_java.http.RequestManager;
import com.curonsys.android_java.model.ContentModel;
import com.curonsys.android_java.model.ContentsListModel;
import com.curonsys.android_java.model.DownloadModel;
import com.curonsys.android_java.model.MarkerModel;
import com.curonsys.android_java.model.TransferModel;
import com.curonsys.android_java.model.UserContentsModel;
import com.curonsys.android_java.model.UserModel;
import com.curonsys.android_java.service.FetchAddressIntentService;
import com.curonsys.android_java.service.GeofenceTransitionsIntentService;
import com.curonsys.android_java.utils.Constants;
import com.curonsys.android_java.utils.DBManager;
import com.curonsys.android_java.utils.LocationUtil;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.kudan.kudan.ARAPIKey;

import com.curonsys.android_java.activity.*;
import com.curonsys.android_java.utils.PermissionManager;

import static com.curonsys.android_java.utils.Constants.KUDAN_API_KEY_DEV;
import static com.curonsys.android_java.utils.Constants.STORAGE_BASE_URL;
import static com.curonsys.android_java.utils.LocationUtil.distanceFrom;
import static com.curonsys.android_java.utils.LocationUtil.latitudeInDifference;
import static com.curonsys.android_java.utils.LocationUtil.longitudeInDifference;

public class ChooseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int MY_PERMISSION_STORAGE = 1;
    private static final int REQUEST_TAKE_PHOTO = 2;
    private static final int REQUEST_TAKE_ALBUM = 3;
    private static final int REQUEST_IMAGE_CROP = 4;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 5;
    private static final int REQUEST_PLACE_PICKER = 6;

    static final String LOCATION_UPDATE_STATE = "location_update_state";
    static final String LOCATION_HISTORY = "location_histry";

    private static final String TAG = ChooseActivity.class.getSimpleName();
    private static final String TAG_GEO = "Geofence";

    private String mOutput = "";
    private String mAddressOutput = "";
    private boolean mLocationUpdateState = false;

    private FirebaseAuth mAuth;
    private FirebaseStorage mStorage;
    private StorageReference mStorageRef;
    private FirebaseAnalytics mAnalytics;

    private GoogleMap mGoogleMap;
    private LocationManager mLocationManager;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private RequestManager mRequestManager;
    private DBManager mDBManager;
    private GeofencingClient mGeofencingClient;
    private List<Geofence> mGeofenceList = new ArrayList<Geofence>();
    private PendingIntent mGeofencePendingIntent;

    private ImageView mProfileImage;
    private TextView mProfileName;
    private TextView mProfileEmail;

    private Button mSearchBtn;
    private Button mSettingsBtn;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<String> myDataset;

    /*
    private ImageView mTestImage;
    private TextView mTestResult;
    private TextView mAddressResult;
    private Button mTestLocation;
    private Button mStopLocation;
    */

    protected Location mLastLocation = null;
    private AddressResultReceiver mResultReceiver = new AddressResultReceiver(new Handler());
    private MarkerAddressReceiver mAddressReceiver = new MarkerAddressReceiver(new Handler());

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultData == null) {
                return;
            }
            //mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            String info = resultData.getString(Constants.RESULT_DATA_KEY);
            if (info != null) {
                info += "\n";
                myDataset.add(info);
                mAdapter.notifyItemInserted(myDataset.size() - 1);
            }

            /*
            if (mAddressOutput == null) {
                mAddressOutput = "";
            }
            */
            if (resultCode == Constants.SUCCESS_RESULT) {
                updateUI();
            }
        }
    }

    class MarkerAddressReceiver extends ResultReceiver {
        public MarkerAddressReceiver(Handler handler) {
            super(handler);
        }
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultData == null) {
                return;
            }
            if (resultCode == Constants.SUCCESS_RESULT) {
                mDBManager.currentCountryCode = resultData.getString(Constants.RESULT_COUNTRY_KEY);
                mDBManager.currentLocality = resultData.getString(Constants.RESULT_LOCALITY_KEY);
                mDBManager.currentThoroughfare = resultData.getString(Constants.RESULT_THOROUGHFARE_KEY);

                // upload marker image to storage
                uploadMarkerImage();
            }
        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        ARAPIKey key = ARAPIKey.getInstance();
        key.setAPIKey(KUDAN_API_KEY_DEV);

        PermissionManager permissionManager = new PermissionManager(this);
        permissionManager.permissionCheck();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //AppBarLayout appbarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
        //appbarLayout.setOutlineProvider(null);

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance(STORAGE_BASE_URL);
        mAnalytics = FirebaseAnalytics.getInstance(this);

        mRequestManager = RequestManager.getInstance();
        mDBManager = DBManager.getInstance();

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mGeofencingClient = LocationServices.getGeofencingClient(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                updateUI();
            }
            public void onDrawerOpened(View view) {
                super.onDrawerOpened(view);
                updateUI();
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        mProfileImage = (ImageView) header.findViewById(R.id.imageView);
        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkLogin()) {
                    goAccount();
                    closeDrawer();
                } else {
                    goLoginStep();
                    closeDrawer();
                }
            }
        });
        mProfileName = (TextView) header.findViewById(R.id.profile_name);
        mProfileEmail = (TextView) header.findViewById(R.id.profile_email);

        myDataset = new ArrayList<String>();
        String str = "Location Tracking..";
        myDataset.add(str);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_main_recycler_view);

        DividerItemDecoration itemDecorator = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.mydivider));
        mRecyclerView.addItemDecoration(itemDecorator);

        mRecyclerView.setHasFixedSize(false);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(myDataset, new MyAdapter.ButtonClickCallback() {
            @Override
            public void onClicked(int position) {
                Log.d(TAG, "onClick: item " + position);

                if (position == 0) {
                    Log.d(TAG, "onClick: Test Location");
                    getLastLocation();
                    if (mLastLocation == null) {
                        return;
                    }
                    if (!Geocoder.isPresent()) {
                        Toast.makeText(ChooseActivity.this,
                                R.string.no_geocoder_available,
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    startFetchAddressIntentService();
                    updateUI();
                    startMonitorGeofences();
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);


        /*
        mTestImage = (ImageView) findViewById(R.id.test_imageview);
        mTestResult = (TextView) findViewById(R.id.test_textview);
        mTestResult.setMovementMethod(new ScrollingMovementMethod());
        mAddressResult = (TextView) findViewById(R.id.address_textview);
        mTestLocation = (Button) findViewById(R.id.choose_location_btn);
        mTestLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Test Location");
                getLastLocation();
                if (mLastLocation == null) {
                    return;
                }
                if (!Geocoder.isPresent()) {
                    Toast.makeText(ChooseActivity.this,
                            R.string.no_geocoder_available,
                            Toast.LENGTH_LONG).show();
                    return;
                }
                startFetchAddressIntentService();
                updateUI();
                startMonitorGeofences();
            }
        });

        mStopLocation = (Button) findViewById(R.id.choose_location_stop);
        mStopLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Stop Location");
                stopLocationUpdate();
                stopMonitorGeofences();
            }
        });
        */

        mLocationUpdateState = true;    // temp: default set
        getLastLocation();
        updateUI();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mLocationUpdateState) {
            startLocationUpdate();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // for abnormal termination
        outState.putBoolean(LOCATION_UPDATE_STATE, mLocationUpdateState);   // test
        outState.putString(LOCATION_HISTORY, mOutput);      // test

        // test download save
        /*
        // If there's a download in progress, save the reference so you can query it later
        if (mStorageRef != null) {
            outState.putString("reference", mStorageRef.toString());
        }
        */
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // for abnormal termination
        if (savedInstanceState.keySet().contains("LOCATION_UPDATE_STATE")) {
            mLocationUpdateState = savedInstanceState.getBoolean("LOCATION_UPDATE_STATE");
        }
        if (savedInstanceState.keySet().contains("LOCATION_HISTORY")) {
            mOutput = savedInstanceState.getString("LOCATION_HISTORY");
        }
        updateUI();

        // test download resume
        /*
        // If there was a download in progress, get its reference and create a new StorageReference
        final String stringRef = savedInstanceState.getString("reference");
        if (stringRef == null) {
            return;
        }
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl(stringRef);

        // Find all DownloadTasks under this StorageReference (in this example, there should be one)
        List<FileDownloadTask> tasks = mStorageRef.getActiveDownloadTasks();
        if (tasks.size() > 0) {
            // Get the task monitoring the download
            FileDownloadTask task = tasks.get(0);

            // Add new listeners to the task using an Activity scope
            task.addOnSuccessListener(this, new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot state) {
                    //handleSuccess(state); //call a user defined function to handle the event.
                }
            });
        }
        */
    }

    public void findPlace(View view) {
        try {
            Intent intent =  new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            //.setFilter(typeFilter)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            Log.i(TAG, "GooglePlayServicesRepairableException: " + e.getMessage());
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.i(TAG, "GooglePlayServicesNotAvailableException: " + e.getMessage());
        }
    }

    public void findPlaceByPicker() {
        // Construct an intent for the place picker
        try {
            PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
            Intent intent = intentBuilder.build(this);
            startActivityForResult(intent, REQUEST_PLACE_PICKER);

        } catch (GooglePlayServicesRepairableException e) {
            // ...
        } catch (GooglePlayServicesNotAvailableException e) {
            // ...
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO) {
        } else if (requestCode == REQUEST_TAKE_ALBUM) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getData() != null) {
                    mDBManager.imageURI = data.getData();
                    // marker registration test
                    startMarkerAddressIntentService();
                }
            }

        } else if (requestCode == REQUEST_IMAGE_CROP) {
        } else if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);

                String info = "Find Place: " + "\n" + place.getName() + "\n" + place.getAddress() +
                        "\n" + place.getLatLng() + "\n" + place.getViewport();
                mOutput += info + "\n\n";
                //mTestResult.setText(mOutput);
                info += "\n";
                myDataset.add(info);
                Log.i(TAG, info);
                mAdapter.notifyItemInserted(myDataset.size() - 1);

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                Log.i(TAG, "The user canceled the place operation.");
            }
        } else if (requestCode == REQUEST_PLACE_PICKER) {
            if (resultCode == Activity.RESULT_OK) {
                final Place place = PlacePicker.getPlace(data, this);
                final CharSequence name = place.getName();
                final CharSequence address = place.getAddress();
                String attributions = PlacePicker.getAttributions(data);
                if (attributions == null) {
                    attributions = "";
                }

                String info = "Find Place: " + "\n" + place.getName() + "\n" + place.getAddress() +
                        "\n" + place.getLatLng() + "\n" + place.getViewport();
                mOutput += info + "\n\n";
                //mTestResult.setText(mOutput);
                info += "\n";
                myDataset.add(info);
                Log.i(TAG, info);
                mAdapter.notifyItemInserted(myDataset.size() - 1);

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.choose, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.search) {
            Log.d(TAG, "Do Find: ");
            goAugmentedImageActivity();
            return true;
        } else if (id == R.id.action_settings) {
            Log.d(TAG, "Do Settings: ");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            goMarkerGeneration();

        } else if (id == R.id.nav_gallery) {
            goGallery();

        } else if (id == R.id.nav_arcamera) {
            goARCamera();

        } else if (id == R.id.nav_options) {
            goOption();

        } else if (id == R.id.nav_signup) {
            goSignupStep();

        } else if (id == R.id.nav_download) {
            //goTestDownload();
            //goTestImageGrid();
            goTestTabbed();

        } else if (id == R.id.nav_gettest) {
            goTestGetList();

        } else if (id == R.id.nav_posttest) {
            goLocation();

        } else if (id == R.id.nav_logout) {
            doSignOut();
        }
        closeDrawer();

        return true;
    }

    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            double longitude = location.getLongitude(); //경도
            double latitude = location.getLatitude();   //위도
            float speed = location.getSpeed();
            //double altitude = location.getAltitude();   //고도//          float accuracy = location.getAccuracy();    //정확도//            String provider = location.getProvider();   //위치제공자

            LatLng currentLocation = new LatLng(latitude,longitude);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(currentLocation);
            markerOptions.title("현재 위치");
            markerOptions.snippet("ARZone");
            //mGoogleMap.addMarker(markerOptions);
            //mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16));
            //mLocationManager.removeUpdates(mLocationListener);  //  미수신할때는 반드시 자원해체를 해주어야 한다.
            mDBManager.currentLatitude = latitude;
            mDBManager.currentLongtitude = longitude;

            double testlat = 34.951302;      // test value to calculate distance between two points
            double testlon = 127.689964;
            double testdist = distanceFrom(latitude, longitude, testlat, testlon);

            double difflat = latitudeInDifference(500);
            double difflon = longitudeInDifference(latitude, 500);

            String info = "lat : " + latitude + "\n" + "lon : " + longitude + "\n" + "speed : " + speed + "\n\n";
            //mTestResult.setText(mOutput);
            info += "\n";
            myDataset.add(info);
            mAdapter.notifyItemInserted(myDataset.size() - 1);

            Log.d(TAG, "Lat: " + latitude);
            Log.d(TAG, "Lon: " + longitude);
            Log.d(TAG, "Test Distance From: " + testdist + "m");
            // lat, lon : 상하, 좌우
            // ++(우상), +-(좌상), -+(우하), --(좌하)
            Log.d(TAG, "Test Latitude in Difference: " + (latitude - difflat));
            Log.d(TAG, "Test Longitude in Difference: " + (longitude - difflon));
        }

        public void onProviderDisabled(String provider) {
            Log.d(TAG, "onProviderDisabled: " + provider);
        }

        public void onProviderEnabled(String provider) {
            Log.d(TAG, "onProviderEnabled: " + provider);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            //Log.d(TAG, "onStatusChanged: " + provider + ", status: " + status + " ,Bundle: " + extras);
        }
    };

    protected void startFetchAddressIntentService() {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);
        startService(intent);
    }

    protected void startMarkerAddressIntentService() {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mAddressReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);
        startService(intent);
    }

    private void startLocationUpdate() {
        try {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    100, 1, mLocationListener);
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    100, 1, mLocationListener);
            mLocationUpdateState = true;

        } catch (SecurityException e) {
            mLocationUpdateState = false;
            e.printStackTrace();
        }
    }

    private void stopLocationUpdate() {
        if (mLocationUpdateState) {
            mLocationManager.removeUpdates(mLocationListener);
            mLocationUpdateState = false;
        }
    }

    private void getLastLocation() {
        try {
            mFusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                mLastLocation = location;

                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();
                                float speed = location.getSpeed();

                                mOutput += "last lat : " + latitude + "\n" + "last lon : " + longitude + "\n" + "speed : " + speed + "m/s" + "\n\n";
                                //mTestResult.setText(mOutput);
                            }
                        }
                    });

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        mGeofencePendingIntent = PendingIntent.getService(this, 0,
                intent, PendingIntent. FLAG_UPDATE_CURRENT);
        return mGeofencePendingIntent;
    }

    private void startMonitorGeofences() {
        for (Map.Entry<String, LatLng> entry : Constants.BAY_AREA_LANDMARKS.entrySet()) {
            mGeofenceList.add(new Geofence.Builder()
                    .setRequestId(entry.getKey())
                    .setCircularRegion(
                            entry.getValue().latitude,
                            entry.getValue().longitude,
                            Constants.GEOFENCE_SERVICE_RADIUS_IN_METERS
                    )
                    .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build());
        }

        if (mGeofenceList.size() < 1) {
            Log.d(TAG_GEO, "startMonitorGeofences: geofence list is empty!");
            return;
        }

        try {
            mGeofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                    .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG_GEO, "onSuccess: add geofences success");
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG_GEO, "onFailure: " + e.getMessage());
                        }
                    });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void stopMonitorGeofences() {
        mGeofencingClient.removeGeofences(getGeofencePendingIntent())
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG_GEO, "onSuccess: remove geofences success");
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG_GEO, "onFailure: " + e.getMessage());
                    }
                });
    }

    private void closeDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void updateUI() {
        if (checkLogin()) {
            AssetManager am = getResources().getAssets();
            InputStream is = null;

            try {
                is = am.open("lake.png");
                if (is != null) {
                    Bitmap bm = BitmapFactory.decodeStream(is);
                    //mTestImage.setImageBitmap(bm);
                    mProfileImage.setImageBitmap(bm);
                    is.close();

                    /*
                    File imgFile = new  File("/sdcard/Images/test_image.jpg");

                    if(imgFile.exists()){
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        mTestImage.setImageBitmap(myBitmap);
                    }
                    */

                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    String email = currentUser.getEmail();
                    //String name = email.substring(0, email.indexOf('@'));
                    String uid = currentUser.getUid();
                    mProfileName.setText(uid);
                    mProfileEmail.setText(email);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            //mTestImage.setImageResource(R.mipmap.ic_launcher_round);
            mProfileImage.setImageResource(R.mipmap.ic_launcher_round);
            mProfileName.setText("Android Studio");
            mProfileEmail.setText("android.studio@android.com");
        }

        //mTestResult.setText(mOutput);
        //mAddressResult.setText(mAddressOutput);
    }

    private boolean checkLogin() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null || !user.isEmailVerified()) {
            return false;
        }
        return true;
    }

    private void doSignOut() {
        if (checkLogin()) {
            mAuth.signOut();
            Snackbar.make(mProfileImage, "Logout Success.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            updateUI();
        } else {
            Snackbar.make(mProfileImage, "You are not logged in.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    private void goLoginStep() {
        // analytics log event test
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "R.id.imageView");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Profile Image");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        Intent intent = new Intent(this, LoginActivity.class);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void goAccount() {
        //Intent intent = new Intent(this, ScrollingActivity.class);
        Intent intent = new Intent(this, SettingsActivity.class);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void goCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    private void goMarkerGeneration() {
        if (checkLogin()) {
            Intent intent = new Intent(getApplicationContext(), MarkerGenerationActivity.class);
            startActivity(intent);
        } else {
            Snackbar.make(mProfileImage, "Please, Log in first.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    private void goGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_TAKE_ALBUM);
    }

    private void goARCamera() {
        //Intent intent = new Intent(this, ARCameraActivity.class);
        Intent intent = new Intent(this, ARCoreTestActivity.class);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void goOption() {
        Intent intent = new Intent(this, BottomActivity.class);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void goLocation() {
        //findPlace(mTestImage);
        findPlaceByPicker();
    }

    private void goTestUpload() {
        if (checkLogin()) {
            StorageReference storageRef = mStorage.getReference("images/lake.png");

            /*
            mTestImage.setDrawingCacheEnabled(true);
            mTestImage.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) mTestImage.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = storageRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Snackbar.make(mProfileImage, "Storage Upload Failed !!!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Snackbar.make(mProfileImage, "Storage Upload Success.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    // test result
                    String result = "Upload Success." + "\n" +
                            "file url : images/lake.png" + "\n" +
                            "file size : " + data.length;
                    mTestResult.setText(result);
                }
            });
            */

        } else {
            Snackbar.make(mProfileImage, "Please, Log in first.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    private void goTestImageGrid() {
        if (checkLogin()) {
            Intent intent = new Intent(this, ImageGridActivity.class);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }

        } else {
            Snackbar.make(mProfileImage, "Please, Log in first.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    private void goTestTabbed() {
        if (checkLogin()) {
            Intent intent = new Intent(this, TabbedActivity.class);
            if (intent.resolveActivity(getPackageManager()) != null) {
                intent.putExtra("ParentClassSource", ChooseActivity.class.getName());
                startActivity(intent);
            }

        } else {
            Snackbar.make(mProfileImage, "Please, Log in first.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    private void goTestGetList() {
        if (checkLogin()) {
            Intent intent = new Intent(this, ItemListActivity.class);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        } else {
            Snackbar.make(mProfileImage, "Please, Log in first.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    private void goSignupStep() {
        Intent intent = new Intent(this, SignupActivity.class);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void getContentInfo(String contentid) {
        //ListView list;

    }

    private void goAugmentedImageActivity() {
        Intent intent = new Intent(this, AugmentedImageActivity.class);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void uploadMarkerImage() {
        try {
            Cursor returnCursor = getContentResolver().query(mDBManager.imageURI,
                    null, null, null, null);
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
            returnCursor.moveToFirst();
            String name = returnCursor.getString(nameIndex);
            long size = returnCursor.getLong(sizeIndex);
            String path = mDBManager.imageURI.getPath();
            String suffix = name.substring(name.indexOf('.'), name.length());
            FirebaseUser currentUser = mAuth.getCurrentUser();
            String userid = currentUser.getUid();

            Map<String, Object> values = new HashMap<>();
            values.put("path", path);
            values.put("name", name);
            values.put("suffix", suffix);
            values.put("size", size);
            values.put("user_id", userid);
            TransferModel model = new TransferModel(values);

            Map<String, Object> address = new HashMap<>();
            address.put("country_code", mDBManager.currentCountryCode);
            address.put("locality", mDBManager.currentLocality);
            address.put("thoroughfare", mDBManager.currentThoroughfare);

            // upload marker
            mRequestManager.requestUploadMarkerToStorage(model, mDBManager.imageURI, address, new RequestManager.TransferCallback() {
                @Override
                public void onResponse(TransferModel result) {
                    Map<String, Object> data = new HashMap<>();

                    data.put("marker_id", ""); // new marker
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    String userid = currentUser.getUid();
                    data.put("user_id", userid);
                    data.put("file", result.getPath());
                    data.put("rating", (float) mDBManager.markerRating);
                    GeoPoint location = new GeoPoint(mDBManager.currentLatitude, mDBManager.currentLongtitude);
                    data.put("location", location);
                    data.put("content_id", mDBManager.contentId);
                    data.put("content_rotation", mDBManager.contentRotation);
                    data.put("content_scale", mDBManager.contentScale);
                    data.put("country_code", mDBManager.currentCountryCode);
                    data.put("locality", mDBManager.currentLocality);
                    data.put("thoroughfare", mDBManager.currentThoroughfare);

                    // update marker database
                    updateMarkerDB(data);
                }
            });

        } catch (Exception e) {
            Log.e("TAKE_ALBUM getData failed. ", e.toString());
        }
    }

    private void updateMarkerDB(Map<String, Object> data) {
        MarkerModel marker = new MarkerModel(data);
        mRequestManager.requestSetMarkerInfo(marker, new RequestManager.SuccessCallback() {
            @Override
            public void onResponse(boolean success) {
                Toast.makeText(ChooseActivity.this,
                        "marker registration success.",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

}
