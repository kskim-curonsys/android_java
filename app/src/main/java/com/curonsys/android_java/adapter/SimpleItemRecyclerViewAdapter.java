package com.curonsys.android_java.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.curonsys.android_java.R;
import com.curonsys.android_java.activity.ItemDetailActivity;
import com.curonsys.android_java.activity.ItemDetailFragment;
import com.curonsys.android_java.activity.ItemListActivity;
import com.curonsys.android_java.dummy.DummyContent;

import java.util.List;

public class SimpleItemRecyclerViewAdapter
        extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {
    private final ItemListActivity mParentActivity;
    private final List<DummyContent.DummyItem> mValues;
    private final boolean mTwoPane;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putString(ItemDetailFragment.ARG_ITEM_ID, item.id);
                ItemDetailFragment fragment = new ItemDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, ItemDetailActivity.class);
                intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id);

                context.startActivity(intent);
            }
        }
    };

    public SimpleItemRecyclerViewAdapter(ItemListActivity parent,
                                  List<DummyContent.DummyItem> items,
                                  boolean twoPane) {
        mValues = items;
        mParentActivity = parent;
        mTwoPane = twoPane;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mName.setText(mValues.get(position).getId());
        holder.mDescription.setText(mValues.get(position).getDetails());
        // get image..
        holder.mThumbnail.setImageResource(R.mipmap.ic_launcher_round);


        holder.itemView.setTag(mValues.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mName;
        final TextView mDescription;
        final ImageView mThumbnail;
        final Button mSelect;

        ViewHolder(View view) {
            super(view);
            mName = (TextView) view.findViewById(R.id.text_name);
            mDescription = (TextView) view.findViewById(R.id.text_description);
            mThumbnail = (ImageView) view.findViewById(R.id.image_thumbnail);
            mSelect = (Button) view.findViewById(R.id.button_select);
        }
    }

}
