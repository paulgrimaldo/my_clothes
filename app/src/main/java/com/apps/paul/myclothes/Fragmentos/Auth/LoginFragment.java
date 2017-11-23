package com.apps.paul.myclothes.Fragmentos.Auth;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by paul on 11/21/2017.
 */

public class LoginFragment extends Fragment {


    public static final String TAG = "LoginFragment";
    private FragmentIterationListener mCallback = null;
    private EditText _emailText;
    private EditText _passwordText;
    JsonObjectRequest jsArrayRequest;
    private String URL_JSON = "signIn";

    public interface FragmentIterationListener {
        public void onClickLogin();
    }

    public static LoginFragment newInstance(Bundle arguments) {
        LoginFragment f = new LoginFragment();
        if (arguments != null) {
            f.setArguments(arguments);
        }
        return f;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (FragmentIterationListener) context;
        } catch (ClassCastException ex) {
            Log.e("ExampleFragment", "El Activity debe implementar la interfaz FragmentIterationListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, null);

        if (view != null) {
            _emailText = (EditText) view.findViewById(R.id.input_email);
            _passwordText = (EditText) view.findViewById(R.id.input_password);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        TextView noaccountYet = (TextView) view.findViewById(R.id.link_signup);
        noaccountYet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_login_register, RegisterFragment.newInstance(null), RegisterFragment.TAG)
                        .addToBackStack(null)
                        .commit();
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

            }
        });
        AppCompatButton button = (AppCompatButton) view.findViewById(R.id.btn_login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public boolean validate() {
        boolean valid = true;
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    public void login() {
        if (!validate()) {
            onLoginFailed();
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        onLoginSuccess();
                        progressDialog.dismiss();
                    }
                }, 1000);
    }

    public void onLoginSuccess() {
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        HashMap<String, String> parametros = new HashMap();
        parametros.put("email", email);
        parametros.put("password", password);

        jsArrayRequest = new JsonObjectRequest(
                Request.Method.POST,
                Constantes.URL_BASE + URL_JSON,
                new JSONObject(parametros),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject json_data = response.getJSONObject("user");
                            Toast.makeText(getContext(), "Hola " + json_data.getString("nombre"), Toast.LENGTH_LONG).show();
                            SharedPreferences preferences = getContext().getSharedPreferences("Current_User", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("email", json_data.getString("email"));
                            editor.putString("nombre", json_data.getString("nombre"));
                            editor.apply();
                            redirect();

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

    public void onLoginFailed() {
        Toast.makeText(getContext(), "Login failed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }
}
