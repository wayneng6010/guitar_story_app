package com.inti.student.criminalintent;

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

import java.util.List;

public class TaskListFragment extends Fragment {
    private RecyclerView mItemRecyclerView;
    private ItemAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        mItemRecyclerView = (RecyclerView) view.findViewById(R.id.item_recycle_view);
        mItemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    private void updateUI(){
        ItemLab itemLab = ItemLab.get(getActivity());
        List<Item> items = itemLab.getItems();

        mAdapter = new ItemAdapter(items);
        mItemRecyclerView.setAdapter(mAdapter);
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mNameTextView;
        private TextView mCatTextView;
        private TextView mPriceTextView;
        private ImageView mItemImageView;
        private String mImageName;
        private int mImageID;
        private Item mItem;

        public CrimeHolder(View itemView) {
            super(itemView);
            mNameTextView = (TextView) itemView.findViewById(R.id.list_item_name);
            mCatTextView = (TextView) itemView.findViewById(R.id.list_item_cat);
            mPriceTextView = (TextView) itemView.findViewById(R.id.list_item_price);
            mItemImageView = (ImageView) itemView.findViewById(R.id.list_item_image);
            itemView.setOnClickListener(this);

        }

        public void bindCrime(Item item) {
            mItem = item;
            mNameTextView.setText(mItem.getName());
            mCatTextView.setText(mItem.getCategory());
            mPriceTextView.setText("RM" + String.valueOf(mItem.getPrice()));

            mImageName = mItem.getImageName();
            mImageID = getResources().getIdentifier(mImageName,"drawable", "com.inti.student.criminalintent");
            mItemImageView.setImageResource(mImageID);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(), mItem.getId() + " clicked!", Toast.LENGTH_SHORT).show();
        }
    }

    private class ItemAdapter extends RecyclerView.Adapter<CrimeHolder> {
        private List<Item> mItems;
        public ItemAdapter(List<Item> items) {
            mItems = items;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item, parent, false);
            return new CrimeHolder(view);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Item item = mItems.get(position);
            holder.bindCrime(item);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

    }
}