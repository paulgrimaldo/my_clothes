package com.apps.paul.myclothes.Fragmentos.Auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.apps.paul.myclothes.NoticiasActivity;
import com.apps.paul.myclothes.R;
import com.apps.paul.myclothes.Utils.Constantes;
import com.apps.paul.myclothes.Utils.VolleyHttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

/**
 * Created by paul on 11/21/2017.
 */

public class RegisterFragment extends Fragment implements View.OnClickListener {
    private EditText input_nombre;
    private EditText input_email;
    private EditText input_pass;
    private EditText input_reEntered_pass;
    public static String TAG = "RegisterFragment";
    JsonObjectRequest jsArrayRequest;
    private String URL_REGISTRO = "signUp";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable

    public static RegisterFragment newInstance(Bundle args) {

        RegisterFragment fragment = new RegisterFragment();
        if (args != null) {

            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override // inflo la vista, tambien puedo asignar los valores de los imput
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_fragment, null);
        return view;
    }


    // asigrno las var a los inputs correspondientes
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        input_nombre = (EditText) view.findViewById(R.id.input_name);
        input_email = (EditText) view.findViewById(R.id.input_email);
        input_pass = (EditText) view.findViewById(R.id.input_password);
        input_reEntered_pass = (EditText) view.findViewById(R.id.input_reEnterPassword);
        TextView noaccountYet = (TextView) view.findViewById(R.id.link_login);
        noaccountYet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        Button btn_signup = (Button) view.findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(this);// this implements the event

    }

    /// el metodo para el boton que corresponde al signup
    @Override
    public void onClick(View view) {
        String pass, repass;
        pass = input_pass.getText().toString();
        repass = input_reEntered_pass.getText().toString();
        // valido todos los datos antes de continuar
        if (Objects.equals(pass, repass)) {
            if (validate()) {
                registrarUser();
            } else {
                Snackbar.make(view, "Correct the fields to continue", Snackbar.LENGTH_SHORT).show();
            }
        } else {
            Snackbar.make(view, "Password not equal", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void registrarUser() {
        String email = input_email.getText().toString();
        String password = input_pass.getText().toString();
        String nombre = input_nombre.getText().toString();
        HashMap<String, String> parametros = new HashMap();
        parametros.put("email", email);
        parametros.put("password", password);
        parametros.put("nombre", nombre);

        jsArrayRequest = new JsonObjectRequest(
                Request.Method.POST,
                Constantes.URL_BASE + URL_REGISTRO,
                new JSONObject(parametros),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int result = response.getInt("result");
                            if (result == 200) {
                                JSONObject json_data = response.getJSONObject("user");
                                Toast.makeText(getContext(), "Hola " + json_data.getString("nombre"), Toast.LENGTH_LONG).show();
                                SharedPreferences preferences = getContext().getSharedPreferences("Current_User", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("email", json_data.getString("email"));
                                editor.putString("nombre", json_data.getString("nombre"));
                                editor.apply();
                                redirect();
                            } else {
                                Toast.makeText(getContext(), "Registro fallido intente nuevamente", Toast.LENGTH_LONG).show();
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

    private void redirect() {
        Intent i = new Intent(getContext(), NoticiasActivity.class);
        getContext().startActivity(i);
        getActivity().finish();
    }

    public boolean validate() {
        boolean valid = true;

        String email = input_email.getText().toString();
        String password = input_pass.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            input_email.setError("enter a valid email address");
            valid = false;
        } else {
            input_email.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            input_pass.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            input_pass.setError(null);
        }

        return valid;
    }

}
