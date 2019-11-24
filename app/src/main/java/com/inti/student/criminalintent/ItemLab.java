package com.inti.student.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemLab {
    private static ItemLab sItemLab;
    private List<Item> mItems;

    public static ItemLab get(Context context) {
        if (sItemLab == null) {
            sItemLab = new ItemLab(context);
        }
        return sItemLab;
    }

    private ItemLab(Context context) {
        mItems = new ArrayList<>();

        // acoustic guitar
        mItems.add(new Item("AG01","ZTA 38 Inch Acoustic Guitar", "Acoustic Guitar",355,"Description 1", "acoustic_1"));
        mItems.add(new Item("AG02","Fender FA-100 Dreadnought Acoustic Guitar", "Acoustic Guitar",499,"Description 2", "acoustic_2"));
        mItems.add(new Item("AG03","LX1E Little Martin Travel Guitar", "Acoustic Guitar",299,"Description 3", "acoustic_3"));
        mItems.add(new Item("AG04","38\" Black Acoustic Guitar Starter Package", "Acoustic Guitar",999,"Description 4", "acoustic_4"));
        mItems.add(new Item("AG05","Jasmine S35 Acoustic Guitar", "Acoustic Guitar",1055,"Description 5", "acoustic_5"));
        mItems.add(new Item("AG06","Epiphone DR-100 Acoustic Guitar", "Acoustic Guitar",655,"Description 6", "acoustic_6"));
        mItems.add(new Item("AG07","Seagull S6 \"The Original\" Acoustic Guitar", "Acoustic Guitar",899,"Description 7", "acoustic_7"));
        mItems.add(new Item("AG08","Taylor 224ce Deluxe Koa Grand Auditorium", "Acoustic Guitar",689,"Description 8", "acoustic_8"));
        mItems.add(new Item("AG09","Martin Road Series DRS1 Dreadnought Acoustic", "Acoustic Guitar",1239,"Description 9", "acoustic_9"));
        mItems.add(new Item("AG10","Fender Sonoran SCE Dreadnought Cutaway", "Acoustic Guitar",1395,"Description 10", "acoustic_10"));

        // electric guitar
        mItems.add(new Item("EG01","Fender Telecaster", "Electric Guitar",785,"Description 1", "electric_1"));
        mItems.add(new Item("EG02","Gibson Les Paul Studio", "Electric Guitar",1355,"Description 2", "electric_2"));
        mItems.add(new Item("EG03","Ibanez Roadcore RC365H", "Electric Guitar",1499,"Description 3", "electric_3"));
        mItems.add(new Item("EG04","Squier Bullet Stratocaster SSS", "Electric Guitar",799,"Description 4", "electric_4"));
        mItems.add(new Item("EG05","Fender Stratocaster American Standard", "Electric Guitar",890,"Description 5", "electric_5"));
        mItems.add(new Item("EG06","Gretsch G5422TDC Electromatic", "Electric Guitar",1399,"Description 6", "electric_6"));
        mItems.add(new Item("EG07","ESP LTD EC-1000", "Electric Guitar",1589,"Description 7", "electric_7"));
        mItems.add(new Item("EG08","Epiphone Les Paul Special II", "Electric Guitar",2100,"Description 8", "electric_8"));
        mItems.add(new Item("EG09","D’Angelico EX-DC Standard", "Electric Guitar",1799,"Description 9", "electric_9"));
        mItems.add(new Item("EG10","Ibanez Artcore AF75", "Electric Guitar",2195,"Description 10", "electric_10"));
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
