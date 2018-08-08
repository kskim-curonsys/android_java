package com.curonsys.android_java;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.io.InputStream;

public class ChooseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView mImageView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mAuth = FirebaseAuth.getInstance();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        mImageView = (ImageView) header.findViewById(R.id.imageView);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });

        FirebaseUser currentUser = mAuth.getCurrentUser();
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
        String msg = "";

        if (id == R.id.nav_camera) {
            msg = "Import";

        } else if (id == R.id.nav_gallery) {
            msg = "Gallery";

        } else if (id == R.id.nav_slideshow) {
            msg = "Slideshow";

        } else if (id == R.id.nav_manage) {
            msg = "Tools";
            goOption();

        } else if (id == R.id.nav_share) {
            msg = "Share";

        } else if (id == R.id.nav_send) {
            msg = "Send";
            try {
                doSignOut();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        Snackbar.make(mImageView, msg, Snackbar.LENGTH_LONG).setAction("Action", null).show();

        return true;
    }

    private void updateUI(FirebaseUser user) throws IOException {
        if (user == null || !user.isEmailVerified()) {
            // default
            mImageView.setImageResource(R.mipmap.ic_launcher_round);

        } else {
            // logged on
            AssetManager am = getResources().getAssets();
            InputStream is = null;

            try {
                is = am.open("lake.png");
                if (is != null) {
                    Bitmap bm = BitmapFactory.decodeStream(is);
                    mImageView.setImageBitmap(bm);
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkLogin() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            goLoginStep();
            return;
        }
        if (!user.isEmailVerified()) {
            goLoginStep();
            return;
        }

        String hello = "Hi " + user.getEmail();
        Snackbar.make(mImageView, hello, Snackbar.LENGTH_LONG).setAction("Action", null).show();

        goAccount();
    }

    private void goLoginStep() {
        Intent intent = new Intent(this, LoginActivity.class);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void doSignOut() throws IOException {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Snackbar.make(mImageView, "not logged on yet!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return;
        }
        if (!user.isEmailVerified()) {
            Snackbar.make(mImageView, "not logged on yet!!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return;
        }
        String email = user.getEmail();
        String saygoodbye = "Bye " + email + " : logged out.";
        mAuth.signOut();

        Snackbar.make(mImageView, saygoodbye, Snackbar.LENGTH_LONG).setAction("Action", null).show();

        // image change (user -> default)
    }

    private void goOption() {
        Intent intent = new Intent(this, AccountActivity.class);
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
}
