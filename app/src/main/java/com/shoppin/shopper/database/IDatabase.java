package com.shoppin.shopper.database;

/**
 * Created by ubuntu on 27/4/16.
 */
public interface IDatabase {

    String DATABASE_NAME = "shoppin_employee";
    int DATABASE_VERSION = 1;

    interface IMap {
        String TABLE_MAP = "map";

        String FALSE = "0";
        String TRUE = "1";

        String KEY_ID = "_id";
        String KEY_MAP_KEY = "map_key";
        String KEY_MAP_VALUE = "map_value";
        String IS_LOGIN = "is_login";
        String KEY_EMPLOYEE_ID = "employee_id";
        String KEY_EMPLOYEE_SUBURB_ID = "employee_suburb_id";

        String CREATE_TABLE_MAP = "create table " + TABLE_MAP + " ("
                + KEY_ID + " integer primary key autoincrement, "
                + KEY_MAP_KEY + " text not null, "
                + KEY_MAP_VALUE + " text not null);";
    }


}
