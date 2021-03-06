package com.shoppin.shopper.network;


/**
 * Created by ubuntu on 10/8/16.
 */

public interface
IWebService {
    /* for payment express*/

    String TRANSACTION_REQUEST = "https://sec.paymentexpress.com/pxaccess/pxpay.aspx";
    String TRANSACTION_REQUEST_TEST = "https://uat.paymentexpress.com/pxaccess/pxpay.aspx";
    String ON_TRANSACTION_SUCCESS = "https://www.dpsdemo.com/SandboxSuccess.aspx";
    String ON_TRANSACTION_FAIL= "https://www.dpsdemo.com/SandboxSuccess.aspx";

    /*for shoper order data*/
    //String MAIN_URL = " http://192.168.0.1/shoppin/service/index.php/";
    String MAIN_URL = " http://dddemo.net/php/shoppin/service/index.php/";

    String KEY_RES_DATA = "data";
    String KEY_RES_SUCCESS = "success";
    String KEY_RES_MESSAGE = "message";


    String EMPLOYEE_LOGIN = MAIN_URL + "shopper/EmployeeLogin";
    String ORDER_REQUEST = MAIN_URL + "shopper/OrderRequests";
    String ORDER_ACTION = MAIN_URL + "shopper/OrderAction";
    String ORDER_DETAILS = MAIN_URL + "shopper/OrderDetail";
    String UPDATE_ORDER_ITEM_AVAILABILITY = MAIN_URL + "shopper/UpdateOrderItemAvailability";
    String ORDER_HISTORY = MAIN_URL + "shopper/OrderHistory";
    String ONGOING_ORDER = MAIN_URL + "shopper/OngoingOrders";
    String EMPLOYEE_APP_VERSION = MAIN_URL + "app/VersionEmployee";
    String DEVICE_REGISTRATION = MAIN_URL + "shopper/DeviceRegistration";
    String GET_EMPLOYEE_PROFILE = MAIN_URL + "shopper/GetEmployeeProfile";
    String UPDATE_EMPLOYEE_PROFILE = MAIN_URL + "shopper/UpdateEmployeeProfile";
    String EMPLOYEE_SIGN_OUT = MAIN_URL + "shopper/EmployeeLogout";

    /**
     * Request Params
     */

    String KEY_REQ_EMPLOYEE_MOBILE = "employee_mobile";
    String KEY_REQ_EMPLOYEE_PASSWORD = "employee_password";
    String KEY_REQ_EMPLOYEE_DEVICE_TYPE = "employee_device_type";
    String KEY_REQ_EMPLOYEE_DEVICE_TOKEN = "employee_device_token";
    String KEY_REQ_EMPLOYEE_DEVICE_ID = "employee_device_id";
    String KEY_REQ_DEVICE_TYPE = "device_type";


    String KEY_REQ_EMPLOYEE_ID = "employee_id";
    String KEY_REQ_EMPLOYEE_NAME = "employee_name";
    String KEY_REQ_EMPLOYEE_EMAIL = "employee_email";
    String KEY_REQ_EMPLOYEE_SUBURB_ID = "employee_suburb_id";
    String KEY_REQ_ORDER_SUBURB_ID = "order_suburb_id";


    String KEY_REQ_ORDER_NUMBER = "order_number";
    String KEY_REQ_ORDER_ITEM_ID = "order_item_id";
    String KEY_REQ_SET_VALUE = "set_value";
    String KEY_REQ_PAGE_NO = "page_no";

    int KEY_REQ_STATUS_PLACED = 1;
    int KEY_REQ_STATUS_REJECTED = 2;
    int KEY_REQ_STATUS_ACCEPTED = 3;
    int KEY_REQ_STATUS_PURCHASING = 4;
    int KEY_REQ_STATUS_SHIPPING = 5;
    int KEY_REQ_STATUS_COMPLETED = 6;
    int KEY_REQ_STATUS_ON_HOLD = 7;
    int KEY_REQ_STATUS_CANCELLED = 8;

    int KEY_REQ_PREFERRED_STORE_ID = 0;


    int KEY_REQ_STATUS_PRODUCT_AVAILABLE = 1;
    int KEY_REQ_STATUS_PRODUCT_NOT_AVAILABLE = -1;
    int KEY_REQ_STATUS_PRODUCT_NORMAL = 0;


    String KEY_REQ_PRODUCT_COMMENTS = "comment";
    String KEY_REQ_NULL = "null";
    String KEY_REQ_STATUS = "status";
    String KEY_REQ_CURRENT_APP_VERSION = "current_version";


    /**
     * Response Params
     */

    String KEY_RES_EMPLOYEE_ID = "employee_id";
    String KEY_RES_EMPLOYEE_NAME = "employee_name";
    String KEY_RES_EMPLOYEE_EMAIL = "employee_email";
    String KEY_RES_EMPLOYEE_MOBILE = "employee_mobile";
    String KEY_RES_EMPLOYEE_SUBURB_ID = "employee_suburb_id";


    String KEY_RES_UPDATE_REQUIRE = "update_require";

    String KEY_RES_ORDER_LIST = "order_list";
    String KEY_RES_PRODUCT_LIST = "product_list";
    String KEY_RES_IMAGES = "images";
    String KEY_RES_OPTION_LIST = "option_list";
    String KEY_RES_STATUS_LIST = "status_list";
    String KET_RES_SUBURB_LIST = "suburb_list";


    String KEY_RES_ORDER_NAMBER = "order_number";
    String KEY_RES_ORDER_SLOT_ID = "order_slot_id";
    String KEY_RES_SHIPPING_ADDRESS_ID = "shipping_address_id";
    String KEY_RES_SUBURB_NAME = "suburb_name";
    String KEY_RES_TOTAL = "total";
    String KEY_RES_STATUS = "status";
    String KEY_RES_STATUS_LABEL = "status_label";
    String KEY_RES_ADDRESS1 = "address1";
    String KEY_RES_DELIVERY_DATE = "delivery_date";
    String KEY_RES_DELIVERY_TIME = "delivery_time";
    String KEY_RES_SHIPPING_DATE = "shipping_date";
    String KEY_RES_SHIPPING_TIME = "shipping_time";
    String KEY_RES_CUSTOMER_MOBILE = "customer_mobile";
    String KEY_RES_CUSTOMER_NAME = "customer_name";
    String KEY_RES_STORE_NAME = "store_name";
    String KEY_RES_ITEM_COUNT = "item_count";
    String KEY_RES_PREFERRED_STORE_ID = "preferred_store_id";
    String KEY_RES_STORE_ADDRESS = "store_address";
    String KEY_RES_ZIP = "zip";
    String KET_RES_PREV_STATUS = "prev_status";


    String KEY_RES_PRODUCT_ITEM_ID = "product_items_id";
    String KEY_RES_PRODUCT_ID = "productId";
    String KEY_RES_SALEPRICE_1 = "saleprice1";
    String KEY_RES_PRODUCT_NAME = "productName";
    String KEY_RES_PRODUCT_COMMENTS = "comment";
    String KEY_RES_SET_VALUE = "set_value";


}
