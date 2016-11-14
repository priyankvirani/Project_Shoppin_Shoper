package com.shoppin.shopper.paymentexpress;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.shoppin.shopper.R;
import com.shoppin.shopper.activity.PxPayWebActivity;
import com.shoppin.shopper.network.DataRequest;
import com.shoppin.shopper.network.IWebService;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static com.shoppin.shopper.R.id.webView;

public class PxPay {

    private static String TAG = PxPay.class.getSimpleName();
    private static String responseBody = null;


    public static String GenerateRequest(String userId, String key,
                                         GenerateRequest gr, String Url, Context context) {


        try {
            gr.setPxPayUserId(userId);
            gr.setPxPayKey(key);


            String inputXml = gr.getXml();

            String responseXml = PxPay.SubmitXml(inputXml, Url, context);

            Log.e(TAG, "responseXml : " + responseXml);

            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            InputStream is = new ByteArrayInputStream(responseXml
                    .getBytes("UTF-8"));
            Document doc = docBuilder.parse(is);

            NodeList nodes = doc.getElementsByTagName("Request");

            Element element = (Element) nodes.item(0);
            NodeList name;
            Element line;

            name = element.getElementsByTagName("URI");
            line = (Element) name.item(0);
            String uri = PxPay.getCharacterDataFromElement(line);

            Log.e(TAG, "uri : " + uri);

            Intent intent = new Intent(context, PxPayWebActivity.class);
            intent.putExtra("url", uri);
            context.startActivity(intent);


            return uri;
        } catch (Exception e) {
            return "";
        }

    }

    public static Response ProcessResponse(String UserId, String Key,
                                           String result, String Url, Context context) throws Exception {

        String inputXml = "<ProcessResponse><PxPayUserId>" + UserId
                + "</PxPayUserId><PxPayKey>" + Key + "</PxPayKey><Response>"
                + result + "</Response></ProcessResponse>";
        String outputXml;

        outputXml = PxPay.SubmitXml(inputXml, Url, context);


        Response response = new Response(outputXml);

        return response;

    }


    private static String SubmitXml(final String Xml, final String Url, final Context context) throws Exception {

        try {
            DataRequest getOrderDetailsRequest = new DataRequest(context);
            getOrderDetailsRequest.pxexecute(Url, Xml, new DataRequest.CallBack() {
                        public void onPreExecute() {

                        }

                        public void onPostExecute(String response) {


                            try {
                                responseBody = response;

//                                    Log.e(TAG, "response " + response);
//                                    Log.e(TAG, "Result " + (ProcessResponse(context.getResources().getString(R.string.pxpay_userid),
//                                            context.getResources().getString(R.string.pxpay_key),
//                                            response, Url, context)).toString());


                                DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance()
                                        .newDocumentBuilder();
                                InputStream is = new ByteArrayInputStream(response
                                        .getBytes("UTF-8"));
                                Document doc = docBuilder.parse(is);

                                NodeList nodes = doc.getElementsByTagName("Request");

                                Element element = (Element) nodes.item(0);
                                NodeList name;
                                Element line;

                                name = element.getElementsByTagName("URI");
                                line = (Element) name.item(0);
                                String uri = PxPay.getCharacterDataFromElement(line);

                                Log.e(TAG, "uri : " + uri);


                                List<NameValuePair> params = URLEncodedUtils.parse(new URI(uri), "UTF-8");

                                Log.e(TAG, "size " + params.size());

                                if (params.size() == 0){

                                    Intent intent = new Intent(context, PxPayWebActivity.class);
                                    intent.putExtra("url", uri);
                                    context.startActivity(intent);
                                }


                            } catch (
                                    Exception e
                                    )

                            {
                                e.printStackTrace();
                            }


                        }
                    }
            );

        } catch (
                Exception e
                )

        {
            e.printStackTrace();
        }


        return responseBody;
    }


    private static String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "";
    }

}
