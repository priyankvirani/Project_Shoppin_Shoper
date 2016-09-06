package com.shoppin.shoper.model;

import java.io.Serializable;

/**
 * Created by ubuntu on 4/8/16.
 */

public class DemoOrderOngoing implements Serializable {


    public String order_number;

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }


}
