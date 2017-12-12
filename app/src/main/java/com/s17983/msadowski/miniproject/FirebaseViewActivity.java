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

        initGUI();

        Intent i = getIntent();
        if(i.getExtras() != null && i.getExtras().getParcelable("product")!= null) {
            setButtonListener(false);
            Product product = i.getExtras().getParcelable("product");
            try {
                productId = product.getProductId();
                setValues(product.getProductName(), String.valueOf(product.getProductPrice()), String.valueOf(product.getProductQuantity()), String.valueOf(product.isBought()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            setButtonListener(true);
        }
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        mFirebaseInstance = FirebaseDatabase.getInstance();

        mFirebaseDatabase = mFirebaseInstance.getReference("products");

        mFirebaseInstance.getReference("android-firebase-database").setValue("Realtime Database");

        mFirebaseInstance.getReference("android-firebase-database").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "App title updated");
                String appTitle = dataSnapshot.getValue(String.class);
                getSupportActionBar().setTitle(appTitle);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Failed to read app title value.", error.toException());
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

    private void addUserChangeListener() {
        mFirebaseDatabase.child(String.valueOf(productId)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Product product = dataSnapshot.getValue(Product.class);
                if (product == null) {
                    Log.e(TAG, "User data is null!");
                    return;
                }
                Log.e(TAG, "User data is changed!" + product.getProductName());
                fieldReset();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }

    private void updateProduct(String name, float price, int quantity, boolean isbought) {
        mFirebaseDatabase.child(String.valueOf(productId)).child("productName").setValue(name);
        mFirebaseDatabase.child(String.valueOf(productId)).child("productPrice").setValue(price);
        mFirebaseDatabase.child(String.valueOf(productId)).child("productQuantity").setValue(quantity);
        mFirebaseDatabase.child(String.valueOf(productId)).child("bought").setValue(isbought);
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
        btnSave.setText("Save");
    }

    public void returnToMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void setValues(String name, String price, String quantity, String isBought) {
        etName.setText(name);
        etPrice.setText(price);
        etQuantity.setText(quantity);
        cbBought.setChecked(Boolean.parseBoolean(isBought));
    }

    private void setButtonListener(final boolean state) {
        if(!state)
            btnSave.setText("Edit");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productName = "";
                int productQuantity = 1;
                float productPrice = 0.f;
                boolean isBought = false;
                try {
                    productName = etName.getText().toString();
                    productPrice = Float.valueOf(etPrice.getText().toString());
                    productQuantity = Integer.valueOf(etQuantity.getText().toString());
                    isBought = cbBought.isChecked();
                    if(state) {
                        createProduct(productName, productPrice, productQuantity, isBought);
                    }
                    else {
                        updateProduct(productName, productPrice, productQuantity, isBought);
                    }
                    fieldReset();
                } catch (Exception e) {
                    etName.setError("There are errors in your product definition.");
                }

            }
        });
    }
}
