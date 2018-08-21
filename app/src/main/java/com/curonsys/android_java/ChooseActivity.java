package com.curonsys.android_java;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import eu.kudan.kudan.ARAPIKey;

import com.curonsys.android_java.activity.*;
import com.curonsys.android_java.utils.PermissionManager;

public class ChooseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int MY_PERMISSION_STORAGE = 1;
    private static final int REQUEST_TAKE_PHOTO = 2;
    private static final int REQUEST_TAKE_ALBUM = 3;
    private static final int REQUEST_IMAGE_CROP = 4;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private FirebaseStorage mStorage;
    private FirebaseAnalytics mAnalytics;

    private ImageView mTestImage;
    private ImageView mProfileImage;
    private TextView mProfileName;
    private TextView mProfileEmail;
    private TextView mTestResult;

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

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance("gs://my-first-project-7e28c.appspot.com");   // ("gs://gce-storage-army");
        mAnalytics = FirebaseAnalytics.getInstance(this);
        FirebaseUser currentUser = mAuth.getCurrentUser();

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
        updateUI();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    // setInputImage()
                    // cropImage()
                    // start crop activity

                } else {
                    // cancel

                }

                break;

            case REQUEST_TAKE_ALBUM:
                if (resultCode == Activity.RESULT_OK) {
                    // getData()
                    // setPhotoURI()
                    // setInputImage()

                    // cropImage()
                    // start crop activity

                } else {

                }

                break;

            case REQUEST_IMAGE_CROP:
                if (resultCode == Activity.RESULT_OK) {
                    // galleryAddPic()
                    // getAlbumURI()
                    // setImageURI()

                    // DBManager
                    // - imageURI
                    // - generatorId

                    // getBitmap()
                    // CheckingAsyncTask.execute()

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.choose, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
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
        // download from storage

    }

    private void goTestGetList() {

    }

    private void goTestGetContent() {

    }
}
