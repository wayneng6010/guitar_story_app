package com.inti.student.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.UUID;

public class ItemDetailsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.item_details, container, false);

        //Bundle bundle = getActivity().getIntent().getExtras();
        //UUID itemUUID = UUID.fromString(bundle.getString("itemUUID"));

        UUID itemUUID = UUID.fromString(getArguments().getString("itemUUID"));


        ImageView mImageView = (ImageView) view.findViewById(R.id.details_item_image);
        TextView mPriceView = (TextView) view.findViewById(R.id.details_item_price);
        TextView mItemNameView = (TextView) view.findViewById(R.id.details_item_price);
        TextView mCategoryView = (TextView) view.findViewById(R.id.details_item_cat);
        TextView mDescriptionView = (TextView) view.findViewById(R.id.details_item_description);

        ItemLab itemLab = ItemLab.get(getActivity());
        Item item = itemLab.getItem(itemUUID);

        String mImageName = item.getImageName();
        int mImageID = getResources().getIdentifier(mImageName, "drawable", "com.inti.student.criminalintent");
        mImageView.setImageResource(mImageID);

        mPriceView.setText("RM" + String.valueOf(item.getPrice()));
        mItemNameView.setText(item.getName());
        mCategoryView.setText(item.getCategory());
        mDescriptionView.setText(item.getDescription());

        return view;
    }
}