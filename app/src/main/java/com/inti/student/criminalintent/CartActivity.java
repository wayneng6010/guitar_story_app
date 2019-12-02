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

public class CartActivity extends AppCompatActivity {
    private ItemAdapter mAdapter;
    private ItemPurchaseDataSource datasource;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView listView;
    private ItemLab itemLab = ItemLab.get(this);
    private int priceCounter = 0;
    private TextView mCartTotalAmountTextView;
    private Button mCheckoutBtn;
    private RelativeLayout mEmptyCartLayout;
    private ImageView mDeleteImageView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // hide default title bar
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }

        setContentView(R.layout.recyclerview_item_cart);

        datasource = new ItemPurchaseDataSource(this);
        datasource.open();

        listView = findViewById(R.id.cart_item_view);
        mLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(mLayoutManager);

        // get all cart item from database
        ArrayList<ItemPurchase> values = datasource.getAllCartItem();
        for (ItemPurchase member : values) {
            Item item_info = itemLab.getItem(member.getItemId());// get item object with item id
            priceCounter += item_info.getPrice() * member.getQty();
        }
        mCartTotalAmountTextView = (TextView) findViewById(R.id.cart_total_amount);
        mCartTotalAmountTextView.setText("Total: RM" + Integer.toString(priceCounter));

        mCheckoutBtn = (Button) findViewById(R.id.cart_checkout_button);
        mEmptyCartLayout = (RelativeLayout) findViewById(R.id.cart_empty_layout);
        mDeleteImageView = (ImageView) findViewById(R.id.cart_delete_image_view);
        if (values.size() == 0){ // if the cart is empty
            mEmptyCartLayout.setVisibility(View.VISIBLE);
            mDeleteImageView.setVisibility(View.GONE);
        } else {
            mEmptyCartLayout.setVisibility(View.GONE);
            mDeleteImageView.setVisibility(View.VISIBLE);
        }

        priceCounter = 0;
        //Toast.makeText(getApplicationContext(),values.toString(),Toast.LENGTH_SHORT).show();

        mAdapter = new ItemAdapter(values);
        listView.setAdapter(mAdapter);

        mDeleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CartActivity.this)
                        .setTitle("Confirmation")
                        .setMessage("Clear all items in your shopping cart?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                int result = datasource.deleteItemPurchase();
                                switch (result) {
                                    case 1:
                                        Toast.makeText(getApplicationContext(), "Clear successful", Toast.LENGTH_LONG).show();
                                        break;
                                    case 0:
                                        Toast.makeText(getApplicationContext(), "Failed to clear the cart", Toast.LENGTH_LONG).show();
                                        break;
                                }
                                // refresh the activity
                                finish();
                                startActivity(getIntent());
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        mCheckoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int total_price = 0;
                // get all cart item from database
                ArrayList<ItemPurchase> values = datasource.getAllCartItem();
                for (ItemPurchase member : values) {
                    Item item_info = itemLab.getItem(member.getItemId());// get item object with item id
                    total_price += item_info.getPrice() * member.getQty();
                }

                if (values.size() == 0){ // if the cart is empty
                    Toast.makeText(getApplicationContext(),"Please add something to the cart",Toast.LENGTH_LONG).show();
                } else {
                    new AlertDialog.Builder(CartActivity.this)
                            .setTitle("Checkout Confirmation")
                            .setMessage("Total payment amount: RM" + Integer.toString(total_price))
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    int result = datasource.checkoutItemPurchase();
                                    switch (result) {
                                        case 1:
                                            Toast.makeText(getApplicationContext(), "Checkout successful", Toast.LENGTH_LONG).show();
                                            break;
                                        case 0:
                                            Toast.makeText(getApplicationContext(), "Failed to checkout", Toast.LENGTH_LONG).show();
                                            break;
                                    }
                                    // refresh the activity
                                    finish();
                                    Intent intent = new Intent(getBaseContext(), PurchasedItemActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                }


            }
        });
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

    private class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener, NumberPicker.OnValueChangeListener {
        private TextView mNameTextView;
        private TextView mCatTextView;
        private TextView mPriceTextView;
        private ImageView mItemImageView;
        private NumberPicker mQtyNumberPicker;
        private ItemPurchase mItem;
        private String itemId;
        private long cartItemId;
        private String mImageName;
        private int mImageID;

        public ItemHolder(View itemView) {
            super(itemView);
            mNameTextView = (TextView) itemView.findViewById(R.id.cart_item_name);
            mCatTextView = (TextView) itemView.findViewById(R.id.cart_item_category);
            mPriceTextView = (TextView) itemView.findViewById(R.id.cart_item_price);
            mItemImageView = (ImageView) itemView.findViewById(R.id.cart_item_image);
            mQtyNumberPicker = (NumberPicker) itemView.findViewById(R.id.cart_quantity_np);
            mQtyNumberPicker.setMinValue(1);
            mQtyNumberPicker.setMaxValue(10);
            mQtyNumberPicker.setWrapSelectorWheel(true);
            itemView.setOnClickListener(this);
            mQtyNumberPicker.setOnValueChangedListener(this);
        }

        public void bindItem(ItemPurchase item) {
            mItem = item;

            cartItemId = mItem.getId();
            itemId = mItem.getItemId();
            Item item_info = itemLab.getItem(itemId);

            mNameTextView.setText(item_info.getName());
            mCatTextView.setText(item_info.getCategory());
            mPriceTextView.setText("RM" + String.valueOf(item_info.getPrice()));

            mImageName = item_info.getImageName();
            mImageID = getResources().getIdentifier(mImageName, "drawable", "com.inti.student.criminalintent");
            mItemImageView.setImageResource(mImageID);

            mQtyNumberPicker.setValue(item.getQty());
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), ItemDetailsActivity.class);
            intent.putExtra("itemId", itemId);
            startActivity(intent);
        }


        @Override
        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
            int result = datasource.updateItemPurchase(cartItemId, i1);
            switch (result) {
                case 1:
                    Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    Toast.makeText(getApplicationContext(), "Failed to save", Toast.LENGTH_LONG).show();
                    break;
            }

            // collect price and quantity of each item to calculate total price
            ArrayList<ItemPurchase> values = datasource.getAllCartItem();
            for (ItemPurchase member : values) {
                Item item_info = itemLab.getItem(member.getItemId()); // get item object with item id
                priceCounter += item_info.getPrice() * member.getQty();
            }
            // update value of total amount text view
            mCartTotalAmountTextView = (TextView) findViewById(R.id.cart_total_amount);
            mCartTotalAmountTextView.setText("Total: RM" + Integer.toString(priceCounter));
            priceCounter = 0;
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
