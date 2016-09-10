package com.shoppin.shoper.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shoppin.shoper.R;
import com.shoppin.shoper.activity.NavigationDrawerActivity;
import com.shoppin.shoper.adapter.ProductDetailsAdapter;
import com.shoppin.shoper.adapter.SelectionAdapter;
import com.shoppin.shoper.database.DBAdapter;
import com.shoppin.shoper.database.IDatabase;
import com.shoppin.shoper.model.Product;
import com.shoppin.shoper.model.StatusOptionValue;
import com.shoppin.shoper.network.DataRequest;
import com.shoppin.shoper.network.IWebService;
import com.shoppin.shoper.utils.Utils;

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

    public static OrderDetailFragment newInstance(String order_number) {
        OrderDetailFragment orderDetailFragment = new OrderDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ORDER_NUMBER, order_number);
        orderDetailFragment.setArguments(bundle);
        return orderDetailFragment;
    }


    private ProductDetailsAdapter productDetailsAdapter;
    private ArrayList<Product> productArrayList;
    private ArrayList<StatusOptionValue> statusOptionValueArrayList;

    private String order_number;

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


    @BindView(R.id.lltAccpeted)
    LinearLayout lltAccpeted;
    @BindView(R.id.lltPurchasing)
    LinearLayout lltPurchasing;
    @BindView(R.id.lltShiping)
    LinearLayout lltShiping;
    @BindView(R.id.lltCompleted)
    LinearLayout lltCompleted;

    @BindView(R.id.imgAccpeted)
    ImageView imgAccpeted;
    @BindView(R.id.imgPurchasing)
    ImageView imgPurchasing;
    @BindView(R.id.imgShiping)
    ImageView imgShiping;
    @BindView(R.id.imgCompleted)
    ImageView imgCompleted;

    @BindView(R.id.txtAccepted)
    TextView txtAccepted;
    @BindView(R.id.txtxPurchasing)
    TextView txtxPurchasing;
    @BindView(R.id.txtShiping)
    TextView txtShiping;
    @BindView(R.id.txtCompleted)
    TextView txtCompleted;

    private boolean isAccpted = false;
    private boolean isPurchasing = false;
    private boolean isShiping = false;
    private boolean isCompleted = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        layoutView = inflater.inflate(R.layout.fragment_home, null);
        layoutView = inflater.inflate(R.layout.fragment_order_details, container, false);
        ButterKnife.bind(this, layoutView);

        productArrayList = new ArrayList<>();
        statusOptionValueArrayList = new ArrayList<>();
        LinearLayoutManager verticalLayoutManagaerdate
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        productDetailsAdapter = new ProductDetailsAdapter(getActivity(),
                productArrayList, OrderDetailFragment.this);
        recyclerListOrderDetails.setLayoutManager(verticalLayoutManagaerdate);
        recyclerListOrderDetails.setAdapter(productDetailsAdapter);

        productDetailsAdapter.setOnStatusChangeListener(new ProductDetailsAdapter.OnStatusChangeListener() {
            @Override
            public void onStatusChange(ImageView imgproductStatus, int position, String productItemID, int productAvailability, String comments) {

                Log.e(TAG, "productAvailability : " + productAvailability);
                if (productDetailsAdapter != null) {

                    if (productAvailability == IWebService.KEY_REQ_STATUS_PRODUCT_NOT_AVAILABLE) {

                        showAlertEmployeeComment(getActivity(), productItemID, productAvailability, imgproductStatus, position);

                    } else if (productAvailability == IWebService.KEY_REQ_STATUS_PRODUCT_AVAILABLE) {
                        sendUpdateOrderItemAvailibility(productItemID, productAvailability, IWebService.KEY_REQ_NULL);
                        statusDrawable(productAvailability, imgproductStatus);

                    } else {
                        sendUpdateOrderItemAvailibility(productItemID, productAvailability, IWebService.KEY_REQ_NULL);
                        statusDrawable(productAvailability, imgproductStatus);

                    }
                    Log.e(TAG, "Value : " + productArrayList.get(position).productAvailability);

                }
            }
        });


        if (getArguments() != null) {
            order_number = getArguments().getString(ORDER_NUMBER);

            Log.e(TAG, "Order Number :  -  " + order_number);

        }

        getOrderDetailData();

        txtStatusOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertStatusOption(statusOptionValueArrayList, txtStatusOption);
            }
        });

