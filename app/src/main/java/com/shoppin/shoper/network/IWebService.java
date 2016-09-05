package com.shoppin.shoper.network;

/**
 * Created by ubuntu on 10/8/16.
 */

public interface IWebService {

    String MAIN_URL = " http://192.168.0.1/shoppin/service/index.php/";

    String KEY_RES_DATA = "data";
    String KEY_RES_SUCCESS = "success";
    String KEY_RES_MESSAGE = "message";

    String GET_SUBURB = MAIN_URL + "customer/GetSuburb";
    String EMPLOYEE_LOGIN = MAIN_URL + "shopper/EmployeeLogin";
    String ORDER_REQUEST = MAIN_URL + "shopper/OrderRequests";
    String ACEEPT_ORDER = MAIN_URL + "shopper/OrderAction";
    String ORDER_DETAILS = MAIN_URL + "shopper/OrderDetail";



    String GET_CATEGORY = MAIN_URL + "category/GetCategoryList";

    /**
     * Request Params
     */
    String KEY_REQ_FALSE = "false";
    String KEY_REQ_TRUE = "true";


    String KEY_REQ_EMPLOYEE_MOBILE = "employee_mobile";
    String KEY_REQ_EMPLOYEE_PASSWORD = "employee_password";
    String KEY_REQ_EMPLOYEE_DEVICE_TYPE = "employee_device_type";
    String KEY_REQ_EMPLOYEE_DEVICE_TOKEN = "employee_device_token";
    String KEY_REQ_EMPLOYEE_DEVICE_ID = "employee_device_id";

    String KEY_REQ_ORDER_SUBURB_ID = "order_suburb_id";
    String KEY_REQ_EMPLOYEE_ID = "employee_id";

    String KEY_REQ_ORDER_NUMBER = "order_number";
    String KEY_REQ_ACTION = "action";



    /**
     * Response Params
     */

    String KEY_RES_EMPLOYEE_ID = "employee_id";
    String KEY_RES_EMPLOYEE_NAME = "employee_name";
    String KEY_RES_EMPLOYEE_EMAIL = "employee_email";
    String KEY_RES_EMPLOYEE_MOBILE = "employee_mobile";
    String KEY_RES_EMPLOYEE_SUBURB_ID = "employee_suburb_id";

    String KEY_RES_SUBURB_LIST = "suburb_list";
    String KEY_RES_SUBURB_ID = "suburb_id";
    String KEY_RES_CATEGORY_LIST = "category_list";

    String KEY_RES_ORDER_LIST = "order_list";
    String KEY_RES_PRODUCT_LIST = "product_list";
    String KEY_RES_IMAGES = "images";
    String KEY_RES_OPTION_LIST = "option_list";


    String KEY_RES_ORDER_NAME = "order_number";
    String KEY_RES_ORDER_SLOT_ID = "order_slot_id";
    String KEY_RES_SHIPPING_ADDRESS_ID = "shipping_address_id";
    String KEY_RES_SUBURB_NAME = "suburb_name";
    String KEY_RES_TOTAL = "total";
    String KEY_RES_STATUS = "status";
    String KEY_RES_ADDRESS1 = "address1";
    String KEY_RES_DELIVERY_DATR = "delivery_date";
    String KEY_RES_DELIVERY_TIME = "delivery_time";
    String KEY_RES_CUSTOMER_MOBILE = "customer_mobile";
    String KEY_RES_STORE_NAME = "store_name";

    String KEY_RES_PRODUCT_ITEM_ID = "product_items_id";
    String KEY_RES_PRODUCT_ID = "product_id";
    String KEY_RES_SALEPRICE_1 = "saleprice_1";
    String KEY_RES_PRODUCT_NAME = "product_name";







}
