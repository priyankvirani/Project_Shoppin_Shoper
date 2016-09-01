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
    String EMPLOYEE_REGISTRATION = MAIN_URL + "customer/CustomerRegistration";
    String ORDER_REQUEST = MAIN_URL + "shopper/OrderRequests";


    String GET_CATEGORY = MAIN_URL + "category/GetCategoryList";

    /**
     * Request Params
     */
    String KEY_REQ_EMPLOYEE_MOBILE = "employee_mobile";
    String KEY_REQ_EMPLOYEE_PASSWORD = "employee_password";
    String KEY_REQ_EMPLOYEE_DEVICE_TYPE = "employee_device_type";
    String KEY_REQ_EMPLOYEE_DEVICE_TOKEN = "employee_device_token";
    String KEY_REQ_EMPLOYEE_DEVICE_ID = "employee_device_id";


    String KEY_REQ_CUSTOMER_NAME = "customer_name";
    String KEY_REQ_CUSTOMER_STREET = "customer_address_line1";
    String KEY_REQ_CUSTOMER_SUBURB_ID = "customer_suburb_id";
    String KEY_REQ_CUSTOMER_POSTCODE = "customer_zip";
    String KEY_REQ_IS_HOME = "is_home";

    String KEY_REQ_ORDER_SUBURB_ID = "order_suburb_id";

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
    String KEY_RES_SUBURB_NAME = "suburb_name";
    String KEY_RES_CATEGORY_LIST = "category_list";

    String KEY_RES_ORDER_LIST = "order_list";
    String KEY_RES_ORDER_NAME = "order_number";
    String KEY_RES_ORDER_SLOT_ID = "order_slot_id";
    String KEY_RES_SHIPPING_ADDRESS_ID = "shipping_address_id";
    String KEY_RES_TOTAL_ID = "total";
    String KEY_RES_STATUS_ID = "status";
    String KEY_RES_ADDRESS1_ID = "address1";
    String KEY_RES_DELIVERY_DATR = "delivery_date";
    String KEY_RES_DELIVERY_TIME = "delivery_time";


}
