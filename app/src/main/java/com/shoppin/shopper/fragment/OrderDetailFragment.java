package com.shoppin.shopper.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shoppin.shopper.R;
import com.shoppin.shopper.activity.NavigationDrawerActivity;
import com.shoppin.shopper.adapter.ProductDetailsAdapter;
import com.shoppin.shopper.adapter.SelectionAdapter;
import com.shoppin.shopper.database.DBAdapter;
import com.shoppin.shopper.database.IDatabase;
import com.shoppin.shopper.model.Product;
import com.shoppin.shopper.model.StatusOptionValue;
import com.shoppin.shopper.network.DataRequest;
import com.shoppin.shopper.network.IWebService;
import com.shoppin.shopper.utils.IConstants;
import com.shoppin.shopper.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by ubuntu on 15/8/16.
 */

public class OrderDetailFragment extends BaseFragment {

    private static final String TAG = OrderDetailFragment.class.getSimpleName();
    public static final String ORDER_NUMBER = "order_number";
    public static final String ISHISTORY = "ishistory";


    public static OrderDetailFragment newInstance(String order_number, boolean isHistory) {
        OrderDetailFragment orderDetailFragment = new OrderDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ORDER_NUMBER, order_number);
        bundle.putBoolean(ISHISTORY, isHistory);
        orderDetailFragment.setArguments(bundle);
        return orderDetailFragment;
    }


    private ProductDetailsAdapter productDetailsAdapter;
    private ArrayList<Product> productArrayList;
    private ArrayList<StatusOptionValue> statusOptionValueArrayList;
    private SelectionAdapter<StatusOptionValue> filterStatusAdapter;

    private String order_number;
    private int order_status;
    private boolean isHistory = false;

    @BindView(R.id.rlvGlobalProgressbar)
    RelativeLayout rlvGlobalProgressbar;

    @BindView(R.id.recyclerListOrderDetails)
    RecyclerView recyclerListOrderDetails;

    @BindView(R.id.rlOrderDetails)
    RelativeLayout rlOrderDetails;

    @BindView(R.id.txtStatusOption)
    TextView txtStatusOption;


    @BindView(R.id.txtOrderNumber)
    TextView txtOrderNumber;

    @BindView(R.id.txtStreetName)
    TextView txtStreetName;

    @BindView(R.id.txtSuburb)
    TextView txtSuburb;

    @BindView(R.id.txtOrderDate)
    TextView txtOrderDate;

    @BindView(R.id.txtOrderTime)
    TextView txtOrderTime;

    @BindView(R.id.txtphoneNumber)
    TextView txtphoneNumber;

    @BindView(R.id.txtOrderPrice)
    TextView txtOrderPrice;

    @BindView(R.id.txtShippingDate)
    TextView txtShippingDate;

    @BindView(R.id.txtShippingTime)
    TextView txtShippingTime;

    @BindView(R.id.txtCustomerName)
    TextView txtCustomerName;

    @BindView(R.id.txtItemCount)
    TextView txtItemCount;

    @BindView(R.id.txtPreferredStore)
    TextView txtPreferredStore;

    @BindView(R.id.txtStoreAddress)
    TextView txtStoreAddress;


    @BindView(R.id.lltAccepted)
    LinearLayout lltAccepted;
    @BindView(R.id.lltPurchasing)
    LinearLayout lltPurchasing;
    @BindView(R.id.lltShipping)
    LinearLayout lltShipping;
    @BindView(R.id.lltCompleted)
    LinearLayout lltCompleted;

    @BindView(R.id.imgAccepted)
    ImageView imgAccepted;
    @BindView(R.id.imgPurchasing)
    ImageView imgPurchasing;
    @BindView(R.id.imgShipping)
    ImageView imgShipping;
    @BindView(R.id.imgCompleted)
    ImageView imgCompleted;

    @BindView(R.id.txtAccepted)
    TextView txtAccepted;
    @BindView(R.id.txtxPurchasing)
    TextView txtxPurchasing;
    @BindView(R.id.txtShipping)
    TextView txtShipping;
    @BindView(R.id.txtCompleted)
    TextView txtCompleted;

    private boolean isAccepted = false;
    private boolean isPurchasing = false;
    private boolean isShipping = false;
    private boolean isCompleted = false;

    private boolean isOrderStatusCompleted = true;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_order_details, container, false);
        ButterKnife.bind(this, layoutView);

        productArrayList = new ArrayList<>();
        statusOptionValueArrayList = new ArrayList<>();
        productDetailsAdapter = new ProductDetailsAdapter(getActivity(),
                productArrayList, OrderDetailFragment.this);

        recyclerListOrderDetails.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerListOrderDetails.setNestedScrollingEnabled(false);
        recyclerListOrderDetails.setAdapter(productDetailsAdapter);

        productDetailsAdapter.setOnStatusChangeListener(new ProductDetailsAdapter.OnStatusChangeListener() {
            @Override
            public void onStatusChange(int position) {

                //Log.e(TAG, "productAvailability : " + productArrayList.get(position).productAvailability);
                if (productDetailsAdapter != null) {

                    if (productArrayList.get(position).productAvailability == IWebService.KEY_REQ_STATUS_PRODUCT_NOT_AVAILABLE) {

                        showAlertProductEmployeeComment(getActivity(), position);

                    } else if (productArrayList.get(position).productAvailability == IWebService.KEY_REQ_STATUS_PRODUCT_AVAILABLE) {

                        sendUpdateOrderItemAvailibility(position, IWebService.KEY_REQ_NULL);


                    } else {
                        sendUpdateOrderItemAvailibility(position, IWebService.KEY_REQ_NULL);


                    }
                    //Log.e(TAG, "Value : " + productArrayList.get(position).productAvailability);

                }
            }
        });


        if (getArguments() != null) {
            order_number = getArguments().getString(ORDER_NUMBER);
            isHistory = getArguments().getBoolean(ISHISTORY);
            //Log.e(TAG, "Order Number :  -  " + order_number);

        }

        getOrderDetailData();

        txtStatusOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (order_status == IWebService.KEY_REQ_STATUS_PLACED) {
                    showAlertStatusOption();

                } else {
                    if (checkProductStatus()) {
                        if (isOrderStatusCompleted) {
                            showAlertStatusOption();
                        } else {
                            Utils.showAlert(getActivity(), null, getActivity().getString(R.string.error_not_allowed));
                        }
                    } else {
                        Utils.showAlert(getActivity(), null, getActivity().getString(R.string.error_update_product_status));
                    }
                }

            }
        });


        txtphoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"
                            + txtphoneNumber.getText().toString()));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "SIM ERROR",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });


        return layoutView;
    }


    public void getOrderDetailData() {
        try {

            JSONObject orderDetailsParam = new JSONObject();
            orderDetailsParam.put(IWebService.KEY_REQ_ORDER_NUMBER, order_number);

            DataRequest getOrderDetailsRequest = new DataRequest(getActivity());
            getOrderDetailsRequest.execute(IWebService.ORDER_DETAILS, orderDetailsParam.toString(), new DataRequest.CallBack() {
                        public void onPreExecute() {
                            rlvGlobalProgressbar.setVisibility(View.VISIBLE);
                            rlOrderDetails.setVisibility(View.GONE);
                            productArrayList.clear();
                            productDetailsAdapter.notifyDataSetChanged();

                        }

                        public void onPostExecute(String response) {
                            rlvGlobalProgressbar.setVisibility(View.GONE);
                            rlOrderDetails.setVisibility(View.VISIBLE);
                            try {

                                if (!DataRequest.hasError(getActivity(), response, true)) {

                                    JSONObject dataJObject = DataRequest.getJObjWebdata(response);

                                    Gson gson = new Gson();

                                    txtOrderNumber.setText(dataJObject.getString(IWebService.KEY_REQ_ORDER_NUMBER));
                                    txtStreetName.setText(dataJObject.getString(IWebService.KEY_RES_ADDRESS1));
                                    txtSuburb.setText(dataJObject.getString(IWebService.KEY_RES_SUBURB_NAME) + ", " + dataJObject.getString(IWebService.KEY_RES_ZIP));
                                    txtphoneNumber.setText(dataJObject.getString(IWebService.KEY_RES_CUSTOMER_MOBILE));
                                    txtOrderDate.setText(dataJObject.getString(IWebService.KEY_RES_DELIVERY_DATE));
                                    txtOrderTime.setText(dataJObject.getString(IWebService.KEY_RES_DELIVERY_TIME));
                                    txtOrderPrice.setText(getActivity().getResources().getString(R.string.dollar_sign) + dataJObject.getString(IWebService.KEY_RES_TOTAL));
                                    txtCustomerName.setText(dataJObject.getString(IWebService.KEY_RES_CUSTOMER_NAME));
                                    txtItemCount.setText(dataJObject.getString(IWebService.KEY_RES_ITEM_COUNT));

                                    if (Integer.valueOf(dataJObject.getString(IWebService.KEY_RES_PREFERRED_STORE_ID)) == IWebService.KEY_REQ_PREFERRED_STORE_ID) {
                                        txtPreferredStore.setVisibility(View.GONE);
                                        txtStoreAddress.setVisibility(View.GONE);
                                    } else {
                                        txtPreferredStore.setText(Html.fromHtml("<b>" + getActivity().getString(R.string.preferred_store) + "</b>" + " " + dataJObject.getString(IWebService.KEY_RES_STORE_NAME)));
                                        txtStoreAddress.setText(dataJObject.getString(IWebService.KEY_RES_STORE_ADDRESS));
                                    }
                                    order_status = Integer.valueOf(dataJObject.getString(IWebService.KEY_RES_STATUS));

                                    if (order_status == IWebService.KEY_REQ_STATUS_PLACED) {
                                        txtStatusOption.setText(getResources().getString(R.string.order_select_status));
                                    } else {
                                        txtStatusOption.setText(dataJObject.getString(IWebService.KEY_RES_STATUS_LABEL));

                                    }
                                    if (isHistory) {
                                        if (Integer.valueOf(dataJObject.getString(IWebService.KEY_REQ_STATUS)) != IWebService.KEY_REQ_STATUS_CANCELLED) {
                                            txtShippingDate.setText(dataJObject.getString(IWebService.KEY_RES_SHIPPING_DATE));
                                            txtShippingTime.setText(dataJObject.getString(IWebService.KEY_RES_SHIPPING_TIME));
                                            txtShippingDate.setVisibility(View.VISIBLE);
                                            txtShippingTime.setVisibility(View.VISIBLE);
                                        } else {
                                            txtShippingDate.setVisibility(View.INVISIBLE);
                                            txtShippingTime.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getActivity()

                                                    , R.drawable.cancel), null);
                                            txtShippingTime.setText(getActivity().getString(R.string.order_status_cancelled));
                                            txtShippingTime.setTextColor(getActivity().getResources().getColor(R.color.red));
                                        }
                                    } else {
                                        txtShippingDate.setVisibility(View.GONE);
                                        txtShippingTime.setVisibility(View.GONE);
                                    }

                                    if (Integer.valueOf(dataJObject.getString(IWebService.KEY_RES_STATUS)) ==
                                            IWebService.KEY_REQ_STATUS_ON_HOLD || Integer.valueOf(dataJObject.getString(IWebService.KEY_RES_STATUS)) ==
                                            IWebService.KEY_REQ_STATUS_CANCELLED) {
                                        setOrderStatus(Integer.valueOf(dataJObject.getString(IWebService.KET_RES_PREV_STATUS)));
                                    } else {
                                        setOrderStatus(Integer.valueOf(dataJObject.getString(IWebService.KEY_RES_STATUS)));
                                    }

                                    if (Integer.valueOf(dataJObject.getString(IWebService.KEY_REQ_STATUS)) == IWebService.KEY_REQ_STATUS_COMPLETED
                                            || Integer.valueOf(dataJObject.getString(IWebService.KEY_REQ_STATUS)) == IWebService.KEY_REQ_STATUS_CANCELLED) {
                                        productDetailsAdapter.setOrderStatusCompleted(false);
                                        isOrderStatusCompleted = false;
                                    }


//                            Log.e(TAG, "Order status :  " + Integer.valueOf(dataJObject.getString(IWebService.KEY_RES_STATUS)));
//                            Log.e(TAG, "tmpProductArrayList Size :  " + productArrayList.size());
//                            Log.e(TAG, "tmpStatusOptionArrayList Size :  " + productArrayList.size());

                                    ArrayList<Product> tmpProductArrayList = gson.fromJson(
                                            dataJObject.getJSONArray(
                                                    IWebService.KEY_RES_PRODUCT_LIST)
                                                    .toString(),
                                            new TypeToken<ArrayList<Product>>() {
                                            }.getType());


                                    if (tmpProductArrayList != null) {
                                        productArrayList.addAll(tmpProductArrayList);
                                        productDetailsAdapter.notifyDataSetChanged();
                                        //Log.e(TAG, "tmpProductArrayList Size :  " + productArrayList.size());

                                    }

                                    ArrayList<StatusOptionValue> tmpStatusOptionArrayList = gson.fromJson(
                                            dataJObject.getJSONArray(
                                                    IWebService.KEY_RES_STATUS_LIST)
                                                    .toString(),
                                            new TypeToken<ArrayList<StatusOptionValue>>() {
                                            }.getType());

                                    if (tmpStatusOptionArrayList != null) {
                                        statusOptionValueArrayList.addAll(tmpStatusOptionArrayList);
                                        //Log.e(TAG, "tmpStatusOptionArrayList Size :  " + statusOptionValueArrayList.size());

                                    }


                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }

            );

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void setOrderStatus(int statusCode) {

        if (statusCode != IWebService.KEY_REQ_STATUS_PLACED) {

            if (statusCode == IWebService.KEY_REQ_STATUS_ACCEPTED) {

                lltAccepted.setBackgroundColor(getResources().getColor(R.color.order_datails_accepted));
                imgAccepted.setImageResource(R.drawable.accepted);
                txtAccepted.setTextColor(getResources().getColor(R.color.white));
                lltAccepted.setClickable(false);

                isAccepted = false;
                isPurchasing = true;
                isShipping = false;
                isCompleted = false;


            } else if (statusCode == IWebService.KEY_REQ_STATUS_PUECHASING) {

                lltAccepted.setBackgroundColor(getResources().getColor(R.color.order_datails_accepted));
                imgAccepted.setImageResource(R.drawable.accepted);
                txtAccepted.setTextColor(getResources().getColor(R.color.white));
                lltAccepted.setClickable(false);

                lltPurchasing.setBackgroundColor(getResources().getColor(R.color.order_datails_purchasing));
                imgPurchasing.setImageResource(R.drawable.purchasing);
                txtxPurchasing.setTextColor(getResources().getColor(R.color.white));
                lltPurchasing.setClickable(false);

                isAccepted = false;
                isPurchasing = false;
                isShipping = true;
                isCompleted = false;

            } else if (statusCode == IWebService.KEY_REQ_STATUS_SHIPPING) {
                lltAccepted.setBackgroundColor(getResources().getColor(R.color.order_datails_accepted));
                imgAccepted.setImageResource(R.drawable.accepted);
                txtAccepted.setTextColor(getResources().getColor(R.color.white));
                lltAccepted.setClickable(false);

                lltPurchasing.setBackgroundColor(getResources().getColor(R.color.order_datails_purchasing));
                imgPurchasing.setImageResource(R.drawable.purchasing);
                txtxPurchasing.setTextColor(getResources().getColor(R.color.white));
                lltPurchasing.setClickable(false);

                lltShipping.setBackgroundColor(getResources().getColor(R.color.order_datails_shiping));
                imgShipping.setImageResource(R.drawable.shipping);
                txtShipping.setTextColor(getResources().getColor(R.color.white));
                lltShipping.setClickable(false);

                isAccepted = false;
                isPurchasing = false;
                isShipping = false;
                isCompleted = true;

            } else if (statusCode == IWebService.KEY_REQ_STATUS_COMPLETED) {
                lltAccepted.setBackgroundColor(getResources().getColor(R.color.order_datails_accepted));
                imgAccepted.setImageResource(R.drawable.accepted);
                txtAccepted.setTextColor(getResources().getColor(R.color.white));
                lltAccepted.setClickable(false);

                lltPurchasing.setBackgroundColor(getResources().getColor(R.color.order_datails_purchasing));
                imgPurchasing.setImageResource(R.drawable.purchasing);
                txtxPurchasing.setTextColor(getResources().getColor(R.color.white));
                lltPurchasing.setClickable(false);

                lltShipping.setBackgroundColor(getResources().getColor(R.color.order_datails_shiping));
                imgShipping.setImageResource(R.drawable.shipping);
                txtShipping.setTextColor(getResources().getColor(R.color.white));
                lltShipping.setClickable(false);

                lltCompleted.setBackgroundColor(getResources().getColor(R.color.order_datails_completed));
                imgCompleted.setImageResource(R.drawable.completed_white);
                txtCompleted.setTextColor(getResources().getColor(R.color.white));
                lltCompleted.setClickable(false);

                isAccepted = false;
                isPurchasing = false;
                isShipping = false;
                isCompleted = false;

            }
        } else {
            isAccepted = true;
            isPurchasing = false;
            isShipping = false;
            isCompleted = false;

        }
    }

    public void sendOrderStatus(final int position, String comments) {


        try {

            JSONObject orderstatusParam = new JSONObject();
            orderstatusParam.put(IWebService.KEY_REQ_ORDER_NUMBER, order_number);
            orderstatusParam.put(IWebService.KEY_REQ_EMPLOYEE_ID, DBAdapter.getMapKeyValueString(getActivity(), IDatabase.IMap.KEY_EMPLOYEE_ID));
            orderstatusParam.put(IWebService.KEY_REQ_STATUS, statusOptionValueArrayList.get(position).status);
            orderstatusParam.put(IWebService.KEY_REQ_PRODUCT_COMMENTS, comments);


            DataRequest orderStatusDataRequest = new DataRequest(getActivity());
            orderStatusDataRequest.execute(IWebService.ORDER_ACTION, orderstatusParam.toString(), new DataRequest.CallBack() {
                public void onPreExecute() {

                    rlvGlobalProgressbar.setVisibility(View.VISIBLE);


                }

                public void onPostExecute(String response) {
                    rlvGlobalProgressbar.setVisibility(View.GONE);

                    try {

                        if (!DataRequest.hasError(getActivity(), response, true)) {

                            JSONObject dataJObject = DataRequest.getJObjWebdata(response);

                            for (int i = 0; i < statusOptionValueArrayList
                                    .size(); i++) {
                                statusOptionValueArrayList.get(i).isSelected = false;
                            }
                            Intent intent = new Intent(IConstants.UPDATE);
                            LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
                            broadcastManager.sendBroadcast(intent);

                            order_status = Integer.valueOf(dataJObject.getString(IWebService.KEY_RES_STATUS));
                            statusOptionValueArrayList.get(position).isSelected = true;
                            txtStatusOption.setText(statusOptionValueArrayList.get(position).statusLabel);
                            filterStatusAdapter.notifyDataSetChanged();

                            setOrderStatus(Integer.valueOf(dataJObject.getString(IWebService.KEY_RES_STATUS)));


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showAlertProductEmployeeComment(Context context, final int position) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.dialog_employe_comments, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        final AlertDialog alertDialog = dialogBuilder.create();

        final EditText etxproductComments = (EditText) dialogView.findViewById(R.id.etxCommentDesrscriptionssc);


        dialogView.findViewById(R.id.txtCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (productArrayList.get(position).productAvailability == 1) {
                    productArrayList.get(position).productAvailability = IWebService.KEY_REQ_STATUS_PRODUCT_NOT_AVAILABLE;
                } else {
                    productArrayList.get(position).productAvailability = IWebService.KEY_REQ_STATUS_PRODUCT_AVAILABLE;
                }

                productDetailsAdapter.notifyDataSetChanged();
                alertDialog.dismiss();


            }
        });

        dialogView.findViewById(R.id.txtSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!Utils.isNullOrEmpty(etxproductComments.getText().toString())) {
                    sendUpdateOrderItemAvailibility(position, etxproductComments.getText().toString());
                    //statusDrawable(productArrayList.get(position).productAvailability, imgproductStatus);
                    productDetailsAdapter.notifyDataSetChanged();
                    alertDialog.dismiss();
                } else {
                    etxproductComments.setError(getActivity().getString(R.string.error_required));
                }


            }
        });

        alertDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        alertDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        alertDialog.show();


    }

    public void showAlertOrderEmployeeComment(Context context, final int position) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.dialog_employe_comments, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        final AlertDialog alertDialog = dialogBuilder.create();

        final EditText etxproductComments = (EditText) dialogView.findViewById(R.id.etxCommentDesrscriptionssc);


        dialogView.findViewById(R.id.txtCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();


            }
        });

        dialogView.findViewById(R.id.txtSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!Utils.isNullOrEmpty(etxproductComments.getText().toString())) {
                    sendOrderStatus(position, etxproductComments.getText().toString());
                    alertDialog.dismiss();
                } else {
                    etxproductComments.setError(getActivity().getString(R.string.error_required));
                }


            }
        });

        alertDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        alertDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        alertDialog.show();


    }

    public void sendUpdateOrderItemAvailibility(final int position, String comments) {

        try {

            JSONObject productstatusParam = new JSONObject();
            productstatusParam.put(IWebService.KEY_REQ_ORDER_ITEM_ID, productArrayList.get(position).productItemId);
            productstatusParam.put(IWebService.KEY_REQ_SET_VALUE, productArrayList.get(position).productAvailability);
            productstatusParam.put(IWebService.KEY_REQ_PRODUCT_COMMENTS, comments);


            DataRequest signinDataRequest = new DataRequest(getActivity());
            signinDataRequest.execute(IWebService.UPDATE_ORDER_ITEM_AVAILABILITY, productstatusParam.toString(), new DataRequest.CallBack() {
                public void onPreExecute() {
                    rlvGlobalProgressbar.setVisibility(View.VISIBLE);

                }

                public void onPostExecute(String response) {
                    rlvGlobalProgressbar.setVisibility(View.GONE);

                    try {

                        if (!DataRequest.hasError(getActivity(), response, false)) {

                            JSONObject dataJObject = DataRequest.getJObjWebdata(response);
                            productArrayList.get(position).productAvailability = Integer.valueOf(dataJObject.getString(IWebService.KEY_REQ_SET_VALUE));
                            //Log.e(TAG, "Status Name : " + dataJObject.getString(IWebService.KEY_REQ_SET_VALUE));
                            productDetailsAdapter.notifyDataSetChanged();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private boolean checkProductStatus() {
        boolean isProductAvailable = false;
        for (int i = 0; i < productArrayList.size(); i++) {
            if (productArrayList.get(i).productAvailability == IWebService.KEY_REQ_STATUS_PRODUCT_AVAILABLE
                    || productArrayList.get(i).productAvailability == IWebService.KEY_REQ_STATUS_PRODUCT_NOT_AVAILABLE) {
                Log.e(TAG, "Product avalibillity : " + productArrayList.get(i).productAvailability);
                isProductAvailable = true;
                break;
            }


        }
        return isProductAvailable;

    }

    private void showAlertStatusOption() {

        RecyclerView recyclerFilter;

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                getActivity());
        LayoutInflater inflater = getLayoutInflater(getArguments());
        View dialogView = inflater.inflate(R.layout.dialog_selection, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();

        ((TextView) dialogView.findViewById(R.id.txtSelectionType))
                .setText(getActivity().getResources().getString(R.string.order_status));
        recyclerFilter = (RecyclerView) dialogView.findViewById(R.id.recyclerFilter);
        filterStatusAdapter = new SelectionAdapter<>(statusOptionValueArrayList);

        filterStatusAdapter
                .setBindAdapterInterface(new SelectionAdapter.IBindAdapterValues<StatusOptionValue>() {
                    @Override
                    public void bindValues(SelectionAdapter.MyViewHolder holder, final int position) {
                        // TODO Auto-generated method stub

                        holder.txtSelectionValue.setText(statusOptionValueArrayList
                                .get(position).statusLabel);
                        holder.txtSelectionValue.setChecked(statusOptionValueArrayList
                                .get(position).isSelected);
                        if (statusOptionValueArrayList.get(position).isSelected) {
                            holder.txtSelectionValue.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_theme_1));
                        } else {
                            holder.txtSelectionValue.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_black));
                        }
                        holder.txtSelectionValue
                                .setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View arg0) {
                                        // TODO Auto-generated method stub
                                        if (statusOptionValueArrayList.get(position).status == IWebService.KEY_REQ_STATUS_ON_HOLD ||
                                                statusOptionValueArrayList.get(position).status == IWebService.KEY_REQ_STATUS_CANCELLED) {
                                            showAlertOrderEmployeeComment(getActivity(), position);
                                        } else if (statusOptionValueArrayList.get(position).status == IWebService.KEY_REQ_STATUS_SHIPPING) {
                                            if (productCheckProductStatus()) {

                                                sendOrderStatus(position, IWebService.KEY_REQ_NULL);

                                            } else {

                                                Utils.showAlert(getActivity(), null, getActivity().getString(R.string.error_update_product_status));

                                            }
                                        } else {
                                            sendOrderStatus(position, IWebService.KEY_REQ_NULL);
                                        }

                                        alertDialog.dismiss();
                                    }
                                });

                    }
                });

        recyclerFilter.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerFilter.setAdapter(filterStatusAdapter);
        alertDialog.getWindow().setBackgroundDrawableResource(
                R.color.transparent);
//        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private boolean productCheckProductStatus() {
        for (int i = 0; i < productArrayList.size(); i++) {
            if (productArrayList.get(i).productAvailability == IWebService.KEY_REQ_STATUS_PRODUCT_NORMAL) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void updateFragment() {
        super.updateFragment();
        NavigationDrawerActivity navigationDrawerActivity = (NavigationDrawerActivity) getActivity();
        if (navigationDrawerActivity != null) {
            navigationDrawerActivity.setToolbarTitle(getActivity().getResources().getString(R.string.fragment_order_details));
        }
    }
}
