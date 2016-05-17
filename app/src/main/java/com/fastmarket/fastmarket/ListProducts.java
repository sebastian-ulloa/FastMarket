package com.fastmarket.fastmarket;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Sebastian on 8/05/2016.
 */

public class ListProducts extends ArrayAdapter<Product> {
    private Activity context;
    private List<Product> products;
    private BtnClickListener mClickListener = null;

    public ListProducts(Activity context, List<Product> products, BtnClickListener listener) {
        super(context, R.layout.list_view_layout, products);
        this.context = context;
        this.products = products;
        mClickListener = listener;
    }

    public void add(Product product) {
        products.add(product);
        notifyDataSetChanged();
    }

    public void increase(boolean add, int position) {
        Product p = products.get(position);
        if (add) {
            p.setQuantity(p.getQuantity() + 1);
        } else {
            p.setQuantity(p.getQuantity() - 1);
        }
        if (p.getQuantity() == 0) {
            products.remove(position);
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ProductHolder holder = new ProductHolder();
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            v = inflater.inflate(R.layout.list_view_layout, parent, false);
            holder.name = (TextView) v.findViewById(R.id.nombreProducto);
            holder.image = ((ImageView) v.findViewById(R.id.imagenProducto));
            holder.add = ((ImageButton) v.findViewById(R.id.addButton));
            holder.minus = ((ImageButton) v.findViewById(R.id.minusButton));
            holder.quantity = (TextView) v.findViewById(R.id.productQuantity);
            v.setTag(holder);
        } else {
            holder = (ProductHolder) v.getTag();
        }
        new DownloadImageTask(holder.image, products.get(position).getName()).execute(products.get(position).getImage());
        holder.name.setText(products.get(position).getName());
        holder.quantity.setText(products.get(position).getQuantity() + "");
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mClickListener != null) {
                    mClickListener.add(position);
                }
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mClickListener != null) {
                    mClickListener.minus(position);
                }
            }
        });
        return v;
    }

    static class ProductHolder {
        TextView name;
        TextView categories;
        ImageView image;
        ImageButton add;
        ImageButton minus;
        TextView quantity;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        String name;

        public DownloadImageTask(ImageView bmImage, String name) {
            this.bmImage = bmImage;
            this.name = name;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
