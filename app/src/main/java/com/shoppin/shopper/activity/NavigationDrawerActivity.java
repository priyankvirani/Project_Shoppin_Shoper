package com.shoppin.shopper.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shoppin.shopper.R;
import com.shoppin.shopper.adapter.NavigationDrawerMenuAdapter;
import com.shoppin.shopper.fragment.BaseFragment;
import com.shoppin.shopper.fragment.IUpdateFragment;
import com.shoppin.shopper.fragment.MyAccountFragment;
import com.shoppin.shopper.fragment.OrderHistoryFragment;
import com.shoppin.shopper.fragment.OrderOngoingFragment;
import com.shoppin.shopper.fragment.OrderRequestFragment;
import com.shoppin.shopper.model.NavigationDrawerMenu;
import com.shoppin.shopper.network.IWebService;
import com.shoppin.shopper.paymentexpress.GenerateRequest;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.shoppin.shopper.utils.IConstants.IDrawerMenu;

public class NavigationDrawerActivity extends BaseActivity {

    private static final String TAG = NavigationDrawerActivity.class.getSimpleName();
    public static String IS_REQUEST_NOTIFICATION = "order.request";
    private boolean isRequestNotification = false;

    @BindView(R.id.txtFragmentTitle)
    public TextView txtFragmentTitle;

    @BindView(R.id.llvContent)
    LinearLayout llvContent;

    /**
     * Container for all fragments
     */
    Fragment content;

