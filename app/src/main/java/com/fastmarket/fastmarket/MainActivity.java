package com.fastmarket.fastmarket;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ListProducts lista;
    private List<Product> products;
    private String authDE = "Cc10Q5b3u5Ll0Vu6", keyDE = "/7gTYlpC/2dT";
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new IntentIntegrator(MainActivity.this).setCaptureActivity(BarCodeScannerActivity.class).initiateScan();
                }
            });
        }
        products = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listView);
        lista = new ListProducts(this, products, new BtnClickListener() {
            @Override
            public void add(int position) {
                lista.increase(true, position);
            }

            @Override
            public void minus(int position) {
                lista.increase(false, position);
            }
        });
        listView.setAdapter(lista);
        requestQueue = Volley.newRequestQueue(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String code = result.getContents();
                String signature = "";
                try {
                    signature = Utils.calculateRFC2104HMAC(code, authDE);
                } catch (SignatureException | NoSuchAlgorithmException | InvalidKeyException ex) {
                    Log.d("Error en la conversion", "HMAC_SHA1", ex);
                }
                signature = Uri.encode(signature, "=");
                signature = signature.replaceAll("\\%0A", "");
                String url = "http://digit-eyes.com/gtin/v2_0/?upc_code=" + code + "&app_key=" + keyDE + "&signature=" + signature + "&language=es&field_names=description,image,categories";
                Log.d("url del sitio", url);
                final String codeFinal = code;
                JsonObjectRequest jsObjRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String producto = response.getString("description");
                                    String imagen = response.getString("image");
                                    String categorias = response.getString("categories");
                                    agregarElemento(producto, imagen, categorias);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                requestNew(codeFinal);
                            }
                        });
                requestQueue.add(jsObjRequest);
            }
        }
    }

    private void agregarElemento(String producto, String imagen, String categorias) {
        Product p = new Product(imagen, categorias, 0, producto, 1);
        lista.add(p);
    }

    private void requestNew(String code) {
        String url = "http://eandata.com/feed/?v=3&keycode=45B88E105738A12C&mode=json&find=" + code;
        Log.d("url del sitio", url);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject product = response.getJSONObject("product");
                            String name = product.getString("product");
                            String image = product.getString("image");
                            String categories = product.getString("category");
                            agregarElemento(name, image, categories);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error volley", error.toString());
                    }
                });
        requestQueue.add(jsObjRequest);
    }
}
