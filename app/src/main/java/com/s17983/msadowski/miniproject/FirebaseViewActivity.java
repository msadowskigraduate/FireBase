package com.s17983.msadowski.miniproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseViewActivity extends SuperActivity {
    private static final String TAG = FirebaseViewActivity.class.getSimpleName();
    ArrayList<Product> products;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    private String productId;

    private Button btnSave;
    private Button btnReturn;
    private EditText etName;
    private EditText etPrice;
    private EditText etQuantity;
    private CheckBox cbBought;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_view);
        //Initialize GUI
        initGUI();
        // Displaying toolbar icon
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("products");

        // store app title to 'app_title' node
        mFirebaseInstance.getReference("android-firebase-database").setValue("Realtime Database");

        // app_title change listener
        mFirebaseInstance.getReference("android-firebase-database").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "App title updated");

                String appTitle = dataSnapshot.getValue(String.class);

                // update toolbar title
                getSupportActionBar().setTitle(appTitle);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read app title value.", error.toException());
            }
        });

        // Save / update the user
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int productQuantity = 1;
                float productPrice = 0.f;
                boolean isBought = false;

                String productName = etName.getText().toString();
                String productPriceStr = etPrice.getText().toString();
                productPriceStr = productPriceStr.replace(',', '.');
                if (TextUtils.isEmpty(productPriceStr)) {
                    productPriceStr = "0";
                }
                productPrice = Float.valueOf(productPriceStr);
                if (TextUtils.isEmpty(productId)) {

                    try {
                        productQuantity = Integer.valueOf(etQuantity.getText().toString());
                    } catch (NumberFormatException nfExp) {
                        etQuantity.setError(nfExp.getMessage().toString());
                    }
                    isBought = cbBought.isChecked();
                    if (productName.equals("") || productQuantity == 0 || productPrice == 0f) {
                        etName.setError("There are some errors in your product definition");
                    } else {
                        createProduct(productName, productPrice, productQuantity, isBought);
                        fieldReset();
                    }
                } else {
                    updateProduct(productName, productPrice, productQuantity, isBought);
                }
            }
        });
    }

    /**
     * Creating new user node under 'users'
     */
    public void createProduct(String productName, float productPrice,
                              int productQuantity, boolean isBought) {


        productId = mFirebaseDatabase.push().getKey();


        Product product = Product.builder()
                .productId(productId)
                .productName(productName)
                .productPrice(productPrice)
                .productQuantity(productQuantity)
                .isBought(isBought)
                .build();

        mFirebaseDatabase.child(String.valueOf(productId)).setValue(product);

        addUserChangeListener();
    }

    /**
     * User data change listener
     */
    private void addUserChangeListener() {
        // User data change listener
        mFirebaseDatabase.child(String.valueOf(productId)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Product product = dataSnapshot.getValue(Product.class);

                // Check for null
                if (product == null) {
                    Log.e(TAG, "User data is null!");
                    return;
                }

                Log.e(TAG, "User data is changed!" + product.getProductName());

                fieldReset();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }

    private void updateProduct(String name, float price, int quantity, boolean isbought) {
        // updating the user via child nodes
        mFirebaseDatabase.child(String.valueOf(productId)).child("productName").setValue(name);
        mFirebaseDatabase.child(String.valueOf(productId)).child("productPrice").setValue(price);
        mFirebaseDatabase.child(String.valueOf(productId)).child("productQuantity").setValue(quantity);
        mFirebaseDatabase.child(String.valueOf(productId)).child("isBought").setValue(isbought);
    }

    private void initGUI() {
        btnSave = (Button) findViewById(R.id.btnFirebaseSave);
        btnReturn = (Button) findViewById(R.id.btnReturn);
        etName = (EditText) findViewById(R.id.name);
        etPrice = (EditText) findViewById(R.id.price);
        etQuantity = (EditText) findViewById(R.id.quantity);
        cbBought = (CheckBox) findViewById(R.id.cbProductBought);
    }

    private void fieldReset() {
        etName.setText("");
        etPrice.setText("");
        etQuantity.setText("");
        cbBought.setChecked(false);
    }

    public void returnToMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
