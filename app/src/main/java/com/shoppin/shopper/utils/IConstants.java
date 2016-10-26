package com.shoppin.shopper.utils;

/**
 * Created by ubuntu on 21/7/16.
 */
public interface IConstants {

    /**
     * for update fragment.
     */
    String UPDATE_SPLASH_SCREEN = "com.shoppin.shopper.fragment.splashscreenactivity";
    String UPDATE_ORDER_ON_GOING = "com.shoppin.shopper.fragment.orderongoingfragment";
    String UPDATE_ORDER_REQUEST = "com.shoppin.shopper.fragment.orderrequestfragment";
    String UPDATE_ORDER_HISTORY = "com.shoppin.shopper.fragment.orderhistoryfragment";

    /**
     * for order details fragment.
     */
    String ORDER_NUMBER = "order_number";
    String IS_HISTORY = "is_history";
    String PREVIOUS_FRAGMENT = "previous_fragment";

    /**
     * navigation drawer menu list.
     */
    interface IDrawerMenu {

        String ONGOING_ORDERS = "Order Ongoing";
        int ONGOING_ORDERS_ID = 1001;

        String ORDER_REQUEST = "Order Request";
        int ORDER_REQUEST_ID = 1002;

        String ORDER_HISTORY = "Order History";
        int ORDER_HISTORY_ID = 1003;

        String MY_PROFILE = "My Profile";
        int MY_PROFILE_ID = 1004;

    }

    interface ISignIn {

        String DEVICE_TYPE = "android";
    }
}
