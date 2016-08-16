package com.shoppin.customer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import static com.shoppin.customer.database.IDatabase.IMap;
/**
 * Created by ubuntu on 27/4/16.
 */
public class DBAdapter {
    private static final String TAG = DBAdapter.class.getSimpleName();

    public static void insertUpdateMap(Context context, String key, String value) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(IMap.KEY_MAP_VALUE, value);

        Cursor cursor = db.query(IMap.TABLE_MAP, new String[]{IMap.KEY_ID}, IMap.KEY_MAP_KEY + " = '" + key + "'", null, null, null, null, null);
        int index = -1;
        if (cursor != null && cursor.getCount() > 0) { //if the row exist then return the id
            cursor.moveToFirst();
            index = cursor.getInt(cursor.getColumnIndex(IMap.KEY_ID));
            cursor.close();
        }

        if (index == -1) {
            contentValues.put(IMap.KEY_MAP_KEY, key);
            db.insert(IMap.TABLE_MAP, null, contentValues);
        } else {
            db.update(IMap.TABLE_MAP, contentValues, IMap.KEY_ID + " = '" + index + "'", null);
        }
        Log.d(TAG, "insertUpdateMap key = " + key + ", value = " + value);
    }

    public static String getMapKeyValueString(Context context, String key) {
        String value = null;
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + IMap.TABLE_MAP + " where " + IMap.KEY_MAP_KEY + " = '" + key + "'", null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            value = cursor.getString(cursor.getColumnIndexOrThrow(IMap.KEY_MAP_VALUE));
            cursor.close(); // that's important too, otherwise you're gonna leak cursors
        }
        Log.d(TAG, "getMapKeyValueString key = " + key + ", value = " + value);
        return value;
    }

    public static boolean getMapKeyValueBoolean(Context context, String key) {
        boolean result = false;
        if (IMap.TRUE.equalsIgnoreCase(getMapKeyValueString(context, key))) {
            result = true;
        }
        Log.d(TAG, "getMapKeyValueBoolean value [" + key + "] = " + result);
        return result;
    }

    public static void setMapKeyValueBoolean(Context context, String key,
                                             boolean value) {
        Log.d(TAG, "setMapKeyValueBoolean value[" + key + "] = " + value);
        if (value) {
            insertUpdateMap(context, key, IMap.TRUE);
        } else {
            insertUpdateMap(context, key, IMap.FALSE);
        }
    }

