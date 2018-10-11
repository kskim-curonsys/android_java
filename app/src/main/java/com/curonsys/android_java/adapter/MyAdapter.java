package com.curonsys.android_java.adapter;

import android.content.ClipData;
import android.content.Context;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.curonsys.android_java.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private static final String TAG = MyAdapter.class.getSimpleName();

    private ArrayList<String> mList;
    private ButtonClickCallback mClickCallback;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mNameTextView;
        public Button mMessageButton;

        public ViewHolder(View itemView) {
            super(itemView);

            mNameTextView = (TextView) itemView.findViewById(R.id.contact_name);
            mMessageButton = (Button) itemView.findViewById(R.id.message_button);
        }
    }

    public interface ButtonClickCallback {
        public void onClicked(final int position);
    }

    public MyAdapter(ArrayList<String> list, final ButtonClickCallback callback) {
        mList = list;
        mClickCallback = callback;
    }

    public void SetData(ArrayList<String> list) {
        mList = list;
    }

    public void AddData(String data) {
        mList.add(data);
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.my_text_view, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        TextView textView = viewHolder.mNameTextView;
        textView.setText(mList.get(position));

        Button button = viewHolder.mMessageButton;
        button.setText("Message " + position);
        button.setEnabled(true);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickCallback.onClicked(position);
                Log.d(TAG, "onClick: item " + position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
