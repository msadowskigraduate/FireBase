package com.s17983.msadowski.miniproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends SuperActivity {
    private Button btnAddNew;
    private Button btnClearCompleted;
    private Button btnSave;
    private Button btnCancel;
    private EditText etNewProduct;
    private EditText etNewProductPrice;
    private EditText etNewProductQuantity;
    private CheckBox cbNewProductBought;

    private ListView listViewProducts;
    private LinearLayout llControlButtons;
    private LinearLayout llNewTaskButtons;

    private ProductDatabase productDatabase;
    private Cursor productCursor;
    private List<Product> products;
    private ProductListAdapter listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        initUiElements();
        initListView();
        initButtonsOnClickListeners();
        VIEW_ADDRESS = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        super.changeColorOnAllViews(VIEW_ADDRESS);
        super.changeTextSizeOnAllViews(VIEW_ADDRESS);
    }

    private void initUiElements() {
        btnAddNew = (Button) findViewById(R.id.btnAddNew);
        btnClearCompleted = (Button) findViewById(R.id.btnClearCompleted);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        etNewProduct = (EditText) findViewById(R.id.etNewProductName);
        listViewProducts = (ListView) findViewById(R.id.lvProducts);
        llControlButtons = (LinearLayout) findViewById(R.id.llControlButtons);
        llNewTaskButtons = (LinearLayout) findViewById(R.id.llNewProductButtons);
        etNewProductPrice = (EditText) findViewById(R.id.etNewProductPrice);
        etNewProductQuantity = (EditText) findViewById(R.id.etNewProductQuantity);
        cbNewProductBought = (CheckBox) findViewById(R.id.cbNewProductBought);
    }

    private void initListView() {
        fillListViewData();
        initListViewOnItemClick();
    }

    private void fillListViewData() {
        productDatabase = new ProductDatabase(getApplicationContext());
        productDatabase.open();
        getAllTasks();
        listAdapter = new ProductListAdapter(this, products, productDatabase);
        listViewProducts.setAdapter(listAdapter);
    }

    private void getAllTasks() {
        products = new ArrayList<Product>();
        productCursor = getAllEntriesFromDb();
        updateTaskList();
    }

    private Cursor getAllEntriesFromDb() {
        productCursor = productDatabase.getAllProducts();
        if(productCursor != null) {
            startManagingCursor(productCursor);
            productCursor.moveToFirst();
        }
        return productCursor;
    }

    private void updateTaskList() {
        if(productCursor != null && productCursor.moveToFirst()) {
            do {
                long id = productCursor.getLong(ProductDatabase.PRODUCT_ID_COLUMN);
                String name = productCursor.getString(ProductDatabase.PRODUCT_NAME_COLUMN);
                float price = productCursor.getFloat(ProductDatabase.PRODUCT_PRICE_COLUMN);
                int quantity = productCursor.getInt(ProductDatabase.PRODUCT_QUANTITY_COLUMN);
                boolean bought = productCursor.getInt(ProductDatabase.PRODUCT_ISBOUGHT_COLUMN) > 0 ? true : false;
                products.add(new Product(id, name, price, quantity, bought));
            } while(productCursor.moveToNext());
        }
    }

    @Override
    protected void onDestroy() {
        if(productDatabase != null)
            productDatabase.close();
        super.onDestroy();
    }

    private void initListViewOnItemClick() {
//        listViewProducts.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.e("BUTTON PRESSED", String.valueOf(position));
//                Product product = products.get(position);
//                productDatabase.deleteProduct(product.getProductId());
//                updateListViewData();
//            }
//        });
    }

    private void updateListViewData() {
        productCursor.requery();
        products.clear();
        updateTaskList();
        listAdapter.notifyDataSetChanged();
    }

    private void initButtonsOnClickListeners() {
        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnAddNew:
                        addNewTask();
                        break;
                    case R.id.btnSave:
                        saveNewTask();
                        break;
                    case R.id.btnCancel:
                        cancelNewTask();
                        break;
                    case R.id.btnClearCompleted:
                        returnToMainActivity();
                        //deleteMarkedProducts();
                        break;
                    default:
                        break;
                }
            }
        };
        btnAddNew.setOnClickListener(onClickListener);
        btnClearCompleted.setOnClickListener(onClickListener);
        btnSave.setOnClickListener(onClickListener);
        btnCancel.setOnClickListener(onClickListener);
    }

    private void showOnlyNewTaskPanel() {
        setVisibilityOf(llControlButtons, false);
        setVisibilityOf(llNewTaskButtons, true);
        setVisibilityOf(etNewProduct, true);
        setVisibilityOf(etNewProductPrice, true);
        setVisibilityOf(etNewProductQuantity, true);
        setVisibilityOf(cbNewProductBought, true);

    }

    private void showOnlyControlPanel() {
        setVisibilityOf(llControlButtons, true);
        setVisibilityOf(llNewTaskButtons, false);
        setVisibilityOf(etNewProduct, false);
        setVisibilityOf(etNewProductPrice, false);
        setVisibilityOf(etNewProductQuantity, false);
        setVisibilityOf(cbNewProductBought, false);
    }

    private void setVisibilityOf(View v, boolean visible) {
        int visibility = visible ? View.VISIBLE : View.GONE;
        v.setVisibility(visibility);
    }

    private void addNewTask(){
        showOnlyNewTaskPanel();
    }

    private void saveNewTask(){
        int productQuantity = 0;
        float productPrice = 0.f;
        boolean isBought = false;

        String productName = etNewProduct.getText().toString();
        String productPriceStr = etNewProductPrice.getText().toString();
        productPriceStr = productPriceStr.replace(',', '.');
        productPrice = Float.valueOf(productPriceStr);

        try {
            productQuantity = Integer.valueOf(etNewProductQuantity.getText().toString());
        } catch (NumberFormatException nfExp)
        {
            etNewProductQuantity.setError(nfExp.getMessage().toString());
        }
        isBought = cbNewProductBought.isChecked();
        if(productName.equals("") || productQuantity == 0 || productPrice == 0f) {
            etNewProduct.setError("There are some errors in your product definition");
        } else {
            productDatabase.insertProduct(productName, productPrice, productQuantity, isBought);
            etNewProduct.setText("");
            showOnlyControlPanel();
        }
        updateListViewData();
    }

    private void cancelNewTask() {
        etNewProduct.setText("");
        showOnlyControlPanel();
    }

    public void returnToMainActivity() {
        Intent intent = new Intent(this , MainActivity.class);
        startActivity(intent);
    }
}

