package com.shoppin.customer.model;

/**
 * Created by ubuntu on 12/8/16.
 */

public class Suburb {
    public String suburb_id;
    public String suburb_name;
    public boolean selected;

    @Override
    public String toString() {
        return suburb_name;
    }
}
