package com.shoppin.shopper.paymentexpress;

import android.content.Context;
import android.util.Log;

import com.shoppin.shopper.network.DataRequest;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class PxPay {

    public static String TAG = PxPay.class.getSimpleName();

    public static String GenerateRequest(String userId, String key,
                                         GenerateRequest gr, String Url, Context context) {

        try {
            gr.setPxPayUserId(userId);
            gr.setPxPayKey(key);

            String inputXml = gr.getXml();
            String responseXml = PxPay.SubmitXml(inputXml, Url, context);

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

    private static String SubmitXml(String Xml, String Url, final Context context) throws Exception {
        String responseBody = null;
        try {
            DataRequest getOrderDetailsRequest = new DataRequest(context);
            getOrderDetailsRequest.execute(Url, Xml, new DataRequest.CallBack() {
                        public void onPreExecute() {


                        }

                        public void onPostExecute(String response) {
                            Log.e(TAG, "response" + response);
//                            try {
//
//                                if (!DataRequest.hasError(context, response, true)) {
//
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }

                        }
                    }
            );

        } catch (Exception e) {
            e.printStackTrace();
        }

//        HttpClient client = new DefaultHttpClient();
//
//        // Prepare the POST request
//        HttpPost pxpayRequest = new HttpPost(Url);
//        pxpayRequest.setEntity(new StringEntity(Xml));
//
//        // Execute the request and extract the response
//        ResponseHandler<String> responseHandler = new BasicResponseHandler();
//        String responseBody = client.execute(pxpayRequest, responseHandler);

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
