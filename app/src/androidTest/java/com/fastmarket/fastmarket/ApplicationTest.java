package com.fastmarket.fastmarket;

import android.test.ActivityInstrumentationTestCase2;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mActivity;

    public ApplicationTest() throws Exception {
        super(MainActivity.class);
        jsonRequest();
    }

    public void jsonRequest() throws Exception {
        String code = "0049000006582";
        String url = "http://eandata.com/feed/?v=3&keycode=45B88E105738A12C&mode=json&find=" + code;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject product = response.getJSONObject("product").getJSONObject("attributes");
                            String name = product.getString("product");
                            String image = response.getJSONObject("product").getString("image");
                            String categories = product.getString("category");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                    }
                });
    }
}