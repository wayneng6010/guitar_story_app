package com.inti.student.criminalintent;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CartActivity extends AppCompatActivity {
    private RecyclerView mItemRecyclerView;
    private ItemAdapter mAdapter;
    private ItemPurchaseDataSource datasource;
    private RecyclerView.LayoutManager mLayoutManager;
//    private ItemAdapter adapter;
    private RecyclerView listView;
    private String mItemName;
    private ItemLab itemLab = ItemLab.get(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_item_cart);

        listView = findViewById(R.id.itemCartView);
        mLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(mLayoutManager);

        datasource = new ItemPurchaseDataSource(this);
        datasource.open();

        ArrayList<ItemPurchase> values = datasource.getAllItemPurchase();
        //UUID itemUUID = UUID.fromString(values.get(2).toString());
        for (ItemPurchase member : values){
            Log.i("Member name: ", String.valueOf(member.getStatus()));
        }
        Toast.makeText(getApplicationContext(),values.toString(),Toast.LENGTH_SHORT).show();

        //Item item = itemLab.getItem(itemUUID);

        //mImageName = item.getImageName();

//        ArrayAdapter<ItemPurchase> adapter = new ArrayAdapter<ItemPurchase>(this, android.R.layout.simple_list_item_1, values);
//        setListAdapter(adapter);

//        adapter = new ItemAdapter(values);
//        listView.setAdapter(adapter);

        mAdapter = new ItemAdapter(values);
        listView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume(){
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause(){
        datasource.close();
        super.onPause();
    }

    private class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mNameTextView;
        private TextView mCatTextView;
        private TextView mPriceTextView;
        private ImageView mItemImageView;
        private NumberPicker mQtyNumberPicker;
        private String mImageName;
        private int mImageID;
        private ItemPurchase mItem;
        private UUID mItemUUID;

        public ItemHolder(View itemView) {
            super(itemView);
            mNameTextView = (TextView) itemView.findViewById(R.id.cart_item_name);
            mCatTextView = (TextView) itemView.findViewById(R.id.cart_item_category);
            mPriceTextView = (TextView) itemView.findViewById(R.id.cart_item_price);
            mItemImageView = (ImageView) itemView.findViewById(R.id.cart_item_image);
            mQtyNumberPicker = (NumberPicker) itemView.findViewById(R.id.cart_item_quantity);
            itemView.setOnClickListener(this);
        }

        public void bindItem(ItemPurchase item) {
            mItem = item;

            UUID itemId = UUID.fromString(mItem.getItemId());
            Item item_info = itemLab.getItem(itemId);

            //mItemUUID = mItem.getId();
            mNameTextView.setText(item_info.getName());
            mCatTextView.setText(item_info.getCategory());
            mPriceTextView.setText("RM" + String.valueOf(item_info.getPrice()));

            mImageName = item_info.getImageName();
            mImageID = getResources().getIdentifier(mImageName,"drawable", "com.inti.student.criminalintent");
            mItemImageView.setImageResource(mImageID);

            mQtyNumberPicker.setValue(item.getQty());
        }

        @Override
        public void onClick(View view) {
//            Intent intent = new Intent(view.getContext(),ItemDetailsActivity.class);
//            intent.putExtra("itemUUID", mItemUUID.toString());
//            startActivity(intent);
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
            View view = layoutInflater.inflate(R.layout.item_cart, parent, false);
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