//    /**
//     * Clear offline data
//     *
//     * @param context
//     */
//    public static void clearOfflineData(Context context) {
//        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
//        db.delete(IDatabase.ICategory.TABLE_CATEGORY, null, null);
//        db.delete(IDatabase.ISubCategory.TABLE_SUB_CATEGORY, null, null);
//        db.delete(IDatabase.IProduct.TABLE_PRODUCT, null, null);
//    }
//
//    public static boolean insertLocations(Context context, ArrayList<MyLocation> myLocationArrayList, JSONArray categoryJArray) {
//        try {
//            if (myLocationArrayList != null && myLocationArrayList.size() > 0) {
//                Log.e(TAG, "myLocationArrayList.size() = " + myLocationArrayList.size());
//
//                SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
//
//                for (int i = 0; i < myLocationArrayList.size(); i++) {
//                    ContentValues contentValues = new ContentValues();
//                    contentValues.put(ILocation.KEY_LOCATION_ID, myLocationArrayList.get(i).id);
//                    contentValues.put(ILocation.KEY_LOCATION_NAME, myLocationArrayList.get(i).location);
//                    contentValues.put(ILocation.KEY_JSON, categoryJArray.getString(i));
//                    db.insert(ILocation.TABLE_LOCATION, null, contentValues);
//                }
//            } else {
//                return false;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//
//    public static String getLocations(Context context) {
//        JSONObject rootJObject = new JSONObject();
//        try {
//            SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
//            Cursor cursor = db.rawQuery("select * from " + ILocation.TABLE_LOCATION, null);
//
//            JSONArray dataJArray = new JSONArray();
//            // looping through all rows and adding to list
//            if (cursor != null && cursor.getCount() > 0) {
//                cursor.moveToFirst();
//                do {
//                    dataJArray.put(new JSONObject(cursor.getString(cursor.getColumnIndex(ICategory.KEY_JSON))));
//                } while (cursor.moveToNext());
//                cursor.close(); // that's important too, otherwise you're gonna leak cursors
//
//                rootJObject.put(IWebService.KEY_RES_SUCCESS, true);
//                rootJObject.put(IWebService.KEY_RES_MESSAGE, "location offline");
//            } else {
//                rootJObject.put(IWebService.KEY_RES_SUCCESS, false);
//                rootJObject.put(IWebService.KEY_RES_MESSAGE, "No offline data available for location");
//            }
//            rootJObject.put(IWebService.KEY_RES_DATA, dataJArray);
//        } catch (JSONException e) {
//            e.printStackTrace();
//            try {
//                rootJObject.put(IWebService.KEY_RES_SUCCESS, false);
//                rootJObject.put(IWebService.KEY_RES_MESSAGE, "No offline data available for location");
//            } catch (JSONException e1) {
//                e1.printStackTrace();
//            }
//        }
//
//        return rootJObject.toString();
//    }
//
//    public static boolean insertCategories(Context context, ArrayList<Category> categoryArrayList, JSONArray categoryJArray) {
//        try {
//            if (categoryArrayList != null && categoryArrayList.size() > 0) {
//                Log.e(TAG, "categoryArrayList.size() = " + categoryArrayList.size());
//
//                SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
//
//                for (int i = 0; i < categoryArrayList.size(); i++) {
//                    ContentValues contentValues = new ContentValues();
//                    contentValues.put(ICategory.KEY_CATEGORY_ID, categoryArrayList.get(i).id);
//                    contentValues.put(ICategory.KEY_NAME, categoryArrayList.get(i).name);
//                    contentValues.put(ICategory.KEY_ICON, categoryArrayList.get(i).icon);
//                    contentValues.put(ICategory.KEY_TYPE, categoryArrayList.get(i).type);
//                    contentValues.put(ICategory.KEY_WEB_LINK, categoryArrayList.get(i).web_link);
//                    contentValues.put(ICategory.KEY_JSON, categoryJArray.getString(i));
//                    db.insert(ICategory.TABLE_CATEGORY, null, contentValues);
//                }
//            } else {
//                return false;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//
//    public static String getCategories(Context context) {
//        JSONObject rootJObject = new JSONObject();
//        JSONObject dataJObject = new JSONObject();
//        JSONArray categoryJArray = new JSONArray();
//        try {
//            SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
//            Cursor cursor = db.rawQuery("select * from " + ICategory.TABLE_CATEGORY, null);
//
//            // looping through all rows and adding to list
//            if (cursor != null && cursor.getCount() > 0) {
//                cursor.moveToFirst();
//                do {
//                    //                Contact contact = new Contact();
//                    //                contact.setID(Integer.parseInt(cursor.getString(0)));
//                    //                contact.setName(cursor.getString(1));
//                    //                contact.setPhoneNumber(cursor.getString(2));
//                    //                // Adding contact to list
//                    //                contactList.add(contact);
//                    categoryJArray.put(new JSONObject(cursor.getString(cursor.getColumnIndex(ICategory.KEY_JSON))));
//                } while (cursor.moveToNext());
//                cursor.close(); // that's important too, otherwise you're gonna leak cursors
//
//                rootJObject.put(IWebService.KEY_RES_SUCCESS, true);
//                rootJObject.put(IWebService.KEY_RES_MESSAGE, "category offline");
//            } else {
//                rootJObject.put(IWebService.KEY_RES_SUCCESS, false);
//                rootJObject.put(IWebService.KEY_RES_MESSAGE, "No offline data available for category");
//            }
//            dataJObject.put(IWebService.KEY_RES_CATEGORY, categoryJArray);
//            rootJObject.put(IWebService.KEY_RES_DATA, dataJObject);
//        } catch (JSONException e) {
//            e.printStackTrace();
//            try {
//                rootJObject.put(IWebService.KEY_RES_SUCCESS, false);
//                rootJObject.put(IWebService.KEY_RES_MESSAGE, "No offline data available for category");
//                dataJObject.put(IWebService.KEY_RES_CATEGORY, new JSONArray());
//                rootJObject.put(IWebService.KEY_RES_DATA, dataJObject);
//            } catch (JSONException e1) {
//                e1.printStackTrace();
//            }
//        }
//
//        return rootJObject.toString();
//    }
//
//    public static boolean insertSubCategories(Context context, ArrayList<SubCategory> subCategoryArrayList, JSONArray subCategoryJArray) {
//        try {
//            if (subCategoryArrayList != null && subCategoryArrayList.size() > 0) {
//                Log.e(TAG, "subCategories.size() = " + subCategoryArrayList.size());
//
//                SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
//
//                for (int i = 0; i < subCategoryArrayList.size(); i++) {
//                    ContentValues contentValues = new ContentValues();
//                    contentValues.put(ISubCategory.KEY_SUB_CATEGORY_ID, subCategoryArrayList.get(i).id);
//                    contentValues.put(ISubCategory.KEY_CATEGORY_ID, subCategoryArrayList.get(i).cat_id);
//                    contentValues.put(ISubCategory.KEY_NAME, subCategoryArrayList.get(i).name);
//                    contentValues.put(ISubCategory.KEY_JSON, subCategoryJArray.getString(i));
//                    db.insert(ISubCategory.TABLE_SUB_CATEGORY, null, contentValues);
//                }
//            } else {
//                return false;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//
//    public static boolean insertProduct(Context context, ArrayList<Product> productArrayList, JSONArray productJArray) {
//        try {
//            if (productArrayList != null
//                    && productArrayList.size() > 0) {
//                Log.e(TAG, "productArrayList.size() = "
//                        + productArrayList.size());
//                SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
//
//                for (int i = 0; i < productArrayList.size(); i++) {
//                    ContentValues contentValues = new ContentValues();
//                    contentValues.put(IProduct.KEY_PRODUCT_ID, productArrayList.get(i).product_id);
//                    contentValues.put(IProduct.KEY_CATEGORY_ID, productArrayList.get(i).cat_id);
//                    contentValues.put(IProduct.KEY_SUB_CATEGORY_ID, productArrayList.get(i).sub_cat_id);
//                    contentValues.put(IProduct.KEY_NAME, productArrayList.get(i).name);
//                    contentValues.put(IProduct.KEY_LOCATION, productArrayList.get(i).location);
//                    contentValues.put(IProduct.KEY_JSON, productJArray.getString(i));
//
//                    db.insert(IProduct.TABLE_PRODUCT, null, contentValues);
//                }
//            } else {
//                return false;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//
//    public static String getProducts(Context context, String filterParams) {
//        JSONObject rootJObject = new JSONObject();
//        JSONObject dataJObject = new JSONObject();
//        int pageCount = -1;
//
//        try {
//            JSONObject filterJObject = new JSONObject(filterParams);
//
//            SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
//
//            /**
//             * For sub category compare Category id
//             */
//            if (filterJObject.has(IWebService.KEY_REQ_ID) && !Utils.isNullOrEmpty(filterJObject.getString(IWebService.KEY_REQ_ID))) {
//                JSONArray subCategoryJArray = new JSONArray();
//                String subCategoryQuery = "SELECT " + ISubCategory.KEY_JSON
//                        + " FROM " + ISubCategory.TABLE_SUB_CATEGORY
//                        + " WHERE " + ISubCategory.KEY_CATEGORY_ID + " = " + filterJObject.getString(IWebService.KEY_REQ_ID);
//                Cursor subCategoryCursor = db.rawQuery(subCategoryQuery, null);
//                if (subCategoryCursor != null && subCategoryCursor.getCount() > 0) {
//                    subCategoryCursor.moveToFirst();
//                    do {
//                        subCategoryJArray.put(new JSONObject(subCategoryCursor.getString(subCategoryCursor.getColumnIndex(ICategory.KEY_JSON))));
//                    } while (subCategoryCursor.moveToNext());
//                    subCategoryCursor.close();
//                    dataJObject.put(IWebService.KEY_RES_SUBCATEGORY, subCategoryJArray);
//                }
//            }
//
//            /**
//             * For product
//             */
//            String productQuery = "SELECT " + IProduct.KEY_JSON
//                    + " FROM " + IProduct.TABLE_PRODUCT + " WHERE ";
//            ArrayList<String> componentQuery = new ArrayList<>();
//
//            // Location id
//            // assuming that id of ALL will be 0
//            if (filterJObject.has(IWebService.KEY_REQ_LOCATION_ID)
//                    && !Utils.isNullOrEmpty(filterJObject.getString(IWebService.KEY_REQ_LOCATION_ID))
//                    && !filterJObject.getString(IWebService.KEY_REQ_LOCATION_ID).equals("0")) {
//                componentQuery.add(IProduct.KEY_LOCATION + " = " + filterJObject.getString(IWebService.KEY_REQ_LOCATION_ID));
//            }
//
//            // Category id
//            if (filterJObject.has(IWebService.KEY_REQ_ID) && !Utils.isNullOrEmpty(filterJObject.getString(IWebService.KEY_REQ_ID))) {
//                componentQuery.add(IProduct.KEY_CATEGORY_ID + " = " + filterJObject.getString(IWebService.KEY_REQ_ID));
//            }
//
//            // Sub Category id
//            if (filterJObject.has(IWebService.KEY_REQ_SUBCATEGORY)) {
//                String subCategoryQuery = "(";
//                JSONArray subCategoryJArray = filterJObject.getJSONArray(IWebService.KEY_REQ_SUBCATEGORY);
//                for (int i = 0; i < subCategoryJArray.length(); i++) {
//                    if (i == 0) {
//                        subCategoryQuery += "(',' || " + IProduct.KEY_SUB_CATEGORY_ID + " || ',') LIKE '%," + subCategoryJArray.get(i) + ",%'";
//                    } else {
//                        subCategoryQuery += "OR (',' || " + IProduct.KEY_SUB_CATEGORY_ID + " || ',') LIKE '%," + subCategoryJArray.get(i) + ",%'";
//                    }
//                }
//                subCategoryQuery += ")";
//                componentQuery.add(subCategoryQuery);
//            }
//
//            // keyword
//            if (filterJObject.has(IWebService.KEY_REQ_KEYWORD) && !Utils.isNullOrEmpty(filterJObject.getString(IWebService.KEY_REQ_KEYWORD))) {
//                componentQuery.add(IProduct.KEY_NAME + " LIKE '%" + filterJObject.getString(IWebService.KEY_REQ_KEYWORD) + "%'");
//            }
//
//            for (int i = 0; i < componentQuery.size(); i++) {
//                if (i == 0) {
//                    productQuery += componentQuery.get(i);
//                } else {
//                    productQuery += " AND " + componentQuery.get(i);
//                }
//            }
//            productQuery += " ORDER BY(" + IProduct.KEY_NAME + ") ASC";
//
////            String pagination = "";
//            int limit = 10;
//            if (filterJObject.has(IWebService.KEY_REQ_PAGE_NO)) {
//                pageCount = filterJObject.getInt(IWebService.KEY_REQ_PAGE_NO);
//                if (pageCount == 1) {
//                    productQuery += " LIMIT " + limit + " OFFSET 0";
//                } else if (pageCount > 1) {
//                    productQuery += " LIMIT " + limit + " OFFSET " + ((pageCount - 1) * limit);
//                }
//            }
//
//            Log.d(TAG, "productQuery = " + productQuery);
//
//            Cursor productCursor = db.rawQuery(productQuery, null);
//            JSONArray productJArray = new JSONArray();
//            // looping through all rows and adding to list
//            if (productCursor != null && productCursor.getCount() > 0) {
//                productCursor.moveToFirst();
//                do {
//                    productJArray.put(new JSONObject(productCursor.getString(productCursor.getColumnIndex(ICategory.KEY_JSON))));
//                } while (productCursor.moveToNext());
//                productCursor.close(); // that's important too, otherwise you're gonna leak cursors
//
//                dataJObject.put(IWebService.KEY_RES_PRODUCT, productJArray);
//                rootJObject.put(IWebService.KEY_RES_SUCCESS, true);
//                rootJObject.put(IWebService.KEY_RES_MESSAGE, "product offline");
//            } else {
//                dataJObject.put(IWebService.KEY_RES_PRODUCT, new JSONArray());
//                rootJObject.put(IWebService.KEY_RES_SUCCESS, false);
//                if (pageCount > 1) {
//                    rootJObject.put(IWebService.KEY_RES_MESSAGE, "No more offline data available for this product");
//                } else {
//                    rootJObject.put(IWebService.KEY_RES_MESSAGE, "No offline data available for this product");
//                }
//            }
//            if (!dataJObject.has(IWebService.KEY_RES_SUBCATEGORY)) {
//                dataJObject.put(IWebService.KEY_RES_SUBCATEGORY, new JSONArray());
//            }
//            rootJObject.put(IWebService.KEY_RES_DATA, dataJObject);
//        } catch (JSONException e) {
//            e.printStackTrace();
//            try {
//                dataJObject.put(IWebService.KEY_RES_SUBCATEGORY, new JSONArray());
//                dataJObject.put(IWebService.KEY_RES_PRODUCT, new JSONArray());
//                rootJObject.put(IWebService.KEY_RES_DATA, dataJObject);
//                rootJObject.put(IWebService.KEY_RES_SUCCESS, false);
//                rootJObject.put(IWebService.KEY_RES_MESSAGE, "No offline data available for this product");
//            } catch (Exception e1) {
//                e1.printStackTrace();
//            }
//        }
//        return rootJObject.toString();
//    }
//
//
//    public static String getFavouriteProducts(Context context, String filterParams) {
//        JSONObject rootJObject = new JSONObject();
//        JSONObject dataJObject = new JSONObject();
//        int pageCount = -1;
//
//        try {
//            JSONObject filterJObject = new JSONObject(filterParams);
//
//            SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
//
//            /**
//             * For sub category compare Category id
//             */
//            String favProduct = getMapKeyValueString(context, IMap.KEY_FAVOURITE);
//            if (!Utils.isNullOrEmpty(favProduct)) {
//                JSONArray favProductJArray = new JSONArray(favProduct);
//                if (favProductJArray.length() > 0) {
//                    /**
//                     * For product
//                     */
//                    String productQuery = "SELECT " + IProduct.KEY_JSON
//                            + " FROM " + IProduct.TABLE_PRODUCT + " WHERE ";
//                    ArrayList<String> componentQuery = new ArrayList<>();
//
//                    String subCategoryQuery = IProduct.KEY_PRODUCT_ID + " IN (";
//                    for (int i = 0; i < favProductJArray.length(); i++) {
//                        if (i < favProductJArray.length() - 1) {
//                            subCategoryQuery += "'" + favProductJArray.get(i) + "', ";
//                        } else {
//                            subCategoryQuery += "'" + favProductJArray.get(i) + "'";
//                        }
//                    }
//                    subCategoryQuery += ")";
//                    componentQuery.add(subCategoryQuery);
//
//                    for (int i = 0; i < componentQuery.size(); i++) {
//                        if (i == 0) {
//                            productQuery += componentQuery.get(i);
//                        } else {
//                            productQuery += " AND " + componentQuery.get(i);
//                        }
//                    }
//                    productQuery += " ORDER BY(" + IProduct.KEY_NAME + ") ASC";
//
//                    int limit = 10;
//                    if (filterJObject.has(IWebService.KEY_REQ_PAGE_NO)) {
//                        pageCount = filterJObject.getInt(IWebService.KEY_REQ_PAGE_NO);
//                        if (pageCount == 1) {
//                            productQuery += " LIMIT " + limit + " OFFSET 0";
//                        } else if (pageCount > 1) {
//                            productQuery += " LIMIT " + limit + " OFFSET " + ((pageCount - 1) * limit);
//                        }
//                    }
//
//                    Log.d(TAG, "productQuery = " + productQuery);
//
//                    Cursor productCursor = db.rawQuery(productQuery, null);
//                    JSONArray productJArray = new JSONArray();
//                    // looping through all rows and adding to list
//                    if (productCursor != null && productCursor.getCount() > 0) {
//                        productCursor.moveToFirst();
//                        do {
//                            productJArray.put(new JSONObject(productCursor.getString(productCursor.getColumnIndex(ICategory.KEY_JSON))));
//                        } while (productCursor.moveToNext());
//                        productCursor.close(); // that's important too, otherwise you're gonna leak cursors
//
//                        dataJObject.put(IWebService.KEY_RES_PRODUCT, productJArray);
//                        rootJObject.put(IWebService.KEY_RES_DATA, dataJObject);
//                        rootJObject.put(IWebService.KEY_RES_SUCCESS, true);
//                        rootJObject.put(IWebService.KEY_RES_MESSAGE, "my stuff offline");
//                    } else {
//                        throw new Exception("no data found");
//                    }
//                } else {
//                    throw new Exception("favourite array is empty");
//                }
//            } else {
//                throw new Exception("favourite array is null");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            try {
//                dataJObject.put(IWebService.KEY_RES_PRODUCT, new JSONArray());
//                rootJObject.put(IWebService.KEY_RES_DATA, dataJObject);
//                rootJObject.put(IWebService.KEY_RES_SUCCESS, false);
//                if (pageCount > 1) {
//                    rootJObject.put(IWebService.KEY_RES_MESSAGE, "No more offline data available for my stuff");
//                } else {
//                    rootJObject.put(IWebService.KEY_RES_MESSAGE, "No offline data available for my stuff");
//                }
//            } catch (Exception e1) {
//                e1.printStackTrace();
//            }
//        }
//        return rootJObject.toString();
//    }
//
//    public static String getProductDetail(Context context, String filterParams) {
//        JSONObject rootJObject = new JSONObject();
//        JSONObject dataJObject = new JSONObject();
//
//        try {
//            JSONObject filterJObject = new JSONObject(filterParams);
//
//            SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
//
//            /**
//             * For sub category compare Category id
//             */
//            if (filterJObject.has(IWebService.KEY_REQ_PRODUCTID)
//                    && !Utils.isNullOrEmpty(filterJObject.getString(IWebService.KEY_REQ_PRODUCTID))) {
//                String subCategoryQuery = "SELECT " + IProduct.KEY_JSON
//                        + " FROM " + IProduct.TABLE_PRODUCT
//                        + " WHERE " + IProduct.KEY_PRODUCT_ID + " = " + filterJObject.getString(IWebService.KEY_REQ_PRODUCTID);
//                Cursor productDetailCursor = db.rawQuery(subCategoryQuery, null);
//                if (productDetailCursor != null && productDetailCursor.getCount() > 0) {
//                    productDetailCursor.moveToFirst();
//                    dataJObject = new JSONObject(productDetailCursor.getString(productDetailCursor.getColumnIndex(ICategory.KEY_JSON)));
//                    rootJObject.put(IWebService.KEY_RES_DATA, dataJObject);
//                    rootJObject.put(IWebService.KEY_RES_SUCCESS, true);
//                    rootJObject.put(IWebService.KEY_RES_MESSAGE, "product detail offline");
//                    productDetailCursor.close();
//                }
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } finally {
//            if (!rootJObject.has(IWebService.KEY_RES_DATA)) {
//                try {
//                    rootJObject.put(IWebService.KEY_RES_DATA, dataJObject);
//                    rootJObject.put(IWebService.KEY_RES_SUCCESS, false);
//                    rootJObject.put(IWebService.KEY_RES_MESSAGE, "product detail offline not available");
//                } catch (JSONException e1) {
//                    e1.printStackTrace();
//                }
//            }
//        }
//        return rootJObject.toString();
//    }
//
//    public static String getFavourites(Context context) {
//        JSONObject rootJObject = new JSONObject();
//        JSONObject dataJObject = new JSONObject();
//        try {
//            String favProduct = getMapKeyValueString(context, IMap.KEY_FAVOURITE);
//
//            // looping through all rows and adding to list
//            if (!Utils.isNullOrEmpty(favProduct)) {
//                rootJObject.put(IWebService.KEY_RES_SUCCESS, true);
//                rootJObject.put(IWebService.KEY_RES_MESSAGE, "category offline");
//                dataJObject.put(IWebService.KEY_RES_PRODUCT_ID, new JSONArray(favProduct));
//                rootJObject.put(IWebService.KEY_RES_DATA, dataJObject);
//            } else {
//                throw new Exception("favourite array is null");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            try {
//                rootJObject.put(IWebService.KEY_RES_SUCCESS, false);
//                rootJObject.put(IWebService.KEY_RES_MESSAGE, "No offline data available for favourites");
//                rootJObject.put(IWebService.KEY_RES_DATA, dataJObject);
//            } catch (JSONException e1) {
//                e1.printStackTrace();
//            }
//        }
//
//        return rootJObject.toString();
//    }
}
