package com.curonsys.android_java.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.curonsys.android_java.R;
import com.curonsys.android_java.model.ContentModel;


public class ContentModelDetailFragment extends Fragment {
    private static final String TAG = ContentModelDetailFragment.class.getSimpleName();

    public static final String ARG_ITEM = "item";
    public static final String ARG_ITEM_ID = "item_id";

    private String mContentId;
    private ContentModel mItem;

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
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_content_model_detail, container, false);

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

        return rootView;
    }
}
