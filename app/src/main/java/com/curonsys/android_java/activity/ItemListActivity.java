package com.curonsys.android_java.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.curonsys.android_java.adapter.ContentsListRecyclerViewAdapter;
import com.curonsys.android_java.adapter.SimpleItemRecyclerViewAdapter;
import com.curonsys.android_java.dummy.DummyContent;
import com.curonsys.android_java.R;
import com.curonsys.android_java.http.RequestManager;
import com.curonsys.android_java.model.ContentModel;
import com.curonsys.android_java.model.DownloadModel;
import com.curonsys.android_java.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity {

    private static final String TAG = ItemListActivity.class.getSimpleName();

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private ArrayList<ContentModel> mItems = new ArrayList<ContentModel>();

    private FirebaseAuth mAuth;
    private RequestManager mRequestManager;

    private MaterialDialog mMaterialProgress = null;
    private MaterialDialog.Builder mMaterialBuilder = null;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(TAG, "onRefresh called from SwipeRefreshLayout");
                        updateList();
                    }
                }
        );

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        mRequestManager = RequestManager.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userid = currentUser.getUid();

        mMaterialBuilder = new MaterialDialog.Builder(this)
                .title("컨텐츠 다운로드")
                .content("컨텐츠 목록을 다운로드 중입니다...")
                .progress(true, 0);
        mMaterialProgress = mMaterialBuilder.build();
        if (currentUser != null && currentUser.isEmailVerified()) {
            mMaterialProgress.show();
            getContentsList(userid);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        /*
        ItemListActivity
         -> recyclerView.setAdapter
            -> ItemDetailActivity
               -> ContentModelDetailFragment
         */
        //recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, mTwoPane));
        recyclerView.setAdapter(new ContentsListRecyclerViewAdapter(this, mItems, mTwoPane));
    }

    private void getContentsList(String userid) {
        mRequestManager.requestGetUserInfo(userid, new RequestManager.UserCallback() {
            @Override
            public void onResponse(UserModel response) {
                Log.d(TAG, "onResponse: ContentListModel (" +
                        response.getUserId() + ", " + response.getName() + ", " + response.getImageUrl() + ")");
                ArrayList<String> ids = response.getContents();

                mRequestManager.requestGetContentsList(ids, new RequestManager.ContentsListCallback() {
                    @Override
                    public void onResponse(ArrayList<ContentModel> response) {
                        mItems = response;

                        mMaterialProgress.dismiss();
                        Log.d(TAG, "onResponse: contents list complete ");
                        View recyclerView = findViewById(R.id.item_list);
                        assert recyclerView != null;
                        setupRecyclerView((RecyclerView) recyclerView);
                    }
                });
            }
        });
    }

    private void updateList() {
        mSwipeRefreshLayout.setRefreshing(false);
    }
}

