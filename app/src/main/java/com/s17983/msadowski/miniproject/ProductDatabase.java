package com.s17983.msadowski.miniproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by esadowska on 12/11/2017.
 */
public class ProductDatabase {
    private static final String DEBUG_TAG = "SqLiteTodoManager";

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "database.db";
    private static final String DB_TODO_TABLE = "PRODUCTLIST";

    public static final String PRODUCT_ID = "_id";
    public static final String PRODUCT_ID_OPTIONS = "INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final int PRODUCT_ID_COLUMN = 0;
    public static final String PRODUCT_NAME = "product_name";
    public static final String PRODUCT_NAME_OPTIONS = "TEXT NOT NULL";
    public static final int PRODUCT_NAME_COLUMN = 1;
    public static final String PRODUCT_PRICE = "product_price";
    public static final String PRODUCT_PRICE_OPTIONS = "LONG DEFAULT 0";
    public static final int PRODUCT_PRICE_COLUMN = 2;
    public static final String PRODUCT_QUANTITY = "product_quantity";
    public static final String PRODUCT_QUANTITY_OPTIONS = "INTEGER DEFAULT 0";
    public static final int PRODUCT_QUANTITY_COLUMN = 3;
    public static final String PRODUCT_ISBOUGHT = "is_bought";
    public static final String PRODUCT_ISBOUGHT_OPTIONS = "INTEGER DEFAULT 0";
    public static final int PRODUCT_ISBOUGHT_COLUMN = 4;

    private static final String DB_CREATE_TODO_TABLE =
            "CREATE TABLE " + DB_TODO_TABLE + "( " +
                    PRODUCT_ID + " " + PRODUCT_ID_OPTIONS + ", " +
                    PRODUCT_NAME + " " + PRODUCT_NAME_OPTIONS + ", " +
                    PRODUCT_PRICE + " " + PRODUCT_PRICE_OPTIONS + ", " +
                    PRODUCT_QUANTITY + " " + PRODUCT_QUANTITY_OPTIONS + ", " +
                    PRODUCT_ISBOUGHT + " " + PRODUCT_ISBOUGHT_OPTIONS +
                    ");";
    private static final String DROP_TODO_TABLE =
            "DROP TABLE IF EXISTS " + DB_TODO_TABLE;

    private SQLiteDatabase db;
    private Context context;
    private DatabaseHelper dbHelper;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE_TODO_TABLE);

            Log.d(DEBUG_TAG, "Database creating...");
            Log.d(DEBUG_TAG, "Table " + DB_TODO_TABLE + " ver." + DB_VERSION + " created");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_TODO_TABLE);

            Log.d(DEBUG_TAG, "Database updating...");
            Log.d(DEBUG_TAG, "Table " + DB_TODO_TABLE + " updated from ver." + oldVersion + " to ver." + newVersion);
            Log.d(DEBUG_TAG, "All data is lost.");

            onCreate(db);
        }
    }

    public ProductDatabase(Context context) {
        this.context = context;
    }

    public ProductDatabase open(){
        dbHelper = new DatabaseHelper(context, DB_NAME, null, DB_VERSION);
        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLException e) {
            db = dbHelper.getReadableDatabase();
        }
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public long insertProduct(String name, float price, int quantity, boolean isBought) {
        ContentValues newProductValues = new ContentValues();
        newProductValues.put(PRODUCT_NAME, name);
        newProductValues.put(PRODUCT_PRICE, price);
        newProductValues.put(PRODUCT_QUANTITY, quantity);
        newProductValues.put(PRODUCT_ISBOUGHT, isBought);
        return db.insert(DB_TODO_TABLE, null, newProductValues);
    }

    public boolean updateProductList(Product product) {
        long id = product.getProductId();
        String name = product.getProductName();
        int quantity = product.getProductQuantity();
        float price = product.getProductPrice();
        boolean bought = product.isBought();
        return updateProductList(id, name, price, quantity, bought);
    }

    public boolean updateProductList(long id, String name, float price, int quantity, boolean bought) {
        String where = PRODUCT_ID + "=" + id;
        int boughtInt = bought ? 1 : 0;
        ContentValues updateProductValues = new ContentValues();
        updateProductValues.put(PRODUCT_NAME, name);
        updateProductValues.put(PRODUCT_PRICE, price);
        updateProductValues.put(PRODUCT_QUANTITY, quantity);
        updateProductValues.put(PRODUCT_ISBOUGHT, boughtInt);
        return db.update(DB_TODO_TABLE, updateProductValues, where, null) > 0;
    }

    public boolean deleteProduct(long id){
        String where = PRODUCT_ID + "=" + id;
        return db.delete(DB_TODO_TABLE, where, null) > 0;
    }

    public Cursor getAllProducts() {
        String[] columns = {PRODUCT_ID, PRODUCT_NAME, PRODUCT_PRICE, PRODUCT_QUANTITY, PRODUCT_ISBOUGHT};
        return db.query(DB_TODO_TABLE, columns, null, null, null, null, null);
    }

    public Product getProduct(long id) {
        String[] columns = {PRODUCT_ID, PRODUCT_NAME, PRODUCT_PRICE, PRODUCT_QUANTITY, PRODUCT_ISBOUGHT};
        String where = PRODUCT_ID + "=" + id;
        Cursor cursor = db.query(DB_TODO_TABLE, columns, where, null, null, null, null);
        Product product = null;
        if(cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(PRODUCT_NAME_COLUMN);
            boolean bought = cursor.getInt(PRODUCT_ISBOUGHT_COLUMN) > 0 ? true : false;
            float price = cursor.getFloat(PRODUCT_PRICE_COLUMN);
            int quantity = cursor.getInt(PRODUCT_QUANTITY_COLUMN);
            product = new Product(id, name, price, quantity, bought);
        }
        return product;
    }
}


