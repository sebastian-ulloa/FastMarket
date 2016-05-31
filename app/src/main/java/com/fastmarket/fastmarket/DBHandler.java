package com.fastmarket.fastmarket;

/**
 * Created by Sebastian on 21/05/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 2;
    // Database Name
    private static final String DATABASE_NAME = "fastMarket";
    // Contacts table name
    private static final String TABLE_PRODUCTS = "products";
    // Shops Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_PRICE = "price";
    private static final String KEY_URL = "url";
    private static final String KEY_CODE = "code";
    private boolean databaseReady = false;

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_IMAGE + " BLOB," + KEY_PRICE + " REAL,"
                + KEY_URL + " TEXT," + KEY_CODE + " TEXT" + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    public boolean addProduct(int id, Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_NAME, product.getName());
        values.put(KEY_IMAGE, product.getImage());
        values.put(KEY_PRICE, product.getPrice());
        values.put(KEY_URL, product.getImageURL());
        values.put(KEY_CODE, product.getCode());

        try {
            db.insertOrThrow(TABLE_PRODUCTS, null, values);
        } catch (SQLiteConstraintException exception) {
            Log.e("Database error", "Error inserting product id: " + id);
            return false;
        }
        db.close();
        return true;
    }

    public Product getProductByCode(String code) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PRODUCTS, new String[]{KEY_ID,
                        KEY_NAME, KEY_IMAGE, KEY_PRICE, KEY_URL, KEY_CODE}, KEY_CODE + "=?",
                new String[]{code}, null, null, null, null);
        if (cursor.getCount() != 0)
            cursor.moveToFirst();
        else
            return null;

        Product product = new Product(cursor.getString(1), cursor.getBlob(2), cursor.getFloat(3), 0,
                cursor.getString(4), cursor.getString(5));
        return product;
    }

    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<Product>();
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(cursor.getString(1), cursor.getBlob(2), cursor.getFloat(3),
                        0, cursor.getString(4), cursor.getString(5));
                productList.add(product);
            } while (cursor.moveToNext());
        }
        return productList;
    }

    public int countProducts() {
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

}
