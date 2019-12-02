package com.inti.student.criminalintent;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PurchasedItemActivity extends AppCompatActivity {
    private ItemAdapter mAdapter;
    private ItemPurchaseDataSource datasource;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView listView;
    private ItemLab itemLab = ItemLab.get(this);

    private RelativeLayout mEmptyPurchasedLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // hide default title bar
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }

        setContentView(R.layout.recyclerview_purchased_item);

        datasource = new ItemPurchaseDataSource(this);
        datasource.open();

        listView = findViewById(R.id.purchased_item_view);
        mLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(mLayoutManager);

        // get all purchased item from database
        ArrayList<ItemPurchase> values = datasource.getAllPurchasedItem();

        mEmptyPurchasedLayout = (RelativeLayout) findViewById(R.id.purchased_item_empty_layout);
        if (values.size() == 0){ // if the purchased item is empty
            mEmptyPurchasedLayout.setVisibility(View.VISIBLE);
        } else {
            mEmptyPurchasedLayout.setVisibility(View.GONE);
        }

        //Toast.makeText(getApplicationContext(),values.toString(),Toast.LENGTH_SHORT).show();
        mAdapter = new ItemAdapter(values);
        listView.setAdapter(mAdapter);

    }

    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }

    private class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mNameTextView;
        private TextView mCatTextView;
        private TextView mPriceTextView;
        private ImageView mItemImageView;
        private TextView mQuantityTextView;
        private ItemPurchase mItem;
        private String itemId;
        private String mImageName;
        private int mImageID;

        public ItemHolder(View itemView) {
            super(itemView);
            mNameTextView = (TextView) itemView.findViewById(R.id.purchased_item_name);
            mCatTextView = (TextView) itemView.findViewById(R.id.purchased_item_category);
            mPriceTextView = (TextView) itemView.findViewById(R.id.purchased_item_price);
            mItemImageView = (ImageView) itemView.findViewById(R.id.purchased_item_image);
            mQuantityTextView = (TextView) itemView.findViewById(R.id.purchased_item_quantity);

            itemView.setOnClickListener(this);
        }

        public void bindItem(ItemPurchase item) {
            mItem = item;

            itemId = mItem.getItemId();
            Item item_info = itemLab.getItem(itemId);

            mNameTextView.setText(item_info.getName());
            mCatTextView.setText(item_info.getCategory());
            mPriceTextView.setText("RM" + String.valueOf(item_info.getPrice()));

            mImageName = item_info.getImageName();
            mImageID = getResources().getIdentifier(mImageName, "drawable", "com.inti.student.criminalintent");
            mItemImageView.setImageResource(mImageID);

            mQuantityTextView.setText("x" + String.valueOf(mItem.getQty()));
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), ItemDetailsActivity.class);
            intent.putExtra("itemId", itemId);
            startActivity(intent);
        }


    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {
        private List<ItemPurchase> mItems;

        public ItemAdapter(List<ItemPurchase> items) {
            mItems = items;
        }

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.purchased_item, parent, false);
            return new ItemHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {
            ItemPurchase item = mItems.get(position);
            holder.bindItem(item);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

    }
}
