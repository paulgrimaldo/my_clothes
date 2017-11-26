package com.apps.paul.myclothes.Modelos.Ropa;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by paul on 11/24/2017.
 */

public class Ropa implements Serializable {
    private String foto;
    private String color;
    private String descripcion;
    private String marca;
    private byte estado = 1;
    private byte es_favorito = 0;
    private byte es_usado = 0;
    private int id_armario;
    private int id_categoria;

    public Ropa() {
    }

    public Ropa(JSONObject data) {
        try {
            this.foto = data.getString("foto");
            this.color = data.getString("color");
            this.descripcion = data.getString("descripcion");
            this.marca = data.getString("marca");
            this.estado = (byte) data.getInt("estado");
            this.es_favorito = (byte) data.getInt("es_favorito");
            this.es_usado = (byte) data.getInt("es_usado");
            this.id_armario = data.getInt("id_armario");
            this.id_categoria = data.getInt("id_categoria");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public byte getEstado() {
        return estado;
    }

    public void setEstado(byte estado) {
        this.estado = estado;
    }

    public byte getEs_favorito() {
        return es_favorito;
    }

    public void setEs_favorito(byte es_favorito) {
        this.es_favorito = es_favorito;
    }

    public byte getEs_usado() {
        return es_usado;
    }

    public void setEs_usado(byte es_usado) {
        this.es_usado = es_usado;
    }

    public int getId_armario() {
        return id_armario;
    }

    public void setId_armario(int id_armario) {
        this.id_armario = id_armario;
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }


    @Override
    public String toString() {
        return "Ropa{" +
                "foto='" + foto + '\'' +
                ", color='" + color + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", marca='" + marca + '\'' +
                ", estado=" + estado +
                ", es_favorito=" + es_favorito +
                ", es_usado=" + es_usado +
                ", id_armario=" + id_armario +
                ", id_categoria=" + id_categoria +
                '}';
    }
}
