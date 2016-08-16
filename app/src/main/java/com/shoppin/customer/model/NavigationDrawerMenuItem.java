package com.shoppin.customer.model;

/**
 * Model class for navigation left side drawer
 *
 * @author chitrang
 */
public class NavigationDrawerMenuItem {
    public String menuName;
    public int menuTagId;
    public int menuIcon;

    public NavigationDrawerMenuItem(String menuName, int menuTagId, int menuIcon) {
        this.menuName = menuName;
        this.menuTagId = menuTagId;
        this.menuIcon = menuIcon;
    }
}
