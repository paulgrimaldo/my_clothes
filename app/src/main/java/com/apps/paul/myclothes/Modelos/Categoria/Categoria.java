package com.apps.paul.myclothes.Modelos.Categoria;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by paul on 11/24/2017.
 */

public class Categoria implements Serializable {
    String nombre;
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Categoria(JSONObject data) throws JSONException {

        this.nombre = data.getString("nombre");
        this.id = data.getInt("id");
    }

    @Override
    public String toString() {
        return nombre;

    }
}
