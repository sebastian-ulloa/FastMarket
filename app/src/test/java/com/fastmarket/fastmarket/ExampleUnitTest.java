package com.fastmarket.fastmarket;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }


    @Test
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
    @Test
    public void testSHA() throws Exception {
        String n = "50557098069";
        int a = 39;
        for (int i = 0; i < 10; i++) {
            System.out.println(Utils.calculateRFC2104HMAC(n + String.valueOf(a), "Cc10Q5b3u5Ll0Vu6"));
            a++;
        }
       // assertEquals("4dC42WyPvICPBbix1QX5ANKWmwo=", Utils.calculateRFC2104HMAC("5055709806939", "Cc10Q5b3u5Ll0Vu6"));
    }
}