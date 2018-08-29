package com.curonsys.android_java;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
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
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.curonsys.android_java.http.RequestManager;
import com.curonsys.android_java.model.ContentModel;
import com.curonsys.android_java.model.ContentsListModel;
import com.curonsys.android_java.model.UserContentsModel;
import com.curonsys.android_java.model.UserModel;
import com.curonsys.android_java.service.FetchAddressIntentService;
import com.curonsys.android_java.service.GeofenceTransitionsIntentService;
import com.curonsys.android_java.utils.Constants;
import com.curonsys.android_java.utils.DBManager;
import com.curonsys.android_java.utils.LocationUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import eu.kudan.kudan.ARAPIKey;

import com.curonsys.android_java.activity.*;
import com.curonsys.android_java.utils.PermissionManager;

import static com.curonsys.android_java.utils.LocationUtil.distanceFrom;
import static com.curonsys.android_java.utils.LocationUtil.latitudeInDifference;
import static com.curonsys.android_java.utils.LocationUtil.longitudeInDifference;

public class ChooseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int MY_PERMISSION_STORAGE = 1;
    private static final int REQUEST_TAKE_PHOTO = 2;
    private static final int REQUEST_TAKE_ALBUM = 3;
    private static final int REQUEST_IMAGE_CROP = 4;

    static final String LOCATION_UPDATE_STATE = "location_update_state";
    static final String LOCATION_HISTORY = "location_histry";

    private static final String TAG = ChooseActivity.class.getSimpleName();
    private static final String TAG_GEO = "Geofence";

    private String mOutput = "";
    private String mAddressOutput = "";
    private boolean mLocationUpdateState = false;

    private FirebaseAuth mAuth;
    private FirebaseStorage mStorage;
    private FirebaseAnalytics mAnalytics;

    private GoogleMap mGoogleMap;
    private LocationManager mLocationManager;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private RequestManager mRequestManager;
    private DBManager mDBManager = DBManager.getInstance();
    private GeofencingClient mGeofencingClient;
    private List<Geofence> mGeofenceList = new ArrayList<Geofence>();
    private PendingIntent mGeofencePendingIntent;

    private MaterialDialog mMaterialDialog = null;
    private MaterialDialog.Builder mMaterialBuilder = null;

    private ImageView mTestImage;
    private ImageView mProfileImage;
    private TextView mProfileName;
    private TextView mProfileEmail;
    private TextView mTestResult;
    private TextView mAddressResult;
    private Button mTestLocation;
    private Button mStopLocation;

    protected Location mLastLocation = null;
    private AddressResultReceiver mResultReceiver = new AddressResultReceiver(new Handler());

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultData == null) {
                return;
            }

            mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            if (mAddressOutput == null) {
                mAddressOutput = "";
            }

            if (resultCode == Constants.SUCCESS_RESULT) {
                updateUI();
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
        key.setAPIKey("JvTam0rZCqbJkMEJZuB4KevXbV0/CquncYpJU0FKx5PA+3miHVsmmBeUEepbEpH+RzFK4VPDJ4DzYOXixEO3ZGDiZkVR/AH2XegPgwIqMJlsSAtAvVErFXsQaOEYlq8SF4kvkEQgrlgXrJY/t6cDuxdBp5ecgf68eI+1KU8ObwjK8YQbXv+6s/3GSL6lVyVo62o2Sv2QkUfZEpjrl5hI1rzjJ70aNfpy0ddZgJSMNUF5gbsk+dtoFETdvmCDhlC58w2E23r8h+XpEvMZslkMFKlY/5zq7x4YSkYcEbAw4KTDsOj63dbO2lld49TOG2Bo3JHoIRgf5kFa03xj0JrQBsxG/gHhNQwYJGqMAhVSNvZEIQKRsS9UTmXOzsjysU8zpPoRuAbhXQAyUnkc7jdIGO49cSoVjR+QGx8bmpLlpxphNu90b1up75IJvrY/fX/EF7LgTfk5tXMRXhpdX90dAdtFiwSZYlcmY6bc/uxC9IqbwGeEQuw7508fte/7h3wBrvoRqS5948giz6VfR6Hxz7lPcwG4sYVPRKc4QsQm9DK8Ac5+QJWUwRUjClfJ0y59kDPoB/M4t2kOBlaJAHeSgijQor1IgXFGEIlLxRbf5qgWfjdGie2yeb84CrcGvCzt6IwltqrD4WbcI+HAJAJogY82hKhMEgJaulqpSIvlKQY=");

        PermissionManager permissionManager = new PermissionManager(this);
        permissionManager.permissionCheck();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottomnavigation);
        //navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance("gs://my-first-project-7e28c.appspot.com");
        mAnalytics = FirebaseAnalytics.getInstance(this);

        mRequestManager = RequestManager.getInstance();

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

        mLocationUpdateState = true;    // temp: default set
        getLastLocation();
        updateUI();

        mMaterialBuilder = new MaterialDialog.Builder(this)
                .title("위치 수신중")
                .content("현재 위치를 확인중입니다...")
                .progress(true, 0);
        mMaterialDialog = mMaterialBuilder.build();
        mMaterialDialog.show();
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
        // for abnormal termination
        outState.putBoolean(LOCATION_UPDATE_STATE, mLocationUpdateState);   // test
        outState.putString(LOCATION_HISTORY, mOutput);      // test

        super.onSaveInstanceState(outState);
    }

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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {

                } else {

                }

                break;

            case REQUEST_TAKE_ALBUM:
                if (resultCode == Activity.RESULT_OK) {

                } else {

                }

                break;

            case REQUEST_IMAGE_CROP:
                if (resultCode == Activity.RESULT_OK) {

                } else {

                }

                break;
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
        getMenuInflater().inflate(R.menu.choose, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            goCamera();

        } else if (id == R.id.nav_gallery) {
            goGallery();

        } else if (id == R.id.nav_arcamera) {
            goARCamera();

        } else if (id == R.id.nav_options) {
            goOption();

        } else if (id == R.id.nav_signup) {
            goSignupStep();

        } else if (id == R.id.nav_upload) {
            goTestUpload();

        } else if (id == R.id.nav_download) {
            goTestDownload();

        } else if (id == R.id.nav_gettest) {
            goTestGetList();

        } else if (id == R.id.nav_posttest) {
            goTestGetContent();

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
            mMaterialDialog.dismiss();

            LatLng currentLocation = new LatLng(latitude,longitude);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(currentLocation);
            markerOptions.title("현재 위치");
            markerOptions.snippet("ARZone");
            //mGoogleMap.addMarker(markerOptions);
            //mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16));
            //mLocationManager.removeUpdates(mLocationListener);  //  미수신할때는 반드시 자원해체를 해주어야 한다.
            mDBManager.markerLatitude = latitude;
            mDBManager.markerLongtitude = longitude;

            double testlat = 34.951302;      //
            double testlon = 127.689964;
            double testdist = distanceFrom(latitude, longitude, testlat, testlon);

            double difflat = latitudeInDifference(500);
            double difflon = longitudeInDifference(latitude, 500);

            mOutput += "lat : " + latitude + "\n" + "lon : " + longitude + "\n" + "speed : " + speed + "\n\n";
            mTestResult.setText(mOutput);

            Log.d(TAG, "onLocationChanged(lat): " + latitude);
            Log.d(TAG, "onLocationChanged(lon): " + longitude);
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

                                mOutput += "last lat : " + latitude + "\n" + "last lon : " + longitude + "\n" + " speed : " + speed + "m/s" + "\n\n";
                                mTestResult.setText(mOutput);
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
                    mTestImage.setImageBitmap(bm);
                    mProfileImage.setImageBitmap(bm);
                    is.close();

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
            mTestImage.setImageResource(R.mipmap.ic_launcher_round);
            mProfileImage.setImageResource(R.mipmap.ic_launcher_round);
            mProfileName.setText("Android Studio");
            mProfileEmail.setText("android.studio@android.com");
        }

        mTestResult.setText(mOutput);
        mAddressResult.setText(mAddressOutput);
    }

    private boolean checkLogin() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null || !user.isEmailVerified()) {
            return false;
        }
        return true;
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

    private void goSignupStep() {
        Intent intent = new Intent(this, SignupActivity.class);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
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

    private void goOption() {
        //Intent intent = new Intent(this, AccountActivity.class);
        //Intent intent = new Intent(this, MyaccountActivity.class);
        //Intent intent = new Intent(this, ItemListActivity.class);
        Intent intent = new Intent(this, ScrollingActivity.class);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void goAccount() {
        Intent intent = new Intent(this, BottomActivity.class);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void goARCamera() {
        Intent intent = new Intent(this, ARCameraActivity.class);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void goCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    private void goGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_TAKE_ALBUM);
    }

    private void goTestUpload() {
        if (checkLogin()) {
            StorageReference storageRef = mStorage.getReference("images/lake.png");

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
                    // Handle unsuccessful uploads
                    Snackbar.make(mProfileImage, "Storage Upload Failed !!!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    Snackbar.make(mProfileImage, "Storage Upload Success.", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                    // test result
                    String result = "Upload Success." + "\n" +
                            "file url : images/lake.png" + "\n" +
                            "file size : " + data.length;
                    mTestResult.setText(result);
                }
            });

            // test: uploaded info.
        } else {
            Snackbar.make(mProfileImage, "Please, Log in first.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    private void goTestDownload() {
        // test requestmanager
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userid = currentUser.getUid();

        // user
        /*
        mRequestManager.requestGetUserInfo(userid, new RequestManager.UserCallback() {
            @Override
            public void onResponse(UserModel response) {
                Log.d(TAG, "onResponse: ContentListModel (" +
                        response.getUserId() + ", " + response.getName() + ", " + response.getImageUrl() + ")");

                // contents list
                ArrayList<String> ids = response.getContents();
                for (int i = 0; i < ids.size(); i++) {
                    String content_id = ids.get(i);


                }
            }
        });
        */

        // content
        String content_id = "ZUZKrsGuGA9DdOwVBiWA";
        mRequestManager.requestGetContentInfo(content_id, new RequestManager.ContentCallback() {
            @Override
            public void onResponse(ContentModel response) {
                Log.d(TAG, "onResponse: ContentModel (" +
                        response.getContentId() + ", " + response.getContentName() + ", " + response.getVersion() + ")");

                mOutput += "onResponse: ContentModel (" + response.getContentId() + ", " + response.getContentName() + ", " + response.getVersion() + ")";
                mTestResult.setText(mOutput);
            }
        });
    }

    private void goTestGetList() {
        Intent intent = new Intent(this, MyaccountActivity.class);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void goTestGetContent() {
        Intent intent = new Intent(this, ItemListActivity.class);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
