/*
 * Copyright 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.curonsys.android_java.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.curonsys.android_java.model.ContentModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A sample which shows how to use {@link android.support.v4.widget.SwipeRefreshLayout} within a
 * {@link android.support.v4.app.ListFragment} to add the 'swipe-to-refresh' gesture to a
 * {@link android.widget.ListView}. This is provided through the provided re-usable
 * {@link SwipeRefreshListFragment} class.
 *
 * <p>To provide an accessible way to trigger the refresh, this app also provides a refresh
 * action item. This item should be displayed in the Action Bar's overflow item.
 *
 * <p>In this sample app, the refresh updates the ListView with a random set of new items.
 *
 * <p>This sample also provides the functionality to change the colors displayed in the
 * {@link android.support.v4.widget.SwipeRefreshLayout} through the options menu. This is meant to
 * showcase the use of color rather than being something that should be integrated into apps.
 */
public class ContentSwipeRefreshListFragment extends SwipeRefreshListFragment {
    private static final String TAG = ContentSwipeRefreshListFragment.class.getSimpleName();

    public static final String ARG_ITEM = "item";
    public static final String ARG_ITEM_ID = "item_id";

    private String mContentId;
    private ContentModel mItem;

    private ArrayList<String> mValues = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mContentId = getArguments().getString(ARG_ITEM_ID);
            mItem = (ContentModel) getArguments().getSerializable(ARG_ITEM);
        }
    }

    // BEGIN_INCLUDE (setup_views)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setListValues();
        /**
         * Create an ArrayAdapter to contain the data for the ListView. Each item in the ListView
         * uses the system-defined simple_list_item_1 layout that contains one TextView.
         */
        ListAdapter adapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                mValues);

        // Set the adapter between the ListView and its backing data.
        setListAdapter(adapter);

        // BEGIN_INCLUDE (setup_refreshlistener)
        /**
         * Implement {@link SwipeRefreshLayout.OnRefreshListener}. When users do the "swipe to
         * refresh" gesture, SwipeRefreshLayout invokes
         * {@link SwipeRefreshLayout.OnRefreshListener#onRefresh onRefresh()}. In
         * {@link SwipeRefreshLayout.OnRefreshListener#onRefresh onRefresh()}, call a method that
         * refreshes the content. Call the same method in response to the Refresh action from the
         * action bar.
         */
        setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "onRefresh called from SwipeRefreshLayout");

                initiateRefresh();
            }
        });
        // END_INCLUDE (setup_refreshlistener)
    }
    // END_INCLUDE (setup_views)

    // BEGIN_INCLUDE (initiate_refresh)
    /**
     * By abstracting the refresh process to a single method, the app allows both the
     * SwipeGestureLayout onRefresh() method and the Refresh action item to refresh the content.
     */
    private void initiateRefresh() {
        Log.i(TAG, "initiateRefresh");

        /**
         * Execute the background task, which uses {@link android.os.AsyncTask} to load the data.
         */
        //refreshList();
    }
    // END_INCLUDE (initiate_refresh)

    private void refreshList() {
        //getContents();
        //setListValues();

        setRefreshing(false);
    }

    private void setListValues() {
        if (mItem != null) {
            mValues.add(mItem.getContentId());
            mValues.add(mItem.getContentName());
            mValues.add(mItem.getFormat());
            mValues.add(mItem.getVersion());
            for (int i = 0; i < mItem.getContentUrl().size(); i++) {
                mValues.add(mItem.getContentUrl().get(i));
            }
        }
    }
}
