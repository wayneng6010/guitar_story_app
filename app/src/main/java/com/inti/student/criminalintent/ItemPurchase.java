package com.inti.student.criminalintent;

import java.util.UUID;

public class ItemPurchase {
    private long mId;
    private String mItemId;
    private int mQty;
    private String mStatus;
    private String mPurchaseDate;

    public long getId() { return mId; }

    public void setId(long mId) { this.mId = mId; }

    public String getItemId() { return mItemId; }

    public void setItemId(String mItemId) { this.mItemId = mItemId; }

    public int getQty() { return mQty; }

    public void setQty(int mQty) { this.mQty = mQty; }

    public String getStatus() { return mStatus; }

    public void setStatus(String mStatus) { this.mStatus = mStatus; }

    public String getPurchaseDate() { return mPurchaseDate; }

    public void setPurchaseDate(String mPurchaseDate) { this.mPurchaseDate = mPurchaseDate; }

    @Override
    public String toString(){
        return Integer.toString(mQty);
    }

}
