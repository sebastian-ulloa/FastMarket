package com.fastmarket.fastmarket;

/**
 * Created by Sebastian on 8/05/2016.
 */
public class Producto {
    private String nombre;
    private String imagen;
    private String categorias;
    private float precio;

    public Producto(String imagen, String categorias, float precio, String nombre) {
        this.imagen = imagen;
        this.categorias = categorias;
        this.precio = precio;
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public String getCategorias() {
        return categorias;
    }

    public float getPrecio() {
        return precio;
    }
}
