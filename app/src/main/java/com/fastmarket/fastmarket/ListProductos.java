package com.fastmarket.fastmarket;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Sebastian on 8/05/2016.
 */
public class ListProductos extends ArrayAdapter<String> {
    static class ProductoHolder{
        TextView nombre;
        TextView categorias;
        ImageView imagen;
    }
    private Activity context;
    private List<Producto> productos;
    private ProductoHolder holder;

    public ListProductos(Activity context, List<Producto> productos) {
        super(context, R.layout.list_view_layout);
        this.context = context;
        this.productos = productos;
    }

    public ListProductos(Activity context) {
        super(context, R.layout.list_view_layout);
        this.context = context;
        this.productos = new List<Producto>() {
            @Override
            public void add(int i, Producto producto) {

            }

            @Override
            public boolean add(Producto producto) {
                return false;
            }

            @Override
            public boolean addAll(int i, Collection<? extends Producto> collection) {
                return false;
            }

            @Override
            public boolean addAll(Collection<? extends Producto> collection) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(Collection<?> collection) {
                return false;
            }

            @Override
            public Producto get(int i) {
                return null;
            }

            @Override
            public int indexOf(Object o) {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @NonNull
            @Override
            public Iterator<Producto> iterator() {
                return null;
            }

            @Override
            public int lastIndexOf(Object o) {
                return 0;
            }

            @Override
            public ListIterator<Producto> listIterator() {
                return null;
            }

            @NonNull
            @Override
            public ListIterator<Producto> listIterator(int i) {
                return null;
            }

            @Override
            public Producto remove(int i) {
                return null;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean removeAll(Collection<?> collection) {
                return false;
            }

            @Override
            public boolean retainAll(Collection<?> collection) {
                return false;
            }

            @Override
            public Producto set(int i, Producto producto) {
                return null;
            }

            @Override
            public int size() {
                return 0;
            }

            @NonNull
            @Override
            public List<Producto> subList(int i, int i1) {
                return null;
            }

            @NonNull
            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @NonNull
            @Override
            public <T> T[] toArray(T[] ts) {
                return null;
            }
        };
    }

    public void add(Producto producto)
    {
        productos.add(producto);
        notifyDataSetChanged();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View v = convertView;
        if(convertView == null)
        {
            v = inflater.inflate(R.layout.list_view_layout,null, true);
            holder = new ProductoHolder();
            holder.nombre = (TextView) v.findViewById(R.id.nombreProducto);
            holder.categorias = (TextView) v.findViewById(R.id.categoriasProducto);
            holder.imagen = ((ImageView) v.findViewById(R.id.imagenProducto));
            v.setTag(holder);
        }else{
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
