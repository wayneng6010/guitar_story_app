package com.inti.student.criminalintent;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;
import java.util.UUID;

public class ItemDetailsActivity extends AppCompatActivity {

    private ItemPurchaseDataSource datasource;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_details);

        datasource = new ItemPurchaseDataSource(this);
        datasource.open();

        ImageView mImageView = (ImageView) findViewById(R.id.details_item_image);
        TextView mPriceView = (TextView) findViewById(R.id.details_item_price);
        TextView mItemNameView = (TextView) findViewById(R.id.details_item_name);
        TextView mCategoryView = (TextView) findViewById(R.id.details_item_cat);
        TextView mDescriptionView = (TextView) findViewById(R.id.details_item_description);

        final String itemUUID = getIntent().getStringExtra("itemUUID");

        ItemLab itemLab = ItemLab.get(this);
        Item item = itemLab.getItem(itemUUID);

        String mImageName = item.getImageName();
        int mImageID = getResources().getIdentifier(mImageName, "drawable", "com.inti.student.criminalintent");
        mImageView.setImageResource(mImageID);

        mPriceView.setText("RM" + item.getPrice());
        mItemNameView.setText(item.getName());
        mCategoryView.setText(item.getCategory());
        mDescriptionView.setText(item.getDescription());

        Button mAddToCartBtn = (Button) findViewById(R.id.add_to_cart_btn);
        mAddToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Intent i = new Intent(MainActivity.this, AddActivity.class);
                //startActivityForResult(i, 1);
                final NumberPicker itemQtyNp = new NumberPicker(ItemDetailsActivity.this);
                itemQtyNp.setMinValue(1);
                itemQtyNp.setMaxValue(10);
                itemQtyNp.setWrapSelectorWheel(true);

                AlertDialog dialog = new AlertDialog.Builder(ItemDetailsActivity.this)
                        .setTitle("Add to Cart")
                        .setMessage("Quantity")
                        .setView(itemQtyNp)
                        .setPositiveButton("Add to Cart", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ItemPurchase itemPurchase = null;
                                int itemQty = itemQtyNp.getValue();
                                itemPurchase = datasource.createItemPurchase(String.valueOf(itemUUID), itemQty, "pending");
                                Toast.makeText(getApplicationContext(),"Added to Cart",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ItemDetailsActivity.this,CartActivity.class));
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
            }
        });
    }
}
