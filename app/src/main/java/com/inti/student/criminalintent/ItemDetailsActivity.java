package com.inti.student.criminalintent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;
import java.util.UUID;

public class ItemDetailsActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_details);

        ImageView mImageView = (ImageView) findViewById(R.id.details_item_image);
        TextView mPriceView = (TextView) findViewById(R.id.details_item_price);
        TextView mItemNameView = (TextView) findViewById(R.id.details_item_name);
        TextView mCategoryView = (TextView) findViewById(R.id.details_item_cat);
        TextView mDescriptionView = (TextView) findViewById(R.id.details_item_description);

        UUID itemUUID = UUID.fromString(getIntent().getStringExtra("itemUUID"));

        ItemLab itemLab = ItemLab.get(this);
        Item item = itemLab.getItem(itemUUID);

        String mImageName = item.getImageName();
        int mImageID = getResources().getIdentifier(mImageName, "drawable", "com.inti.student.criminalintent");
        mImageView.setImageResource(mImageID);

        mPriceView.setText("RM" + item.getPrice());
        mItemNameView.setText(item.getName());
        mCategoryView.setText(item.getCategory());
        mDescriptionView.setText(item.getDescription());


    }
}
