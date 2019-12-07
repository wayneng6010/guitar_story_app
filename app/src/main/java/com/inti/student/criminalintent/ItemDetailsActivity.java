package com.inti.student.criminalintent;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
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

import com.inti.student.criminalintent.Config.Config;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class ItemDetailsActivity extends AppCompatActivity {

    private ItemPurchaseDataSource datasource;

    private int mItemPrice;
    private String mItemId = "";
    private int mItemQty;

    // for PayPal payment
    private static final int PAYPAL_REQUEST_CODE = 7171;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // hide default title bar
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        setContentView(R.layout.item_details);

        // start PayPal service
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        datasource = new ItemPurchaseDataSource(this);
        datasource.open();

        ImageView mImageView = (ImageView) findViewById(R.id.details_item_image);
        TextView mPriceView = (TextView) findViewById(R.id.details_item_price);
        TextView mItemNameView = (TextView) findViewById(R.id.details_item_name);
        TextView mCategoryView = (TextView) findViewById(R.id.details_item_cat);
        TextView mDescriptionView = (TextView) findViewById(R.id.details_item_description);

        mItemId = getIntent().getStringExtra("itemId");

        ItemLab itemLab = ItemLab.get(this);
        Item item = itemLab.getItem(mItemId);

        String mImageName = item.getImageName();
        // retrieve image from local repository
        int mImageID = getResources().getIdentifier(mImageName, "drawable",
                "com.inti.student.criminalintent");
        mImageView.setImageResource(mImageID);

        mPriceView.setText("RM" + item.getPrice());
        mItemPrice = item.getPrice();
        mItemNameView.setText(item.getName());
        mCategoryView.setText(item.getCategory());
        mDescriptionView.setText(item.getDescription());

        Button mAddToCartBtn = (Button) findViewById(R.id.add_to_cart_btn);
        mAddToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("LoginSession", MODE_PRIVATE);
                boolean isLogin = prefs.getBoolean("login", false); // retrieve login session saved in preference

                if (isLogin){
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

                                    int itemQty = itemQtyNp.getValue();
                                    int result = datasource.createItemPurchase(mItemId, itemQty);

                                    switch (result){
                                        case 1:
                                            Toast.makeText(getApplicationContext(),"Added to Cart",Toast.LENGTH_SHORT).show();
                                            break;
                                        case 0:
                                            Toast.makeText(getApplicationContext(),"Error occurred, please try again",Toast.LENGTH_LONG).show();
                                            break;
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .create();
                    dialog.show();
                } else {
                    Toast.makeText(getApplicationContext(),"Please login first",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ItemDetailsActivity.this, UserLoginActivity.class));
                }

            }
        });

        Button mBuyNowBtn = (Button) findViewById(R.id.buy_now_btn);
        mBuyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("LoginSession", MODE_PRIVATE);
                boolean isLogin = prefs.getBoolean("login", false); // retrieve login session saved in preference

                if (isLogin){
                    final NumberPicker itemQtyNp = new NumberPicker(ItemDetailsActivity.this);
                    itemQtyNp.setMinValue(1);
                    itemQtyNp.setMaxValue(10);
                    itemQtyNp.setWrapSelectorWheel(true);

                    AlertDialog dialog = new AlertDialog.Builder(ItemDetailsActivity.this)
                            .setTitle("Buy Now")
                            .setMessage("Quantity")
                            .setView(itemQtyNp)
                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    final int itemQty = itemQtyNp.getValue();
                                    mItemQty = itemQtyNp.getValue();

                                    new android.support.v7.app.AlertDialog.Builder(ItemDetailsActivity.this)
                                            .setTitle("Checkout Confirmation")
                                            .setMessage("Total payment amount: RM" + (mItemPrice * itemQty))
                                            .setIcon(android.R.drawable.ic_dialog_info)
                                            .setPositiveButton("Confirm Payment", new DialogInterface.OnClickListener() {

                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                    processPayment(mItemPrice * itemQty);
                                                }
                                            })
                                            .setNegativeButton("Cancel", null).show();

                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .create();
                    dialog.show();
                } else {
                    Toast.makeText(getApplicationContext(),"Please login first",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ItemDetailsActivity.this, UserLoginActivity.class));
                }

            }
        });

        ImageView mCartImageView = (ImageView) findViewById(R.id.details_cart);
        mCartImageView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getSharedPreferences("LoginSession", MODE_PRIVATE);
                boolean isLogin = prefs.getBoolean("login", false); // retrieve login session saved in preference

                if (isLogin) {
                    startActivity(new Intent(ItemDetailsActivity.this, CartActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(),"Please login first",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ItemDetailsActivity.this, UserLoginActivity.class));
                }

            }
        });


    }

    public void processPayment(int final_price){
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(final_price)), "MYR",
                "Payment for Guitar Story", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PAYPAL_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null){
                    datasource.open();
                    int result = datasource.checkoutOneItem(mItemId, mItemQty);
                    switch (result) {
                        case 1:
                            new android.support.v7.app.AlertDialog.Builder(ItemDetailsActivity.this)
                                    .setTitle("Message")
                                    .setMessage("Checkout successful")
                                    .setIcon(android.R.drawable.ic_dialog_info)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            // clear activity
                                            finish();
                                            Intent intent = new Intent(getBaseContext(), PurchasedItemActivity.class);
                                            startActivity(intent);
                                        }
                                    }).show();
                            break;
                        case 0:
                            Toast.makeText(getApplicationContext(), "Failed to checkout", Toast.LENGTH_LONG).show();
                            break;
                    }

                }
            }
        }
    }
}
