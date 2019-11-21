package com.inti.student.criminalintent;

import java.util.UUID;

public class Item {

    private UUID mId;
    private String mName;
    private String mCategory;
    private int mPrice;
    private String mDescription;
    private String mImageName;

    public Item(String name, String category, int price, String description, String imageName) {
        mId = UUID.randomUUID(); // generate unique identifier
        mName = name;
        mCategory = category;
        mPrice = price;
        mDescription = description;
        mImageName = imageName;
    }

    public UUID getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getCategory() { return mCategory; }

    public void setCategory(String category) { mCategory = category; }

    public int getPrice() { return mPrice; }

    public void setPrice(int price) { mPrice = price; }

    public String getDescription() { return mDescription; }

    public void setDescription(String description) { mDescription = description; }

    public String getImageName() { return mImageName; }

    public void setImageName(String imageName) { mImageName = imageName; }
}
