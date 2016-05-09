package com.fastmarket.fastmarket;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sebastian on 8/05/2016.
 */
public class ListProductos extends ArrayAdapter<Producto> {
    static class ProductoHolder {
        TextView nombre;
        TextView categorias;
        ImageView imagen;
    }

    private Activity context;
    private List<Producto> productos;

    public ListProductos(Activity context, List<Producto> productos) {
        super(context, R.layout.list_view_layout,productos);
        this.context = context;
        this.productos = productos;
    }

    public void add(Producto producto) {
        productos.add(producto);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ProductoHolder holder = new ProductoHolder();
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            v = inflater.inflate(R.layout.list_view_layout, parent,false);
            holder.nombre = (TextView) v.findViewById(R.id.nombreProducto);
            holder.categorias = (TextView) v.findViewById(R.id.categoriasProducto);
            holder.imagen = ((ImageView) v.findViewById(R.id.imagenProducto));
            v.setTag(holder);
        } else {
            holder = (ProductoHolder) v.getTag();
        }
        new DownloadImageTask(holder.imagen).execute(productos.get(position).getImagen());
        holder.nombre.setText(productos.get(position).getNombre());
        holder.categorias.setText(productos.get(position).getCategorias());
        return v;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
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
