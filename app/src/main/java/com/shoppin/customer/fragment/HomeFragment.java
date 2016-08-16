package com.shoppin.customer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shoppin.customer.R;
import com.shoppin.customer.activity.SignupActivity;
import com.shoppin.customer.adapter.CategoryAdapter;
import com.shoppin.customer.model.Category;
import com.shoppin.customer.network.DataRequest;
import com.shoppin.customer.network.IWebService;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ubuntu on 15/8/16.
 */

public class HomeFragment extends BaseFragment {

    private static final String TAG = SignupActivity.class.getSimpleName();


    @BindView(R.id.rlvGlobalProgressbar)
    public View rlvGlobalProgressbar;

    @BindView(R.id.lstCategory)
    ListView lstCategory;

    private ArrayList<Category> categoryArrayList;
    private CategoryAdapter categoryAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        layoutView = inflater.inflate(R.layout.fragment_home, null);
        layoutView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, layoutView);

        categoryArrayList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getActivity(), categoryArrayList);
        lstCategory.setAdapter(categoryAdapter);

        getCategory();

        return layoutView;
    }

    private void getCategory() {
        try {
            JSONObject loginParam = new JSONObject();
            loginParam.put(IWebService.KEY_REQ_IS_HOME, true);
            DataRequest getSuburbsDataRequest = new DataRequest(getActivity());
            getSuburbsDataRequest.execute(IWebService.GET_CATEGORY, loginParam.toString(), new DataRequest.CallBack() {
                public void onPreExecute() {
                    rlvGlobalProgressbar.setVisibility(View.VISIBLE);
                }

                public void onPostExecute(String response) {
                    try {
                        rlvGlobalProgressbar.setVisibility(View.GONE);
                        if (!DataRequest.hasError(getActivity(), response, true)) {
                            JSONObject dataJObject = DataRequest.getJObjWebdata(response);
                            Gson gson = new Gson();
                            ArrayList<Category> tmpCategoryArrayList = gson.fromJson(
                                    dataJObject.getJSONArray(IWebService.KEY_RES_CATEGORY_LIST).toString(),
                                    new TypeToken<ArrayList<Category>>() {
                                    }.getType());

                            if (tmpCategoryArrayList != null) {
                                categoryArrayList.addAll(tmpCategoryArrayList);
                                categoryAdapter.notifyDataSetChanged();
                                Log.d(TAG, "tmpCategoryArrayList = " + tmpCategoryArrayList.size());
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
}
