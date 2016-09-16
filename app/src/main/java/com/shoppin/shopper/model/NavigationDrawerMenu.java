package com.shoppin.shopper.model;

/**
 * Created by ubuntu on 10/9/16.
 */

public class NavigationDrawerMenu {

    public String menuName;
    public int menuTagId;
    public int menuIcon;

    public NavigationDrawerMenu(String menuName, int menuTagId, int menuIcon) {
        this.menuName = menuName;
        this.menuTagId = menuTagId;
        this.menuIcon = menuIcon;
    }
}
