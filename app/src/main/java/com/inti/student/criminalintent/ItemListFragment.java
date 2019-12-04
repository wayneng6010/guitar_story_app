package com.inti.student.criminalintent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ItemListFragment extends Fragment {
    private RecyclerView mItemRecyclerView;
    private ItemAdapter mAdapter;
    DatabaseReference reff;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_item_list, container, false);

        mItemRecyclerView = (RecyclerView) view.findViewById(R.id.item_recycle_view);
        mItemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    private void updateUI() {

        ItemLab itemLab = ItemLab.get(getActivity());
        List<Item> items = itemLab.getItems();

        mAdapter = new ItemAdapter(items);
        mAdapter.notifyDataSetChanged();
        mItemRecyclerView.setAdapter(mAdapter);

        reff = FirebaseDatabase.getInstance().getReference().child("item");
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        mAdapter.notifyDataSetChanged();
                        mItemRecyclerView.setAdapter(mAdapter);
                    }
                } else{
                    Toast.makeText(getContext(),"No item found",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mNameTextView;
        private TextView mCatTextView;
        private TextView mPriceTextView;
        private ImageView mItemImageView;
        private String mImageName;
        private int mImageID;
        private Item mItem;
        private String mItemId;

        public ItemHolder(View itemView) {
            super(itemView);
            mNameTextView = (TextView) itemView.findViewById(R.id.list_item_name);
            mCatTextView = (TextView) itemView.findViewById(R.id.list_item_cat);
            mPriceTextView = (TextView) itemView.findViewById(R.id.list_item_price);
            mItemImageView = (ImageView) itemView.findViewById(R.id.list_item_image);
            itemView.setOnClickListener(this);
        }

        public void bindItem(Item item) {
            mItem = item;
            mItemId = mItem.getId();
            mNameTextView.setText(mItem.getName());
            mCatTextView.setText(mItem.getCategory());
            mPriceTextView.setText("RM" + String.valueOf(mItem.getPrice()));

            mImageName = mItem.getImageName();
            mImageID = getResources().getIdentifier(mImageName, "drawable", "com.inti.student.criminalintent");
            mItemImageView.setImageResource(mImageID);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), ItemDetailsActivity.class);
            intent.putExtra("itemId", mItemId);
            startActivity(intent);
        }
    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {
        private List<Item> mItems;

        public ItemAdapter(List<Item> items) {
            mItems = items;
        }

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.item_list, parent, false);
            return new ItemHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {
            Item item = mItems.get(position);
            holder.bindItem(item);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

    }
}
