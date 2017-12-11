package com.s17983.msadowski.miniproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends SuperActivity {
    private static final String TAG = FirebaseViewActivity.class.getSimpleName();
    private List<Product> products;
    private FirebaseAdapter listAdapter;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private ListView listViewProducts;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeGUI();
        Button button3 = (Button) findViewById(R.id.firebase);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), FirebaseViewActivity.class);
                startActivity(intent);
            }
        });

        VIEW_ADDRESS = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);

        changeColorOnAllViews(VIEW_ADDRESS);
        changeTextSizeOnAllViews(VIEW_ADDRESS);


        mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("products");

        // store app title to 'app_title' node
        mFirebaseInstance.getReference("android-firebase-database").setValue("Realtime Database");

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
        // app_title change listener
        mFirebaseInstance.getReference("products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                products = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    Product product = noteDataSnapshot.getValue(Product.class);
                    products.add(product);
                    Log.e(TAG, product.getProductName());
                    fillListViewData(products);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read app title value.", error.toException());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        mFirebaseInstance.getReference("products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                products = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    Product product = noteDataSnapshot.getValue(Product.class);
                    products.add(product);
                    fillListViewData(products);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read app title value.", error.toException());
            }
        });

    }

    //TODO Edit
    
    private void fillListViewData(List<Product> products) {
        listAdapter = new FirebaseAdapter(this, products, mFirebaseDatabase);
        listViewProducts.setAdapter(listAdapter);
    }

    private void initializeGUI() {
        listViewProducts = (ListView) findViewById(R.id.lvProducts);
    }

    public void refreshMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
