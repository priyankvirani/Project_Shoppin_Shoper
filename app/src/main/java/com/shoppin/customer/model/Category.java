package com.shoppin.customer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ubuntu on 4/8/16.
 */

public class Category implements Serializable {


    public String cat_name;
    public String cat_id;
    @SerializedName("sub_category")
    public ArrayList<SubCategory> subCategoryArrayList;
    private boolean isCategoryExpand;

    public boolean isCategoryExpand() {
        return isCategoryExpand;
    }

    public void setCategoryExpand(boolean categoryExpand) {
        this.isCategoryExpand = categoryExpand;
    }

}
