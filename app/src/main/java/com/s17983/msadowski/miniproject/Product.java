package com.s17983.msadowski.miniproject;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by esadowska on 18/11/2017.
 */
@IgnoreExtraProperties
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product implements Parcelable{
    private String productId;
    private String productName;
    private float productPrice;
    private int productQuantity;
    private boolean isBought;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(productId);
        parcel.writeString(productName);
        parcel.writeFloat(productPrice);
        parcel.writeInt(productQuantity);
        parcel.writeString(String.valueOf(isBought));
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public Product(Parcel in) {
        productId = in.readString();
        productName = in.readString();
        productPrice = in.readFloat();
        productQuantity = in.readInt();
        isBought = Boolean.getBoolean(in.readString());
    }
}
