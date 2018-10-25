package com.curonsys.android_java.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.curonsys.android_java.R;
import com.curonsys.android_java.adapter.MyContentDetailAdapter;
import com.curonsys.android_java.model.ContentModel;

import java.util.ArrayList;


public class ContentModelDetailFragment extends Fragment {
    private static final String TAG = ContentModelDetailFragment.class.getSimpleName();

    public static final String ARG_ITEM = "item";
    public static final String ARG_ITEM_ID = "item_id";

    private String mContentId;
    private ContentModel mItem;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public ContentModelDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mContentId = getArguments().getString(ARG_ITEM_ID);
            mItem = (ContentModel)getArguments().getSerializable(ARG_ITEM);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getContentName());
                appBarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
                appBarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_content_model_detail, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_content_detail_view);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        ArrayList<String> list = new ArrayList<String>();
        list.add("Content ID : " + mItem.getContentId());
        list.add("Content Name : " + mItem.getContentName());
        list.add("Description : " + mItem.getDescription());
        list.add("3D : " + mItem.get3D());
        list.add("Animation : " + mItem.getAnimation());
        list.add("Model : " + mItem.getModel());
        list.add("Thumb : " + mItem.getThumb());
        list.add("Textures");
        int tsize = mItem.getTextures().size();
        for (int i = 0; i < tsize; i++) {
            list.add(" > " + mItem.getTextures().get(i));
        }

        mRecyclerView.setHasFixedSize(false);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyContentDetailAdapter(list);
        mRecyclerView.setAdapter(mAdapter);

        /*
        if (mItem != null) {
            String output = "id: " + mItem.getContentId() +
                            "\nname: " + mItem.getContentName() +
                            "\nformat: " + mItem.getFormat() +
                            "\nversion: " + mItem.getVersion() +
                            "\n3d: " + mItem.get3D() +
                            "\n";
            String path = "path: ";
            for (int i = 0; i < mItem.getContentUrl().size(); i++) {
                path += "\n - " + mItem.getContentUrl().get(i);
            }
            if (mItem.getContentUrl().size() > 0) {
                output += path;
            }

            ((TextView) rootView.findViewById(R.id.model_detail)).setText(output);
        }
        */

        return rootView;
    }
}
