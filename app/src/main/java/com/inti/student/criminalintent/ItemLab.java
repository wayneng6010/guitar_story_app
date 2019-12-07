package com.inti.student.criminalintent;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ItemLab {
    private static ItemLab sItemLab;
    private List<Item> mItems;
    private static Context mContext;
    DatabaseReference reff;

    public static ItemLab get(Context context) {
        if (sItemLab == null) {
            sItemLab = new ItemLab(context);
        }
        mContext = context;
        return sItemLab;
    }

    private ItemLab(Context context) {
        reff = FirebaseDatabase.getInstance().getReference().child("item");
        mItems = new ArrayList<>();

        //mItems.add(new Item("AG1", "ZTA 38 Inch Acoustic Guitar", "Acoustic Guitar", 355, "Vintage tone and style to spare with this high-end Takamine.", "acoustic_1"));

//        Map<String, Item> items = new HashMap<>();
//        items.put("1", new Item("AG01", "ZTA 38 Inch Acoustic Guitar", "Acoustic Guitar", 355, "Vintage tone and style to spare with this high-end Takamine.", "acoustic_1"));
//        items.put("2", new Item("AG02", "Fender FA-100 Dreadnought Acoustic Guitar", "Acoustic Guitar", 499, "A gorgeous premium all-solid-wood Taylor.", "acoustic_2"));
//        items.put("3", new Item("AG03", "LX1E Little Martin Travel Guitar", "Acoustic Guitar", 299, "Great looking dreadnaught body guitar from Bristol.", "acoustic_3"));
//        items.put("4", new Item("AG04", "38\" Black Acoustic Guitar Starter Package", "Acoustic Guitar", 999, "Classic style with this solid-wood Martin.", "acoustic_4"));
//        items.put("5", new Item("AG05", "Jasmine S35 Acoustic Guitar", "Acoustic Guitar", 1055, "A mid-range performance-focused acoustic with a high-end feel.", "acoustic_5"));
//        items.put("6", new Item("AG06", "Epiphone DR-100 Acoustic Guitar", "Acoustic Guitar", 655, "Beautiful style, quality and playability with this Seagull.", "acoustic_6"));
//        items.put("7", new Item("AG07", "Seagull S6 \"The Original\" Acoustic Guitar", "Acoustic Guitar", 899, "A taste of vintage America with this stylish Chinese guitar.", "acoustic_7"));
//        items.put("8", new Item("AG08", "Taylor 224ce Deluxe Koa Grand Auditorium", "Acoustic Guitar", 689, "Affordable slimline Yamaha with upgraded performance.", "acoustic_8"));
//        items.put("9", new Item("AG09", "Martin Road Series DRS1 Dreadnought Acoustic", "Acoustic Guitar", 1239, "A proud representative from the Takamine family.", "acoustic_9"));
//        items.put("10", new Item("AG10", "Fender Sonoran SCE Dreadnought Cutaway", "Acoustic Guitar", 1395, "FG800 shows what made Yamaha's FG series so legendary to begin with.", "acoustic_10"));
//
//        // electric guitar
//        items.put("11", new Item("EG01", "Fender Telecaster", "Electric Guitar", 785, "Take a trip back in time with this ‘70s-influenced Strat.", "electric_1"));
//        items.put("12", new Item("EG02", "Gibson Les Paul Studio", "Electric Guitar", 1355, "An affordable eye-catching shred machine.", "electric_2"));
//        items.put("13", new Item("EG03", "Ibanez Roadcore RC365H", "Electric Guitar", 1499, "Lightning fast neck means shredding on a budget is possible!", "electric_3"));
//        items.put("14", new Item("EG04", "Squier Bullet Stratocaster SSS", "Electric Guitar", 799, "Seriously good value from a better-than-basic Strat.", "electric_4"));
//        items.put("15", new Item("EG05", "Fender Stratocaster American Standard", "Electric Guitar", 890, "An affordable Tele with a bit of bite.", "electric_5"));
//        items.put("16", new Item("EG06", "Gretsch G5422TDC Electromatic", "Electric Guitar", 1399, "A versatile all-rounder from a respected series.", "electric_6"));
//        items.put("17", new Item("EG07", "ESP LTD EC-1000", "Electric Guitar", 1589, "Striking style from an affordable metal machine!", "electric_7"));
//        items.put("18", new Item("EG08", "Epiphone Les Paul Special II", "Electric Guitar", 2100, "Huge quality in design and tone from this premium RG.", "electric_8"));
//        items.put("19", new Item("EG09", "D’Angelico EX-DC Standard", "Electric Guitar", 1799, "One of the most versatile high-end Les Pauls ever made.", "electric_9"));
//        items.put("20", new Item("EG10", "Ibanez Artcore AF75", "Electric Guitar", 2195, "A premium guitar delivering the true EVH experience.", "electric_10"));
//
//        reff.setValue(items);

        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String id = childSnapshot.child("id").getValue().toString();
                        String name = childSnapshot.child("name").getValue().toString();
                        String category = childSnapshot.child("category").getValue().toString();
                        int price = Integer.parseInt(childSnapshot.child("price").getValue().toString());
                        String description = childSnapshot.child("description").getValue().toString();
                        String imageName = childSnapshot.child("imageName").getValue().toString();

                        // add Item object with retrieved product information
                        mItems.add(new Item(id, name, category, price, description, imageName));
                    }
                } else{
                    Toast.makeText(mContext,"No item found",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        // acoustic guitar
//        mItems.add(new Item("AG01", "ZTA 38 Inch Acoustic Guitar", "Acoustic Guitar", 355, "Vintage tone and style to spare with this high-end Takamine.", "acoustic_1"));
//        mItems.add(new Item("AG02", "Fender FA-100 Dreadnought Acoustic Guitar", "Acoustic Guitar", 499, "A gorgeous premium all-solid-wood Taylor.", "acoustic_2"));
//        mItems.add(new Item("AG03", "LX1E Little Martin Travel Guitar", "Acoustic Guitar", 299, "Great looking dreadnaught body guitar from Bristol.", "acoustic_3"));
//        mItems.add(new Item("AG04", "38\" Black Acoustic Guitar Starter Package", "Acoustic Guitar", 999, "Classic style with this solid-wood Martin.", "acoustic_4"));
//        mItems.add(new Item("AG05", "Jasmine S35 Acoustic Guitar", "Acoustic Guitar", 1055, "A mid-range performance-focused acoustic with a high-end feel.", "acoustic_5"));
//        mItems.add(new Item("AG06", "Epiphone DR-100 Acoustic Guitar", "Acoustic Guitar", 655, "Beautiful style, quality and playability with this Seagull.", "acoustic_6"));
//        mItems.add(new Item("AG07", "Seagull S6 \"The Original\" Acoustic Guitar", "Acoustic Guitar", 899, "A taste of vintage America with this stylish Chinese guitar.", "acoustic_7"));
//        mItems.add(new Item("AG08", "Taylor 224ce Deluxe Koa Grand Auditorium", "Acoustic Guitar", 689, "Affordable slimline Yamaha with upgraded performance.", "acoustic_8"));
//        mItems.add(new Item("AG09", "Martin Road Series DRS1 Dreadnought Acoustic", "Acoustic Guitar", 1239, "A proud representative from the Takamine family.", "acoustic_9"));
//        mItems.add(new Item("AG10", "Fender Sonoran SCE Dreadnought Cutaway", "Acoustic Guitar", 1395, "FG800 shows what made Yamaha's FG series so legendary to begin with.", "acoustic_10"));
//
//        // electric guitar
//        mItems.add(new Item("EG01", "Fender Telecaster", "Electric Guitar", 785, "Take a trip back in time with this ‘70s-influenced Strat.", "electric_1"));
//        mItems.add(new Item("EG02", "Gibson Les Paul Studio", "Electric Guitar", 1355, "An affordable eye-catching shred machine.", "electric_2"));
//        mItems.add(new Item("EG03", "Ibanez Roadcore RC365H", "Electric Guitar", 1499, "Lightning fast neck means shredding on a budget is possible!", "electric_3"));
//        mItems.add(new Item("EG04", "Squier Bullet Stratocaster SSS", "Electric Guitar", 799, "Seriously good value from a better-than-basic Strat.", "electric_4"));
//        mItems.add(new Item("EG05", "Fender Stratocaster American Standard", "Electric Guitar", 890, "An affordable Tele with a bit of bite.", "electric_5"));
//        mItems.add(new Item("EG06", "Gretsch G5422TDC Electromatic", "Electric Guitar", 1399, "A versatile all-rounder from a respected series.", "electric_6"));
//        mItems.add(new Item("EG07", "ESP LTD EC-1000", "Electric Guitar", 1589, "Striking style from an affordable metal machine!", "electric_7"));
//        mItems.add(new Item("EG08", "Epiphone Les Paul Special II", "Electric Guitar", 2100, "Huge quality in design and tone from this premium RG.", "electric_8"));
//        mItems.add(new Item("EG09", "D’Angelico EX-DC Standard", "Electric Guitar", 1799, "One of the most versatile high-end Les Pauls ever made.", "electric_9"));
//        mItems.add(new Item("EG10", "Ibanez Artcore AF75", "Electric Guitar", 2195, "A premium guitar delivering the true EVH experience.", "electric_10"));
    }

    public List<Item> getItems() {
        return mItems;
    }

    public Item getItem(String id) {
        for (Item item : mItems) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }
}
