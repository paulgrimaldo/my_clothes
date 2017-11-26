package com.apps.paul.myclothes.Fragmentos.Ropa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.apps.paul.myclothes.Modelos.Categoria.Categoria;
import com.apps.paul.myclothes.Modelos.Ropa.Ropa;
import com.apps.paul.myclothes.R;
import com.apps.paul.myclothes.Utils.Constantes;
import com.apps.paul.myclothes.Utils.Util;
import com.apps.paul.myclothes.Utils.VolleyHttpRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by paul on 11/23/2017.
 */

public class RegistrarPrenda_Fragment extends Fragment {
    private static final int CAMERA_REQUEST = 1;
    private static final String URL_CATEGORIAS = "categorias";
    private static final String URL_PRENDA = "prendas";

    public interface RegistrarFragment_Events {
        public void registroFinalizado(Ropa ropa);
    }


    private RegistrarFragment_Events m_callback = null;
    public static String TAG = "RegistrarPrenda_Fragment ";


    private ImageView _img_prenda;
    private EditText _descripcion;
    private EditText _marca;
    private Spinner _categoria;
    private EditText _color;
    private Button _btn_registrar;
    private Ropa m_ropa = new Ropa();
    private Bitmap picture;// la que por defecto tomara con la camara

    JsonObjectRequest jsArrayRequest;
    ArrayAdapter<Categoria> categoria_adapter;

    public static RegistrarPrenda_Fragment newInstance(Bundle args) {
        RegistrarPrenda_Fragment fragment = new RegistrarPrenda_Fragment();
        if (args != null) {
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        try {
            super.onAttach(context);
            m_callback = (RegistrarFragment_Events) context;
        } catch (ClassCastException e) {
            Log.wtf(TAG, context.getClass().getSimpleName() + " Debe implementar la interfaz " + this.getClass().getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        m_callback = null;
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registrar_ropa_fragment, container, false);
        if (view != null) {
            _descripcion = view.findViewById(R.id.input_descripcion_prenda);
            _btn_registrar = view.findViewById(R.id.btn_registrar_ropa);
            _categoria = view.findViewById(R.id.input_categoria_ropa);
            _color = view.findViewById(R.id.input_color_prenda);
            _img_prenda = view.findViewById(R.id.input_img_prenda);
            _marca = view.findViewById(R.id.input_marca_prenda);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //cargar las categorias
        _img_prenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
        _btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_ropa.setDescripcion(_descripcion.getText().toString());
                m_ropa.setMarca(_marca.getText().toString());
                int id_categoria = categoria_adapter.getItem(_categoria.getSelectedItemPosition()).getId();
                SharedPreferences preferences = getContext().getSharedPreferences("Current_User", getContext().MODE_PRIVATE);
                int id_armario = preferences.getInt("id_armario", -1);// -1 default val
                m_ropa.setId_armario(id_armario);
                m_ropa.setId_categoria(id_categoria);
                m_ropa.setFoto(encodeTobase64(picture));
                registrarPrenda();
            }
        });
        loadCategorias();
    }

    private void registrarPrenda() {
        Gson gson = new Gson();
        //convertimos el obtejo ropa a json para enviarlo por post
        HashMap<String, String> params = new HashMap();
        params.put("prenda", gson.toJson(m_ropa));
        jsArrayRequest = new JsonObjectRequest(
                Request.Method.POST,
                Constantes.URL_BASE + URL_PRENDA,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int result = response.getInt("result");
                            if (result == 200) {

                                Ropa ropa = new Ropa(response.getJSONObject("prenda"));
                                m_callback.registroFinalizado(ropa);

                            } else {
                                Toast.makeText(getContext(), "Error en el registro", Toast.LENGTH_SHORT).show();
                                Log.wtf("registrarPrenda", response.getString("data"));
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );
        VolleyHttpRequest.getInstance(getContext()).getRequestQueue().add(jsArrayRequest);


    }

    private void loadCategorias() {
        Uri.Builder builder = Uri.parse(Constantes.URL_BASE + URL_CATEGORIAS).buildUpon();
        JSONObject faker = null;
        jsArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                builder.toString(),
                faker,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getInt("result") == 200) {
                                List<Categoria> data = new ArrayList<>();
                                JSONArray jArray = response.getJSONArray("categorias");
                                for (int i = 0; i < jArray.length(); i++) {
                                    JSONObject json_data = jArray.getJSONObject(i);
                                    Categoria categoria = new Categoria(json_data);
                                    data.add(categoria);
                                }
                                categoria_adapter = new
                                        ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, data);
                                categoria_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                _categoria.setAdapter(categoria_adapter);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );
        VolleyHttpRequest.getInstance(getContext()).getRequestQueue().add(jsArrayRequest);
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && data != null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            _img_prenda.setImageBitmap(photo);
            m_ropa.setColor(Util.intColorToHex(Util.getDominantColor1(photo)));
            picture = photo;
            //m_ropa.setFoto(photo);
            _color.setText(Util.intColorToHex(Util.getDominantColor1(photo)));
        } else {
            Toast.makeText(getContext(), "Peticion cancelada", Toast.LENGTH_SHORT).show();
        }
    }
}
