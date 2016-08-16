package com.shoppin.customer.database;

/**
 * Created by ubuntu on 27/4/16.
 */
public interface IDatabase {

    String DATABASE_NAME = "shoppin_customer";
    int DATABASE_VERSION = 1;

    interface IMap {
        String TABLE_MAP = "map";

        String FALSE = "0";
        String TRUE = "1";

        String KEY_ID = "_id";
        String KEY_MAP_KEY = "map_key";
        String KEY_MAP_VALUE = "map_value";
        String IS_LOGIN = "is_login";
        String SUBURB_ID = "suburb_id";
        String SUBURB_NAME = "suburb_name";
        String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
        String GCM_REGISTRATION_ID = "gcm_registration_id";

        String CREATE_TABLE_MAP = "create table " + TABLE_MAP + " ("
                + KEY_ID + " integer primary key autoincrement, "
                + KEY_MAP_KEY + " text not null, "
                + KEY_MAP_VALUE + " text not null);";
    }

    interface ILocation {
        String TABLE_LOCATION = "location";

        String KEY_ID = "_id";
        String KEY_LOCATION_ID = "location_id";
        String KEY_LOCATION_NAME = "location";
        String KEY_JSON = "json";

        String CREATE_TABLE_lOCATION = "create table " + TABLE_LOCATION + " ("
                + KEY_ID + " integer primary key autoincrement, "
                + KEY_LOCATION_ID + " text not null, "
                + KEY_LOCATION_NAME + " text, "
                + KEY_JSON + " text);";
    }

    interface ICategory {
        String TABLE_CATEGORY = "category";

        String KEY_ID = "_id";
        String KEY_CATEGORY_ID = "category_id";
        String KEY_NAME = "name";
        String KEY_ICON = "icon";
        String KEY_TYPE = "type";
        String KEY_WEB_LINK = "web_link";
        String KEY_JSON = "json";


        String CREATE_TABLE_CATEGORY = "create table " + TABLE_CATEGORY + " ("
                + KEY_ID + " integer primary key autoincrement, "
                + KEY_CATEGORY_ID + " text not null, "
                + KEY_NAME + " text, "
                + KEY_ICON + " text, "
                + KEY_TYPE + " text, "
                + KEY_WEB_LINK + " text, "
                + KEY_JSON + " text);";
    }

    interface ISubCategory {
        String TABLE_SUB_CATEGORY = "sub_category";

        String KEY_ID = "_id";
        String KEY_SUB_CATEGORY_ID = "sub_category_id";
        String KEY_CATEGORY_ID = "category_id";
        String KEY_NAME = "name";
        String KEY_JSON = "json";


        String CREATE_TABLE_SUB_CATEGORY = "create table " + TABLE_SUB_CATEGORY + " ("
                + KEY_ID + " integer primary key autoincrement, "
                + KEY_SUB_CATEGORY_ID + " text not null, "
                + KEY_CATEGORY_ID + " text not null, "
                + KEY_NAME + " text, "
                + KEY_JSON + " text);";
    }

    interface IProduct {
        String TABLE_PRODUCT = "product";

        String KEY_ID = "_id";
        String KEY_PRODUCT_ID = "product_id";
        String KEY_CATEGORY_ID = "category_id";
        String KEY_SUB_CATEGORY_ID = "sub_cat_id";
        String KEY_NAME = "name";
        String KEY_LOCATION = "location";
        String KEY_JSON = "json";

        String CREATE_TABLE_PRODUCT = "create table " + TABLE_PRODUCT + " ("
                + KEY_ID + " integer primary key autoincrement, "
                + KEY_PRODUCT_ID + " text not null, "
                + KEY_CATEGORY_ID + " text not null, "
                + KEY_SUB_CATEGORY_ID + " text not null, "
                + KEY_LOCATION + " text not null, "
                + KEY_NAME + " text, "
                + KEY_JSON + " text );";
    }
}
