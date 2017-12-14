package com.s17983.msadowski.miniproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = FirebaseViewActivity.class.getSimpleName();
    private List<Product> products;

    @Inject
    public FirebaseAdapter listAdapter;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private ListView listViewProducts;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        initializeGUI();

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

        getDataFromDB();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataFromDB();
    }

    private void getDataFromDB() {
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
                Log.e(TAG, "Failed to read app title value.", error.toException());
            }
        });
    }
    private void fillListViewData(List<Product> products) {
        listAdapter = new FirebaseAdapter(this, products, mFirebaseDatabase);
        listViewProducts.setAdapter(listAdapter);
    }

    private void initializeGUI() {
        listViewProducts = (ListView) findViewById(R.id.lvProducts);
        //Button button3 = (Button) findViewById(R.id.firebase);
        FloatingActionButton button3 = (FloatingActionButton) findViewById(R.id.btnAddNew);
        initListViewOnItemClick();
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), FirebaseViewActivity.class);
                startActivity(intent);
            }
        });
    }

    public void refreshMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void initListViewOnItemClick() {
        listViewProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position,
                                    long id) {
                Product product = products.get(position);
                Intent intent = new Intent(MainActivity.this, FirebaseViewActivity.class);
                intent.putExtra("product", product);
                startActivity(intent);
            }
        });
    }
}
