package com.s17983.msadowski.miniproject;

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
public class Product {
    private String productId;
    private String productName;
    private float productPrice;
    private int productQuantity;
    private boolean isBought;
}
