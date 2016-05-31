package com.fastmarket.fastmarket;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextView;
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
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private TextView totalText;
    private ListProducts lista;
    private List<Product> products;
    private String authDE = "Cc10Q5b3u5Ll0Vu6", keyDE = "/7gTYlpC/2dT";
    private RequestQueue requestQueue;
    private boolean showFAB = true;
    private float total = 0;
    DBHandler db;
    boolean databaseReady;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new IntentIntegrator(MainActivity.this).setCaptureActivity(BarCodeScannerActivity.class).initiateScan();
                }
            });
        }
        final Animation growAnimation = AnimationUtils.loadAnimation(this, R.anim.simple_grow);
        final Animation shrinkAnimation = AnimationUtils.loadAnimation(this, R.anim.simple_shrink);
        View bottomSheet = findViewById(R.id.bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setPeekHeight(120);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        if (showFAB)
                            fab.startAnimation(shrinkAnimation);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        showFAB = true;
                        fab.setVisibility(View.VISIBLE);
                        fab.startAnimation(growAnimation);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        showFAB = false;
                        fab.setVisibility(View.INVISIBLE);
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        totalText = (TextView) findViewById(R.id.total);

        db = new DBHandler(this);
        databaseReady = false;
        int count = db.countProducts();
        if (count < 28) {
            createProducts();
        }else{
            databaseReady = true;
        }
        products = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listView);
        lista = new ListProducts(this, products, new BtnClickListener() {
            @Override
            public void add(int position) {
                total += lista.increase(true, position);
                updateTotal();
            }

            @Override
            public void minus(int position) {
                total -= lista.increase(false, position);
                if (total < 0) {
                    total = 0;
                }
                updateTotal();
            }
        });
        listView.setAdapter(lista);
        requestQueue = Volley.newRequestQueue(this);
    }

    private void updateTotal() {
        totalText.setText(((int) total) + "");
    }

    private void createProducts() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.addProduct(1, new Product("Pañuelos familia", "Hogar", 2500, 0, "7702026032616", Utils.getBytes(getImage(R.drawable.panuelos_familia))));

                db.addProduct(2, new Product("Gaseosa Cocacocola 1/2", "Bebidas", 3373, 0, "7702535001769", Utils.getBytes(getImage(R.drawable.coca_cola))));
                db.addProduct(3, new Product("Gaseosa CocaCola Light 1/2", "Bebidas", 3524, 0, "7702535013557", Utils.getBytes(getImage(R.drawable.coca_cola))));
                db.addProduct(4, new Product("Gaseosa CocaCola 2uni", "Bebidas", 5776, 0, "7702535017326", Utils.getBytes(getImage(R.drawable.coca_cola))));
                db.addProduct(5, new Product("Gaseosa CocaCola Zero Pet X 600", "Bebidas", 1719, 0, "7702535006764", Utils.getBytes(getImage(R.drawable.coca_cola))));
                db.addProduct(6, new Product("Gaseosa CocaCola Zero 2.5", "Bebidas", 3524, 0, "7702535007914", Utils.getBytes(getImage(R.drawable.coca_cola))));
                db.addProduct(7, new Product("Gaseosa CocaCola Light X 600", "Bebidas", 1718, 0, "7702535011256", Utils.getBytes(getImage(R.drawable.coca_cola))));
                db.addProduct(8, new Product("Gaseosa CocaCola 1.5", "Bebidas", 2241, 0, "7702535011805", Utils.getBytes(getImage(R.drawable.coca_cola))));
                db.addProduct(9, new Product("Gaseosa CocaCola Zero 1.5", "Bebidas", 2392, 0, "7702535011799", Utils.getBytes(getImage(R.drawable.coca_cola))));
                db.addProduct(10, new Product("Gaseosa CocaCola Mini Lata X 237", "Bebidas", 5216, 0, "7702535010952", Utils.getBytes(getImage(R.drawable.coca_cola))));
                db.addProduct(11, new Product("Gaseosa Sprite X 400ml", "Bebidas", 1059, 0, "7702535012086", Utils.getBytes(getImage(R.drawable.sprite))));
                db.addProduct(12, new Product("Gaseosa Quatro X 400ml", "Bebidas", 1058, 0, "7702535012079", Utils.getBytes(getImage(R.drawable.quatro))));
                db.addProduct(13, new Product("Gaseosa CocaCola Light X 1.5", "Bebidas", 2392, 0, "7702535011782", Utils.getBytes(getImage(R.drawable.coca_cola))));
                db.addProduct(14, new Product("Gaseosa CocaCola", "Bebidas", 5216, 0, "7702535013687", Utils.getBytes(getImage(R.drawable.coca_cola))));
                db.addProduct(15, new Product("Gaseosa Sprite 1.4", "Bebidas", 2091, 0, "7702535016633", Utils.getBytes(getImage(R.drawable.sprite))));
                db.addProduct(16, new Product("Agua Brisa Bidon", "Bebidas", 4648, 0, "7702535002650", Utils.getBytes(getImage(R.drawable.noimage))));
                db.addProduct(17, new Product("Agua Manantial X 600", "Bebidas", 1346, 0, "7702609005372", Utils.getBytes(getImage(R.drawable.agua_manantial_600ml))));
                db.addProduct(18, new Product("Agua Manantial Con Gas X 600ml", "Bebidas", 1346, 0, "7702609005488", Utils.getBytes(getImage(R.drawable.agua_manantial_600ml))));
                db.addProduct(19, new Product("Agua Brisa Pet Gas 600ml", "Bebidas", 1254, 0, "7702535010341", Utils.getBytes(getImage(R.drawable.noimage))));
                db.addProduct(20, new Product("Te Fuze Tea Te Negro-Durazno X 400", "Bebidas", 1228, 0, "7702535014233", Utils.getBytes(getImage(R.drawable.noimage))));
                db.addProduct(21, new Product("Bebida Powerade Surtido", "Bebidas", 1724, 0, "7702535015476", Utils.getBytes(getImage(R.drawable.noimage))));
                db.addProduct(22, new Product("Te Fuze Tea Negro Limon X 1.2 ", "Bebidas", 1796, 0, "7702535015353", Utils.getBytes(getImage(R.drawable.noimage))));
                db.addProduct(23, new Product("Te Fuze Tea Negro Durazno X 1.2", "Bebidas", 1796, 0, "7702535015346", Utils.getBytes(getImage(R.drawable.noimage))));
                db.addProduct(24, new Product("Jugo Fresh Naranja X 2.5", "Bebidas", 2370, 0, "7702535015544", Utils.getBytes(getImage(R.drawable.noimage))));
                db.addProduct(25, new Product("Jugo Fresh Mandarina 2.5 Lts", "Bebidas", 2370, 0, "7702535015537", Utils.getBytes(getImage(R.drawable.noimage))));
                db.addProduct(26, new Product("Jugo Fresh Naranja X 2.5 ", "Bebidas", 4203, 0, "7702535015759", Utils.getBytes(getImage(R.drawable.noimage))));
                db.addProduct(27, new Product("Jugo Vallefrut Mango X 300", "Bebidas", 948, 0, "7702535009710", Utils.getBytes(getImage(R.drawable.noimage))));
                db.addProduct(28, new Product("Jugos Vallefrut Lulo X 300 ", "Bebidas", 948, 0, "7702535009727", Utils.getBytes(getImage(R.drawable.noimage))));
                databaseReady = true;
            }

            private Bitmap getImage(int id) {
                Drawable myDrawable = ResourcesCompat.getDrawable(getResources(), id, null);
                Bitmap image = null;
                if (myDrawable != null) {
                    image = ((BitmapDrawable) myDrawable).getBitmap();
                }
                return image;
            }
        }).start();

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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String code = result.getContents();
                Product p = null;
                if (databaseReady) {
                    p = db.getProductByCode(code);
                }
                if (p != null) {
                    p.setQuantity(1);
                    lista.add(p);
                    total += p.getPrice();
                    updateTotal();
                } else {
                    getProductExternal(code);
                }
            }
        }
    }

    private void getProductExternal(String code) {
        String signature = "";
        try {
            signature = Utils.calculateRFC2104HMAC(code, authDE);
        } catch (SignatureException | NoSuchAlgorithmException | InvalidKeyException ex) {
            Log.d("Error en la conversion", "HMAC_SHA1", ex);
        }
        Log.d("signtaure pre ",signature);
        signature = Uri.encode(signature, "=+-%");
        signature = signature.replaceAll("\\%0A", "");
        Log.d("signtaure pos ",signature);
        String url = "http://digit-eyes.com/gtin/v2_0/?upc_code=" + code + "&app_key=" + keyDE + "&signature=" + signature + "&field_names=description,image,categories";
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
                            addElement(producto, imagen, categorias, codeFinal);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,"Producto no encontrado",Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                });
        requestQueue.add(jsObjRequest);
    }

    private void addElement(String product, String image, String categories, String code) {
        Product p = new Product(product, categories, 0, 1, image, code);
        lista.add(p);
    }
}