//        lltAccpeted.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (isAccpted) {
//                    lltAccpeted.setBackgroundColor(getResources().getColor(R.color.order_datails_accepted));
//                    imgAccpeted.setImageResource(R.drawable.accepted);
//                    txtAccepted.setTextColor(getResources().getColor(R.color.white));
//                    sendOrderStatus(order_number, IWebService.KEY_REQ_STATUS_ACCEPTED);
//                }
//
//
//            }
//        });
//        lltPurchasing.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isPurchasing) {
//                    lltPurchasing.setBackgroundColor(getResources().getColor(R.color.order_datails_purchasing));
//                    imgPurchasing.setImageResource(R.drawable.purchasing);
//                    txtxPurchasing.setTextColor(getResources().getColor(R.color.white));
//                    sendOrderStatus(order_number, IWebService.KEY_REQ_STATUS_PUECHASING);
//                }
//
//
//            }
//        });
//        lltShiping.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isShiping) {
//                    lltShiping.setBackgroundColor(getResources().getColor(R.color.order_datails_shiping));
//                    imgShiping.setImageResource(R.drawable.shipping);
//                    txtShiping.setTextColor(getResources().getColor(R.color.white));
//                    sendOrderStatus(order_number, IWebService.KEY_REQ_STATUS_SHIPING);
//                }
//            }
//        });
//        lltCompleted.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isCompleted) {
//                    lltCompleted.setBackgroundColor(getResources().getColor(R.color.order_datails_completed));
//                    imgCompleted.setImageResource(R.drawable.completed_white);
//                    txtCompleted.setTextColor(getResources().getColor(R.color.white));
//                    sendOrderStatus(order_number, IWebService.KEY_REQ_STATUS_COMPLETED);
//                }
//            }
//        });
//
//        txtphoneNumber.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_CALL);
//                intent.setData(Uri.parse("tel:"
//                        + txtphoneNumber.getText().toString()));
//                startActivity(intent);
//            }
//        });


        return layoutView;
    }







    public void getOrderDetailData() {
        try {

            JSONObject loginParam = new JSONObject();
            loginParam.put(IWebService.KEY_REQ_ORDER_NUMBER, order_number);

            DataRequest setdataRequest = new DataRequest(getActivity());
            setdataRequest.execute(IWebService.ORDER_DETAILS, loginParam.toString(), new DataRequest.CallBack() {
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
                            txtSuburb.setText(dataJObject.getString(IWebService.KEY_RES_SUBURB_NAME));
                            txtphoneNumber.setText(dataJObject.getString(IWebService.KEY_RES_CUSTOMER_MOBILE));
                            txtOrderDate.setText(dataJObject.getString(IWebService.KEY_RES_DELIVERY_DATR));
                            txtOrderTime.setText(dataJObject.getString(IWebService.KEY_RES_DELIVERY_TIME));
                            txtOrderPrice.setText(getActivity().getResources().getString(R.string.dollar_sign) + dataJObject.getString(IWebService.KEY_RES_TOTAL));
                            txtStatusOption.setText(dataJObject.getString(IWebService.KEY_RES_STATUS_LABEL));
                            orderstatus(Integer.valueOf(dataJObject.getString(IWebService.KEY_RES_STATUS)));

                            Log.e(TAG, "Order status :  " + Integer.valueOf(dataJObject.getString(IWebService.KEY_RES_STATUS)));
                            Log.e(TAG, "tmpProductArrayList Size :  " + productArrayList.size());
                            Log.e(TAG, "tmpStatusOptionArrayList Size :  " + productArrayList.size());

                            ArrayList<Product> tmpProductArrayList = gson.fromJson(
                                    dataJObject.getJSONArray(
                                            IWebService.KEY_RES_PRODUCT_LIST)
                                            .toString(),
                                    new TypeToken<ArrayList<Product>>() {
                                    }.getType());


                            if (tmpProductArrayList != null) {
                                Log.e(TAG, "tmpProductArrayList Size :  " + productArrayList.size());
                                productArrayList.addAll(tmpProductArrayList);
                                productDetailsAdapter.notifyDataSetChanged();
                            }

                            ArrayList<StatusOptionValue> tmpStatusOptionArrayList = gson.fromJson(
                                    dataJObject.getJSONArray(
                                            IWebService.KEY_RES_STATUS_LIST)
                                            .toString(),
                                    new TypeToken<ArrayList<StatusOptionValue>>() {
                                    }.getType());

                            if (tmpStatusOptionArrayList != null) {
                                Log.e(TAG, "tmpStatusOptionArrayList Size :  " + productArrayList.size());
                                statusOptionValueArrayList.addAll(tmpStatusOptionArrayList);
                            }


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

    private void orderstatus(int statusCode) {

        if (statusCode != IWebService.KEY_REQ_STATUS_ORDER_PLACED) {

            if (statusCode == IWebService.KEY_REQ_STATUS_ACCEPTED) {

                lltAccpeted.setBackgroundColor(getResources().getColor(R.color.order_datails_accepted));
                imgAccpeted.setImageResource(R.drawable.accepted);
                txtAccepted.setTextColor(getResources().getColor(R.color.white));
                lltAccpeted.setClickable(false);

                isAccpted = false;
                isPurchasing = true;
                isShiping = false;
                isCompleted = false;


            } else if (statusCode == IWebService.KEY_REQ_STATUS_PUECHASING) {

                lltAccpeted.setBackgroundColor(getResources().getColor(R.color.order_datails_accepted));
                imgAccpeted.setImageResource(R.drawable.accepted);
                txtAccepted.setTextColor(getResources().getColor(R.color.white));
                lltAccpeted.setClickable(false);

                lltPurchasing.setBackgroundColor(getResources().getColor(R.color.order_datails_purchasing));
                imgPurchasing.setImageResource(R.drawable.purchasing);
                txtxPurchasing.setTextColor(getResources().getColor(R.color.white));
                lltPurchasing.setClickable(false);

                isAccpted = false;
                isPurchasing = false;
                isShiping = true;
                isCompleted = false;

            } else if (statusCode == IWebService.KEY_REQ_STATUS_SHIPING) {
                lltAccpeted.setBackgroundColor(getResources().getColor(R.color.order_datails_accepted));
                imgAccpeted.setImageResource(R.drawable.accepted);
                txtAccepted.setTextColor(getResources().getColor(R.color.white));
                lltAccpeted.setClickable(false);

                lltPurchasing.setBackgroundColor(getResources().getColor(R.color.order_datails_purchasing));
                imgPurchasing.setImageResource(R.drawable.purchasing);
                txtxPurchasing.setTextColor(getResources().getColor(R.color.white));
                lltPurchasing.setClickable(false);

                lltShiping.setBackgroundColor(getResources().getColor(R.color.order_datails_shiping));
                imgShiping.setImageResource(R.drawable.shipping);
                txtShiping.setTextColor(getResources().getColor(R.color.white));
                lltShiping.setClickable(false);

                isAccpted = false;
                isPurchasing = false;
                isShiping = false;
                isCompleted = true;

            } else {
                if (statusCode == IWebService.KEY_REQ_STATUS_COMPLETED) {
                    lltAccpeted.setBackgroundColor(getResources().getColor(R.color.order_datails_accepted));
                    imgAccpeted.setImageResource(R.drawable.accepted);
                    txtAccepted.setTextColor(getResources().getColor(R.color.white));
                    lltAccpeted.setClickable(false);

                    lltPurchasing.setBackgroundColor(getResources().getColor(R.color.order_datails_purchasing));
                    imgPurchasing.setImageResource(R.drawable.purchasing);
                    txtxPurchasing.setTextColor(getResources().getColor(R.color.white));
                    lltPurchasing.setClickable(false);

                    lltShiping.setBackgroundColor(getResources().getColor(R.color.order_datails_shiping));
                    imgShiping.setImageResource(R.drawable.shipping);
                    txtShiping.setTextColor(getResources().getColor(R.color.white));
                    lltShiping.setClickable(false);

                    lltCompleted.setBackgroundColor(getResources().getColor(R.color.order_datails_completed));
                    imgCompleted.setImageResource(R.drawable.completed_white);
                    txtCompleted.setTextColor(getResources().getColor(R.color.white));
                    lltCompleted.setClickable(false);

                    isAccpted = false;
                    isPurchasing = false;
                    isShiping = false;
                    isCompleted = false;
                }
            }
        } else {
            isAccpted = true;
            isPurchasing = false;
            isShiping = false;
            isCompleted = false;

        }
    }

    public void sendOrderStatus(String orderNumber, int status) {

        try {

            JSONObject orderstatusParam = new JSONObject();
            orderstatusParam.put(IWebService.KEY_REQ_ORDER_NUMBER, orderNumber);
            orderstatusParam.put(IWebService.KEY_REQ_EMPLOYEE_ID, DBAdapter.getMapKeyValueString(getActivity(), IDatabase.IMap.KEY_EMPLOYEE_ID));
            orderstatusParam.put(IWebService.KEY_RES_STATUS, status);


            DataRequest signinDataRequest = new DataRequest(getActivity());
            signinDataRequest.execute(IWebService.ACCEPT_ORDER, orderstatusParam.toString(), new DataRequest.CallBack() {
                public void onPreExecute() {
                    rlvGlobalProgressbar.setVisibility(View.VISIBLE);


                }

                public void onPostExecute(String response) {
                    rlvGlobalProgressbar.setVisibility(View.GONE);

                    try {

                        if (!DataRequest.hasError(getActivity(), response, true)) {

                            JSONObject dataJObject = DataRequest.getJObjWebdata(response);

                            Gson gson = new Gson();

                            orderstatus(Integer.valueOf(dataJObject.getString(IWebService.KEY_RES_STATUS)));

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

    public void showAlertEmployeeComment(Context context, final String productItemID, final int productAvailability, final ImageView imgproductStatus, final int position) {

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

                if (Integer.valueOf(productAvailability) == 1) {
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
                    sendUpdateOrderItemAvailibility(productItemID, productAvailability, etxproductComments.getText().toString());
                    statusDrawable(productAvailability, imgproductStatus);
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

    public void sendUpdateOrderItemAvailibility(String productItemId, int productAvailability, String comments) {

        try {

            JSONObject productstatusParam = new JSONObject();
            productstatusParam.put(IWebService.KEY_REQ_ORDER_ITEM_ID, productItemId);
            productstatusParam.put(IWebService.KEY_REQ_SET_VALUE, productAvailability);
            productstatusParam.put(IWebService.KEY_REQ_PRODUCT_COMMENTS, comments);


            DataRequest signinDataRequest = new DataRequest(getActivity());
            signinDataRequest.execute(IWebService.UPDATE_ORDERITEM_AVAILIBILITY, productstatusParam.toString(), new DataRequest.CallBack() {
                public void onPreExecute() {
                    rlvGlobalProgressbar.setVisibility(View.VISIBLE);

                }

                public void onPostExecute(String response) {
                    rlvGlobalProgressbar.setVisibility(View.GONE);

                    try {

                        if (!DataRequest.hasError(getActivity(), response, false)) {

                            JSONObject dataJObject = DataRequest.getJObjWebdata(response);

                            Gson gson = new Gson();

                            Log.e(TAG, "Status Name : " + dataJObject.getString(IWebService.KEY_REQ_SET_VALUE));


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

    public void statusDrawable(int statusCode, ImageView imgIteStatus) {


        if (statusCode == IWebService.KEY_REQ_STATUS_PRODUCT_NOT_AVAILABLE) {

            imgIteStatus.setImageResource(R.drawable.not_available_red);

        } else if (statusCode == IWebService.KEY_REQ_STATUS_PRODUCT_AVAILABLE) {

            imgIteStatus.setImageResource(R.drawable.available_green);

        } else {

            imgIteStatus.setImageResource(R.drawable.round);
        }
    }

    private void showAlertStatusOption(final ArrayList<StatusOptionValue> statusOptionValueArrayList, final TextView txtOption) {

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
        final SelectionAdapter<StatusOptionValue> filterStateAdapter = new SelectionAdapter<StatusOptionValue>(statusOptionValueArrayList);

        filterStateAdapter
                .setBindAdapterInterface(new SelectionAdapter.IBindAdapterValues<StatusOptionValue>() {
                    @Override
                    public void bindValues(SelectionAdapter.MyViewHolder holder, final int position) {
                        // TODO Auto-generated method stub

                        holder.txtSelectionValue.setText(statusOptionValueArrayList
                                .get(position).statusLable);
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
                                        for (int i = 0; i < statusOptionValueArrayList
                                                .size(); i++) {
                                            statusOptionValueArrayList.get(i).isSelected = false;
                                        }
                                        statusOptionValueArrayList.get(position).isSelected = true;
                                        txtOption.setText(statusOptionValueArrayList.get(position).statusLable);
                                        filterStateAdapter.notifyDataSetChanged();
                                        sendOrderStatus(order_number, statusOptionValueArrayList.get(position).status);

                                        alertDialog.dismiss();
                                    }
                                });
                    }
                });

        recyclerFilter.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerFilter.setAdapter(filterStateAdapter);
        alertDialog.getWindow().setBackgroundDrawableResource(
                R.color.transparent);
//        alertDialog.setCancelable(false);
        alertDialog.show();
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