    /**
     * For tool bar and left drawer
     */
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.recyclerListNavigationDrawer)
    RecyclerView recyclerListNavigationDrawer;
    /**
     * Basically to change title in tool bar.
     * <p/>
     * First get the current fragment and then calling its onStart() method to
     * set title.
     */
    FragmentManager.OnBackStackChangedListener onBackStackChangedListener = new FragmentManager.OnBackStackChangedListener() {

        @Override
        public void onBackStackChanged() {
            // TODO Auto-generated method stub
            // Update your UI here.
            Log.i(TAG, "OnBackStackChangedListener");
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment currentFragment = fragmentManager
                    .findFragmentById(R.id.contentFrame);
            if (currentFragment != null
                    && (currentFragment instanceof IUpdateFragment)) {
                Log.e(TAG, currentFragment.getClass().getSimpleName());
                ((BaseFragment) currentFragment).updateFragment();
            }
        }
    };
    private ActionBarDrawerToggle drawerToggle;
    private ArrayList<NavigationDrawerMenu> navigationDrawerMenuArrayList;
    public NavigationDrawerMenuAdapter drawerMenuAdapter;
    /**
     * When user select option from navigation drawer remove all previous
     * frgaments
     */
    private boolean isNavMenuchange = false;
    /**
     * Menu drawer item click listener to set respective fragment
     */


    NavigationDrawerMenuAdapter.OnItemClickListener leftDrawerListItemClickListener = new NavigationDrawerMenuAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            if (view != null && view.getTag() != null) {
                Log.d(TAG, "menuName = " + view.getTag());

                int menuTagId = (int) view.getTag();
                Fragment newContent = null;
                switch (menuTagId) {


                    case IDrawerMenu.ONGOING_ORDERS_ID:
                        newContent = new OrderOngoingFragment();
                        break;
                    case IDrawerMenu.ORDER_REQUEST_ID:
                        newContent = new OrderRequestFragment();
                        break;
                    case IDrawerMenu.ORDER_HISTORY_ID:
                        newContent = new OrderHistoryFragment();
                        break;
                    case IDrawerMenu.MY_PROFILE_ID:
                        newContent = new MyAccountFragment();
                        break;
                }
                if (newContent != null) {
                    isNavMenuchange = true;
                    switchContent(newContent);
                }
            }
        }
    };
    /**
     * For double back exit functionality
     */
    private boolean doubleBackToExitPressedOnce;

    /**
     * Set title in toolbar
     */
    public void mSetTitle(String toolbarTitle) {
        txtFragmentTitle.setText(toolbarTitle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (getIntent() != null) {
            isRequestNotification = intent.getBooleanExtra(IS_REQUEST_NOTIFICATION, false);
        }
        //Log.d(TAG, "suburb_id = " + DBAdapter.getMapKeyValueString(NavigationDrawerActivity.this, IMap.SUBURB_ID));
        //Log.d(TAG, "suburb_name = " + DBAdapter.getMapKeyValueString(NavigationDrawerActivity.this, IMap.SUBURB_NAME));

        if (toolbar != null) {
//            toolbar.setNavigationIcon(R.drawable.menu_icon);
//            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.menu_icon));
            setSupportActionBar(toolbar);
            //getSupportActionBar().setTitle("");
        }
        initDrawer();

        // For the first time load it will display
        // Product fragment as default
        if (content == null) {
            Log.i(TAG, "content is null");
            isNavMenuchange = true;
            if (isRequestNotification) {
                switchContent(new OrderRequestFragment());
            } else {
                switchContent(new OrderOngoingFragment());
            }

            // switchContent(new ProductDetailFragment());
        }
        getSupportFragmentManager().addOnBackStackChangedListener(
                onBackStackChangedListener);
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_app_global, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_search_product) {
//            return true;
//        } else if (id == R.id.action_view_cart) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @OnClick(R.id.imgSearch)
    public void searchProduct() {


        Toast.makeText(NavigationDrawerActivity.this, "Under Devlopment", Toast.LENGTH_SHORT).show();

        GenerateRequest generateRequest = new GenerateRequest();
        generateRequest.setPxPayUserId(getResources().getString(R.string.pxpay_userid));
        generateRequest.setPxPayKey(getResources().getString(R.string.pxpay_key));
        generateRequest.setAmountInput("1.00");
        generateRequest.setCurrencyInput(getResources().getString(R.string.pxpay_currency_input));
        generateRequest.setEmailAddress("name@name.com");
        generateRequest.setMerchantReference("Purchase Example");
        generateRequest.setTxnData1("Name");
        generateRequest.setTxnData2("Mobile Number");
        generateRequest.setTxnData3("Address");
        generateRequest.setTxnType(getResources().getString(R.string.pxpay_txt_type));
        generateRequest.setTxnId("TxnID");
        generateRequest.setBillingId("BillingId");
        generateRequest.setEnableAddBillCard("EnableAddBillCard");
        generateRequest.setUrlSuccess(IWebService.ON_TRANSACTION_SUCCESS);
        generateRequest.setUrlFail(IWebService.ON_TRANSACTION_FAIL);
        generateRequest.setOpt("Opt");
        generateRequest.setXml("Xml");


//        PxPay.GenerateRequest(getResources().getString(R.string.pxpay_userid)
//                , getResources().getString(R.string.pxpay_key),
//                generateRequest, IWebService.TRANSACTION_REQUEST_TEST,
//                NavigationDrawerActivity.this);

//        isNavMenuchange = true;
//        switchContent(new HomeFragment());
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.cancelAll();
    }


    private void setMenuAdapter() {

        navigationDrawerMenuArrayList = new ArrayList<>();

        navigationDrawerMenuArrayList.add(new NavigationDrawerMenu(IDrawerMenu.ONGOING_ORDERS,
                IDrawerMenu.ONGOING_ORDERS_ID, R.drawable.user));
        navigationDrawerMenuArrayList.add(new NavigationDrawerMenu(IDrawerMenu.ORDER_REQUEST,
                IDrawerMenu.ORDER_REQUEST_ID, R.drawable.user));
        navigationDrawerMenuArrayList.add(new NavigationDrawerMenu(IDrawerMenu.ORDER_HISTORY,
                IDrawerMenu.ORDER_HISTORY_ID, R.drawable.user));
        navigationDrawerMenuArrayList.add(new NavigationDrawerMenu(IDrawerMenu.MY_PROFILE,
                IDrawerMenu.MY_PROFILE_ID, R.drawable.user));


    }

    private void initDrawer() {
        setMenuAdapter();
        drawerMenuAdapter = new NavigationDrawerMenuAdapter(navigationDrawerMenuArrayList);
        drawerMenuAdapter.setOnItemClickListener(leftDrawerListItemClickListener);
        recyclerListNavigationDrawer.setLayoutManager(new LinearLayoutManager(NavigationDrawerActivity.this));
        recyclerListNavigationDrawer.setAdapter(drawerMenuAdapter);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Log.e(TAG, "onDrawerClosed");
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Log.e(TAG, "onDrawerOpened");
            }
        };
        drawerToggle.setDrawerIndicatorEnabled(false);
        drawerToggle
                .setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e(TAG, "setToolbarNavigationClickListener");
                        toggleLeftDrawer();
                    }
                });

        drawerLayout.setDrawerListener(drawerToggle);
    }

    private void hideDrawersAndShowContent() {
        drawerLayout.closeDrawer(Gravity.LEFT);
    }

    @OnClick(R.id.imgToggleDrawer)
    public void toggleLeftDrawer() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            drawerLayout.openDrawer(Gravity.LEFT);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * For switching fragments
     **/
    public void switchContent(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
//        To hide keyboard on fragment change
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

        content = fragment;
        String fragmentTag = fragment.getClass().getSimpleName();

        // if (backStateName.compareTo(CategoryListFragment.class.getSimpleName()
        // .toString()) == 0
        // || backStateName.compareTo(VideoListFragment.class.getSimpleName()
        // .toString()) == 0
        // || backStateName.compareTo(SettingsFragment.class.getSimpleName()
        // .toString()) == 0) {
        if (isNavMenuchange) {
            isNavMenuchange = false;
            // Remove all inner fragments of previous section
            boolean fragmentPopped = manager.popBackStackImmediate(null,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
            Log.i(TAG, "fragmentPopped = " + fragmentPopped);
        }

        Log.i(TAG, "fragmentTag = " + fragmentTag);
        FragmentTransaction ft = manager.beginTransaction();
        //ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
//        ft.setCustomAnimations(R.anim.pop_enter, R.anim.pop_exit, R.anim.enter,
//                R.anim.exit);
        Fragment tmpMyFragment = manager.findFragmentById(R.id.contentFrame);
        if (tmpMyFragment != null) {
            ft.hide(tmpMyFragment);
        }
        ft.add(R.id.contentFrame, fragment, fragmentTag);
        ft.addToBackStack(fragmentTag);
        ft.commit();

        hideDrawersAndShowContent();
    }

    @Override
    public void onBackPressed() {

        Log.i(TAG, "Back");

        FragmentManager manager = getSupportFragmentManager();
        if (manager != null) {
            int backStackEntryCount = manager.getBackStackEntryCount();
            Log.i(TAG, " getSupportFragmentManager backStackEntryCount = "
                    + backStackEntryCount);
            // hide left drawer
            if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                hideDrawersAndShowContent();
            }
            // logic for two time back press exit app
            else if (backStackEntryCount == 1) {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    finish();
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                if (getApplicationContext() == null) {
                    return;
                } else {
                    Toast.makeText(NavigationDrawerActivity.this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                }
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            } else {
                super.onBackPressed();
            }
        }
    }

    /**
     * Set title
     **/

    public void setToolbarTitle(String toolbarTitle) {
        txtFragmentTitle.setText(toolbarTitle);
    }

}
