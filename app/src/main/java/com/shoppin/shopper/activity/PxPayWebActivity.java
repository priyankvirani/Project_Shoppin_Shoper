package com.shoppin.shopper.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.crystal.crystalpreloaders.widgets.CrystalPreloader;
import com.shoppin.shopper.R;
import com.shoppin.shopper.network.IWebService;
import com.shoppin.shopper.paymentexpress.PxPay;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PxPayWebActivity extends AppCompatActivity {
    @BindView(R.id.webView)
    WebView webView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.preLoader)
    CrystalPreloader preLoader;

    Context context;

    private static String TAG = PxPayWebActivity.class.getSimpleName();
    String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_px_pay_web);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            url = intent.getStringExtra("url");
            if (url != null) {
                Log.e(TAG, url);
                initWeb();
                webView.loadUrl(url);


            }
        }
    }

    private void initWeb() {
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onReceivedSslError(final WebView view, final SslErrorHandler handler, SslError error) {
//                Log.d("CHECK", "onReceivedSslError");
//                AlertDialog.Builder builder = new AlertDialog.Builder(PxPayWebActivity.this);
//                AlertDialog alertDialog = builder.create();
//                String message = "Certificate error.";
//                switch (error.getPrimaryError()) {
//                    case SslError.SSL_UNTRUSTED:
//                        message = "The certificate authority is not trusted.";
//                        break;
//                    case SslError.SSL_EXPIRED:
//                        message = "The certificate has expired.";
//                        break;
//                    case SslError.SSL_IDMISMATCH:
//                        message = "The certificate Hostname mismatch.";
//                        break;
//                    case SslError.SSL_NOTYETVALID:
//                        message = "The certificate is not yet valid.";
//                        break;
//                }
//                message += " Do you want to continue anyway?";
//                alertDialog.setTitle("SSL Certificate Error");
//                alertDialog.setMessage(message);
//                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Log.d("CHECK", "Button ok pressed");
//                        // Ignore SSL certificate errors
//                        handler.proceed();
//                    }
//                });
//                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Log.d("CHECK", "Button cancel pressed");
//                        handler.cancel();
//                    }
//                });
//                alertDialog.show();
//            }
//        });

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.addJavascriptInterface(new MyJavaScriptInterface(this), "HtmlViewer");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.e(TAG, "response " + webView.getUrl());
                view.loadUrl(request.toString());

                return true;
            }


        });

        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                Log.d(TAG, "newProgress = " + newProgress);
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                    preLoader.setVisibility(View.GONE);
                    Log.e(TAG, "response " + webView.getUrl());

                    String pxuri = webView.getUrl();

                    try {
                        List<NameValuePair> params = URLEncodedUtils.parse(new URI(pxuri), "UTF-8");

                        for (NameValuePair param : params) {
                            if (param.getName().equals("result")) {
                                Log.e(TAG, param.getName() + " : " + param.getValue());

                                try {

                                    PxPay.ProcessResponse(getResources().getString(R.string.pxpay_userid),
                                            getResources().getString(R.string.pxpay_userid),
                                            param.getValue(),
                                            IWebService.TRANSACTION_REQUEST, context);


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                } else if (newProgress == 1) {
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    preLoader.setVisibility(View.VISIBLE);
                }
            }

        });
        webView.setFocusable(true);
        webView.setFocusableInTouchMode(true);
        webView.requestFocus(View.FOCUS_DOWN);

        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.e(TAG, "response " + webView.getUrl());
                    case MotionEvent.ACTION_UP:
                        if (!v.hasFocus()) {
                            v.requestFocus();
                        }
                        break;
                }
                return false;
            }
        });


    }

    class MyJavaScriptInterface {

        private Context ctx;

        MyJavaScriptInterface(Context ctx) {
            this.ctx = ctx;
        }

        @JavascriptInterface
        public void showHTML(String html) {
            Log.e(TAG, "response " + html);
        }
    }
}
