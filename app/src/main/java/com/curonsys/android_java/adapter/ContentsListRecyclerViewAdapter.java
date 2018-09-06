package com.curonsys.android_java.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.curonsys.android_java.R;
import com.curonsys.android_java.activity.ItemDetailActivity;
import com.curonsys.android_java.activity.ItemListActivity;
import com.curonsys.android_java.fragment.ContentModelDetailFragment;
import com.curonsys.android_java.model.ContentModel;

import java.util.List;

public class ContentsListRecyclerViewAdapter
        extends RecyclerView.Adapter<ContentsListRecyclerViewAdapter.ViewHolder> {

    private final ItemListActivity mParentActivity;
    private final List<ContentModel> mValues;
    private final boolean mTwoPane;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ContentModel item = (ContentModel) view.getTag();
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putString(ContentModelDetailFragment.ARG_ITEM_ID, item.getContentId());
                arguments.putSerializable(ContentModelDetailFragment.ARG_ITEM, item);
                ContentModelDetailFragment fragment = new ContentModelDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .add(R.id.item_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, ItemDetailActivity.class);
                intent.putExtra(ContentModelDetailFragment.ARG_ITEM_ID, item.getContentId());
                intent.putExtra(ContentModelDetailFragment.ARG_ITEM, item);
                context.startActivity(intent);
            }
        }
    };

    public ContentsListRecyclerViewAdapter(ItemListActivity parent,
                                  List<ContentModel> items,
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
        holder.mIdView.setText(mValues.get(position).getFormat());
        holder.mContentView.setText(mValues.get(position).getContentName());

        holder.itemView.setTag(mValues.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mIdView;
        final TextView mContentView;

        ViewHolder(View view) {
            super(view);
            mIdView = (TextView) view.findViewById(R.id.id_text);
            mContentView = (TextView) view.findViewById(R.id.content);
        }
    }
}

