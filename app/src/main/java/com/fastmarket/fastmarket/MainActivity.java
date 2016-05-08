package com.fastmarket.fastmarket;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ListProductos lista;
    private String authDE="Cc10Q5b3u5Ll0Vu6",  keyDE="/7gTYlpC/2dT";

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
        listView = (ListView) findViewById(R.id.listView);
        lista = new ListProductos(this);
        listView.setAdapter(lista);
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() != null) {
                String code = result.getContents();
                String signature ="";
                try {
                    signature = Utils.calculateRFC2104HMAC(code, authDE);
                }catch(SignatureException|NoSuchAlgorithmException|InvalidKeyException ex){
                    Log.d("Error en la conversion","HMAC_SHA1",ex);
                }
                String url = "http://digit-eyes.com/gtin/v2_0/?upc_code="+code+"&app_key="+keyDE+"&signature="+signature+"&language=es&field_names=description,image,categories";
                Log.d("url del sitio", url);
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
                                    Toast.makeText(getApplicationContext(),
                                            "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO Auto-generated method stub

                            }
                        });
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(jsObjRequest);
            }
        }
    }

    private void agregarElemento(String producto, String imagen, String categorias) {
        Producto p =  new Producto(imagen,categorias,0,producto);
        lista.add(p);
    }
}
